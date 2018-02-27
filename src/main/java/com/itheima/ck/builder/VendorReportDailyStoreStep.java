package com.itheima.ck.builder;

import com.itheima.ck.bean.MonthTransferModel;
import com.itheima.ck.bean.VendorSubInfoModel;
import com.itheima.ck.util.FileUtils;
import com.itheima.ck.util.Repeatable;
import com.itheima.ck.util.hock.FileIteraHock;
import com.itheima.ck.util.hock.TxtHock;
import com.itheima.ck.util.XlsUtils;
import com.itheima.ck.util.hock.XlsHock;
import com.itheima.ck.util.servant.MonthTransferModelServant;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

// 按天存储商户日报
// 第二步
// 步骤目标
/*3. 按天读取临时目录的表报文件
4. 按商户分类创建文件夹存储单个商户的报表文件
5. 商户存储报表文件按天创建目录进行存储*/
public class VendorReportDailyStoreStep implements OperateStep, FileIteraHock {
    private OperateStep nextStep;
    private static Logger logger = LoggerFactory.getLogger(VendorReportDailyStoreStep.class);

    private ReportBuilderStepSubResult subResult;
    private Map<String, Boolean> statusMap = new HashMap<>();
    TxtHock txtHock = new TxtHockImpl();
    XlsHock xlsHock = new XlsHockImpl();
    public VendorReportDailyStoreStep(OperateStep nextStep) {
        this.nextStep = nextStep;
    }

    @Override
    public void invoke(ReportBuilderStepSubResult subResult) {
        this.subResult = subResult;
        // 你的操作
        // 1.获取父文件位置
        // 2.创建分类临时文件
        // 3.获取zip解压文件位置
        // 4.按天解析zip文件
        // 5.创建商户文件
        // 6.按天追加文件到指定目录
        // 7.存储商户分类存储目录到中间结果,传递给下一步.

        String tmpDirFile = subResult.zipConfig.fileDir + "tmp\\vendor\\";
        subResult.vendorConfig.tmpVendorFileDir = tmpDirFile;
        File dirFile = new File(tmpDirFile);
        if(!dirFile.exists()) {
            if(!dirFile.mkdir()) {
                logger.error("创建存储商户的文件夹{}失败", dirFile.getName());
                return;
            }
        }
        File zipDir = new File(subResult.zipConfig.tmpZipFileDir);
        FileUtils.iterable(zipDir, this);
        // 这里也要文件记录完成情况
        // 遍历查看完成情况
        for(Map.Entry<String, Boolean> entry : statusMap.entrySet()) {
            String fileName = entry.getKey();
            Boolean status = entry.getValue();
            if(!status) {
                logger.info("文件{}没有成功被解析,状态为{},程序以自动退出", fileName, status);
            }
        }
        nextStep.invoke(subResult);
    }

