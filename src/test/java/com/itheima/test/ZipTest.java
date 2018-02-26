package com.itheima.test;

import com.itheima.ck.config.Config;
import com.itheima.ck.util.FileUtils;
import com.itheima.ck.util.TxtHock;
import com.itheima.ck.util.XlsUtils;
import com.itheima.ck.util.XmlHock;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipTest {

    // 读取状态
    enum ReadStatus {
        FAILURE(false),
        SUCCESS(true);

        private boolean value;
        ReadStatus(boolean v) {
            value = v;
        }

        public boolean status() {
            return value;
        }
    }

    public static Logger logger = LoggerFactory.getLogger(ZipTest.class);

    public static void main(String[] args) {
        File root = new File(Config.fileDir);
        File[] files = root.listFiles();
        Map<String, Boolean> statusMap = new HashMap<>();
        // 第一遍
        for(File itemFile : files) {
            if(itemFile.isDirectory()) {
                continue;
            }
            try {
                zipParse(itemFile);
                statusMap.put(itemFile.getName(), true);
            } catch (FileNotFoundException e) {
                // 这个异常不处理
                // 简单打印出来
                logger.info("文件没有找到,文件名称是:{}", itemFile.getName());
            } catch (IOException e) {
                //e.printStackTrace();
                //这个异常需要记录
                logger.error("出现了IO异常:{}", e);
                statusMap.put(itemFile.getName(), false);
            }
        }
        //第二遍
        for(Map.Entry<String, Boolean> entry : statusMap.entrySet()) {
            String fileName = entry.getKey();
            Boolean status = entry.getValue();
            logger.info("文件{},存储临时目录{}", files, status);
            // 存储临时目录失败了..
            // 单独在做一遍
            if(!status) {
                //
                File targetFile = new File(Config.fileDir, fileName);
                try {
                    zipParse(targetFile);
                    statusMap.put(fileName, true);
                } catch (IOException e) {
                    logger.info("再次存入本地目录失败,文件为{},系统将会自动退出,系统会保存配置文件在临时目录", fileName);
                    statusMap.put(fileName, false);
                    break;
                }
            }
        }
        System.exit(0);
    }
    // 1. ZIP报表包解包
    public static void zipParse(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry nextEntry = null;
        while ((nextEntry = zis.getNextEntry()) != null) {
            // 判断是否为指定文件
            logger.error("目前遍历的文件名为:{}", nextEntry.getName());
            zis.closeEntry();
        }
        zis.close();
    }
    // 2. 解包报表文件分类存储到指定临时目录
    public static void saveToTmpDir(ZipEntry nextEntry, ZipInputStream zis) throws IOException {
        boolean isTxt = nextEntry.getName().matches(Config.targetFileName1);
        if(isTxt) {
            FileUtils.write(zis, Config.tmpZipfileDir, nextEntry.getName());
        }
        boolean isXls = StringUtils.contains( nextEntry.getName(), Config.targetFileName2);
        if(isXls) {
            FileUtils.write(zis, Config.tmpZipfileDir, nextEntry.getName());
        }
    }

    // 3. 保存记录文件
    public static void save(File file, ReadStatus rs) throws IOException {
        File protFile = new File(Config.tmpZipfileProt);
        FileOutputStream fos = new FileOutputStream(protFile, true);
        Writer writer = new OutputStreamWriter(fos, "UTF-8");
        //BufferedWriter bw = new BufferedWriter(writer);
        String tmp = file.getName() + "=" + rs.value + File.separator;
        writer.write(tmp);
        fos.close();
    }

    // 4.修改记录文件
    public static void alert(File file, ReadStatus rs) throws IOException {
        File protFile = new File(Config.tmpZipfileProt);
        FileInputStream fis = new FileInputStream(protFile);
        Reader reader = new InputStreamReader(fis, "UTF-8");
        Scanner scanner = new Scanner(reader);
        String fileName = file.getName();
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.contains(fileName)) {
                line = file.getName() + "=" + rs.value;
            }
            sb.append(line).append(File.separator);
        }
        scanner.close();
        reader.close();
        fis.close();

        FileOutputStream fos = new FileOutputStream(protFile, true);
        Writer writer = new OutputStreamWriter(fos, "UTF-8");
        //BufferedWriter bw = new BufferedWriter(writer);
        writer.write(sb.toString());
        writer.close();
        fos.close();
    }

    // 读取txt
    public static void readTxt(InputStream is) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);

        FileUtils.readLine(bis, "\\s+",  new TxtHockImpl());
        // ZIP的流不能这么关,傻逼
        //bis.close();
    }

    // 读取xls
    public static void readXls(InputStream is) throws IOException {
        //BufferedInputStream bis = new BufferedInputStream(is);

        XlsUtils.readExcel2003Or2007(is, new XlsHockImpl());
        // ZIP的流不能这么关,傻逼
        //bis.close();
    }
}

class TxtHockImpl implements TxtHock {
    private static Logger logger = LoggerFactory.getLogger(TxtHockImpl.class);

    @Override
    public void hock(List<String[]> cellData, int row) {
        // 打印数据
        for(String[] printData : cellData) {
            logger.info("接受到的数据为:{}", Arrays.toString(printData));
        }
        // 忽略文本的第一行
        if(row == 1) {
            return;
        }
        String[] cData = cellData.get(0);
        if(cData.length == 1 && cData[0].contains("=")) {
            return;
        }
        if(cData.length > 1) {
            // 头部
            // 1. 创建日期目录 -> 根据清算日期
            String strClearDate = cData[0];
            String clearDate = strClearDate.substring(strClearDate.indexOf("："));
            logger.info("日期目录为:{}", clearDate);
            // 2. 创建商户目录 -> 根据商户号
            String strClearVendor = cellData.get(1)[0];
            String clearVendor = strClearVendor.substring(strClearVendor.indexOf("："));
            logger.info("商户目录为:{}", clearVendor);
        } else {
            // 其他数据
            // 使用仆人模式,让仆人去读取并载入.
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
}

class XlsHockImpl implements XmlHock {
    private static Logger logger = LoggerFactory.getLogger(XlsHockImpl.class);

    @Override
    public void hock(List<Map<Integer, String>> cellData, int row) {
        for(Map<Integer, String> printData : cellData) {
            logger.info("接受到的数据为:{}", printData.values());
        }
        if(cellData.size() > 1) {
            // 头部
            // 1. 创建日期目录 -> 根据清算日期
            String strClearDate = cellData.get(0).get(0);
            String clearDate = strClearDate.substring(strClearDate.indexOf("："));
            logger.info("日期目录为:{}", clearDate);
            // 2. 创建商户目录 -> 根据商户号
            String strClearVendor = cellData.get(1).get(0);
            String clearVendor = strClearVendor.substring(strClearVendor.indexOf("："));
            logger.info("商户目录为:{}", clearVendor);
        } else {
            // 其他数据
            // 使用仆人模式,让仆人去读取并载入.
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
}
