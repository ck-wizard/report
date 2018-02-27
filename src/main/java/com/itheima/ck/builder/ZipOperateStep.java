package com.itheima.ck.builder;

import com.itheima.ck.util.FileUtils;
import com.itheima.ck.util.hock.FileIteraHock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 步骤生成器 第一步
 * zip文件解包分类存储
 */
public class ZipOperateStep implements OperateStep, FileIteraHock{
    private OperateStep nextStep;
    ReportBuilderStepSubResult subResult;
    Map<String, Boolean> statusMap = new HashMap<>();
    public static Logger logger = LoggerFactory.getLogger(ZipOperateStep.class);
    public ZipOperateStep(OperateStep nextStep) {
        this.nextStep = nextStep;
    }
    @Override
    public void invoke(ReportBuilderStepSubResult subResult) {
        this.subResult = subResult;
        // 你需要做的操作....
        // 确定临时目录的地址
        subResult.zipConfig.tmpZipFileDir = subResult.zipConfig.fileDir + "\\tmp\\zip\\";

        // 开始遍历
        File root = new File(subResult.zipConfig.fileDir);
        FileUtils.iterable(root, this);

        // 第一遍遍历完成后,检查一下,如需要进行第二遍遍历,那么继续
        for(Map.Entry<String, Boolean> entry : statusMap.entrySet()) {
            String fileName = entry.getKey();
            Boolean status = entry.getValue();
            logger.info("文件{},存储临时目录{}", fileName, status);
            // 存储临时目录失败了..
            // 单独在做一遍
            if(!status) {
                //
                File targetFile = new File(subResult.zipConfig.fileDir, fileName);
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
        nextStep.invoke(subResult);
    }

    // 1. ZIP报表包解包
    public void zipParse(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry nextEntry = null;
        while ((nextEntry = zis.getNextEntry()) != null) {
            // 判断是否为指定文件
            logger.info("目前遍历的文件名为:{}", nextEntry.getName());
            saveToTmpDir(nextEntry, zis);
            zis.closeEntry();
        }
        zis.close();
    }
    // 2. 解包报表文件分类存储到指定临时目录
    public void saveToTmpDir(ZipEntry nextEntry, ZipInputStream zis) throws IOException {
        for(String targetFileName : subResult.zipConfig.targetFile) {
            String zipName = nextEntry.getName();
            if(zipName.matches(targetFileName)) {
                boolean isTxt = zipName.endsWith(".txt");
                if(isTxt) {
                    FileUtils.write(zis, subResult.zipConfig.tmpZipFileDir, nextEntry.getName());
                }
                boolean isXls = zipName.endsWith(".xls");
                if(isXls) {
                    FileUtils.write(zis, subResult.zipConfig.tmpZipFileDir, nextEntry.getName());
                }
            }

        }

    }

    @Override
    public void hock(File itemFile) {
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

}