    @Override
    public void hock(File itemFile) {
        String itemName = itemFile.getName();
        logger.error("目前读取的文件为{}", itemName);
        boolean isTxt = itemName.endsWith(".txt");
        boolean isXls = itemName.endsWith(".xls");
        List<VendorSubInfoModel> infos = null;
        try {
            FileInputStream fis = new FileInputStream(itemFile);
            BufferedInputStream bis = new BufferedInputStream(fis);

            if(isTxt) {
                FileUtils.readLine(bis, "\\s+", txtHock);
                infos= txtHock.getData();
            }
            if(isXls) {
                XlsUtils.readExcel2003Or2007(bis, xlsHock);
                infos= xlsHock.getData();
            }
            bis.close();
            fis.close();

            statusMap.put(itemName, true);

        } catch (FileNotFoundException e) {
            logger.info("文件{}没有找到", itemName);

        } catch (IOException e) {
            logger.info("读取文件时发生了IO异常", e);
            statusMap.put(itemName, false);
        }
        // 去解析数据啦.
        // 1.创建文件夹,文件
        // 如果当天报表一个商户都没有,直接返回.
        try {
            for (VendorSubInfoModel info : infos) {
                if (StringUtils.isEmpty(info.getClaarVendorLine())) continue;
                File vendorDir = createVendorDir(info.getClaarVendorLine());
                File vendorFile = createVendorDateFile(vendorDir.getAbsolutePath(), info.getClearDateLine().trim());
                // 只使用txt的来创建
                // 第一次创建的时候需要把头部信息保存到head.txt中
                // 拼装数据
                if(info.isTxt()) {
                    String headName = vendorDir.getAbsolutePath() + File.separator + subResult.vendorConfig.headFileName;
                    File file = new File(headName);
                    // 已经存在了,那么不需要创建了.
                    if(!file.exists()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(info.getClearDateLine()).append(System.getProperty("line.separator"));
                        sb.append(info.getClaarVendorLine()).append(System.getProperty("line.separator"));
                        sb.append(info.getClearVendorNameLine()).append(System.getProperty("line.separator"));
                        sb.append(info.getVendorInfoHead()).append(System.getProperty("line.separator"));
                        // 创建head.txt文件
                        FileUtils.write(sb.toString().getBytes(), file);
                    }
                }
                // 写入商户的数据
                FileOutputStream fos = new FileOutputStream(vendorFile, true);
                PrintWriter pw = new PrintWriter(vendorFile, "UTF-8");
                List<MonthTransferModel> vendorData = info.getVendorData();
                for(MonthTransferModel vendor : vendorData) {
                    MonthTransferModelServant.servant.formatPrint(pw, vendor);
                }
                pw.close();
                fos.close();
            }
        } catch (IOException e) {
            logger.info("创建文件时发生了IO异常", e);
        }
        ((Repeatable)xlsHock).clear();
        ((Repeatable)txtHock).clear();
        // 2. 分类存储数据

    }


    public File createVendorDir(String strClearVendor) throws IOException {

        String clearVendor = strClearVendor.substring(strClearVendor.indexOf("：")+1);
        //logger.info("商户目录为:{}", clearVendor);
        String tmpVendorFileDir = subResult.vendorConfig.tmpVendorFileDir;
        File vendorDir = FileUtils.createDir(tmpVendorFileDir, clearVendor);

        return vendorDir;
    }

    public File createVendorDateFile(String vendorDirPath, String strClearDate) throws IOException  {
        String clearDate = strClearDate.substring(strClearDate.indexOf("：")+1);
        //logger.info("日期目录为:{}", clearDate);
        //String vendorDirPath = vendorDir.getAbsolutePath();
        return FileUtils.createFile(vendorDirPath, clearDate);
    }
}

// 该类中用来读取数据..
class TxtHockImpl implements TxtHock, Repeatable {
    Logger logger = LoggerFactory.getLogger(TxtHockImpl.class);

    List<VendorSubInfoModel> infos;
    VendorSubInfoModel currentInfo;
    public TxtHockImpl() {
        infos = new ArrayList<>();
    }

    @Override
    public void hock(List<String[]> cellData, int row) throws IOException {
        // 打印数据
        if(cellData.size() == 0) {
            logger.info("行{}没有数据", row);
        }
        if(cellData.size() > 1) {
            for (String[] printData : cellData) {
                //sb.append(Arrays.toString(printData));
                logger.info("row:{},Txt接受到的数据为:{}",row,  Arrays.toString(printData));
            }
        }

        // 忽略文本的第一行
        if(row == 1) {
            return;
        }
        // 忽略空行
        String[] cData = cellData.get(0);
        if(cData.length == 0) {
            return;
        }
        if(cellData.size() == 1 && cData[0].contains("=")) {
            // 会有一整天没有销售的情况
            if(null != currentInfo) {
                currentInfo.setOtherLine(cData[0]);
            }
            return;
        }
        if(cellData.size() == 1 && cData[0].contains("合计")) {
            // 合计行 自动跳过
            return;
        }

        if(cellData.size() > 1) {
            VendorSubInfoModel info = new VendorSubInfoModel();
            // 头部
            // 1. 创建商户目录 -> 根据商户号
            // 2. 创建日期文件 -> 根据清算日期
            String strClearDate = cData[0];
            info.setClearDateLine(strClearDate.trim());
            info.setClaarVendorLine(cellData.get(1)[0].trim());
            info.setClearVendorNameLine(cellData.get(2)[0].trim());
            info.setVendorInfoHead(cellData.get(3)[0].trim());
            info.setTxt(true);
            infos.add(info);
            currentInfo = info;
        } else {
            // 其他数据 忽略合计行, 忽略空行
            // 使用仆人模式,让仆人去读取并载入.
            // 一个文件读取完成后,最后才写入到指定的文件中去.
            MonthTransferModel model = new MonthTransferModel();
            MonthTransferModelServant.servant.transferTxt(model, cData);
            currentInfo.add(model);
        }
    }

    @Override
    public boolean hasMore(String line) {
        if(line.contains("清算日期")) {
            return true;
        }
        if(line.contains("清算商户号")) {
            return true;
        }
        if(line.contains("清算商户名称")) {
            return true;
        }
        if(line.contains("卡号")) {
            return true;
        }
        return false;
    }

    @Override
    public List<VendorSubInfoModel> getData() {
        return infos;
    }

    @Override
    public void clear() {
        currentInfo = null;
        if(null != infos) {
            for (VendorSubInfoModel info : infos) {
                info.getVendorData().clear();
            }
            infos.clear();
        }
    }
}

class XlsHockImpl implements XlsHock, Repeatable {
    private static Logger logger = LoggerFactory.getLogger(XlsHockImpl.class);

    List<VendorSubInfoModel> infos;
    VendorSubInfoModel currentInfo;
    public XlsHockImpl() {
        infos = new ArrayList<>();
    }

    @Override
    public void hock(List<Map<Integer, String>> cellData, int row) throws IOException{

        StringBuilder sb = new StringBuilder();
        if(cellData.size() > 1) {
            for (Map<Integer, String> printData : cellData) {
                //sb.append(printData.values().toString());
                logger.info("Xls接受到的数据为:{}", printData.values());
            }
        }

        Map<Integer, String> cData = cellData.get(0);
        if(cellData.size() == 1 && cData.get(0).contains("合计")) {
            // 合计行 自动跳过
            return;
        }


        if(cellData.size() > 1) {
            VendorSubInfoModel info = new VendorSubInfoModel();
            // 头部
            // 1. 创建商户目录 -> 根据商户号
            // 2. 创建日期文件 -> 根据清算日期
            String strClearDate = cellData.get(0).get(0);
            info.setClearDateLine(strClearDate);
            info.setClaarVendorLine(cellData.get(1).get(0));
            info.setClearVendorNameLine(cellData.get(2).get(0));
            infos.add(info);
            currentInfo = info;
        } else {
            // 其他数据 忽略合计行, 忽略空行

            // 使用仆人模式,让仆人去读取并载入.
            // 一个文件读取完成后,最后才写入到指定的文件中去.
            MonthTransferModel model = new MonthTransferModel();
            MonthTransferModelServant.servant.transferXls(model, cellData.get(0));
            currentInfo.add(model);
        }
    }

    @Override
    public boolean hasMore(Map<Integer, String> line) {
        String first_str = line.get(0);
        //logger.info("xls 第一个单元数据为:{}", first_str);
        if(first_str.contains("清算日期")) {
            return true;
        }
        if(first_str.contains("清算商户号")) {
            return true;
        }
        if(first_str.contains("清算商户名称")) {
            return true;
        }
        return false;
    }

    @Override
    public List<VendorSubInfoModel> getData() {
        return infos;
    }

    @Override
    public void clear() {
        currentInfo = null;
        if(null != infos) {
            for (VendorSubInfoModel info : infos) {
                info.getVendorData().clear();
            }
            infos.clear();
        }
    }
}
