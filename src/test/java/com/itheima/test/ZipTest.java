package com.itheima.test;

import com.itheima.ck.util.FileUtils;
import com.itheima.ck.util.XlsUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipTest {
    public final static String fileDir = "C:\\Java\\测试数据\\report";

    public final static String targetFileName1 = "1.txt";
    public final static String targetFileName2 = "x-2.xls";


    public static void main(String[] args) {
        File root = new File(fileDir);
        File[] files = root.listFiles();
        for(File itemFile : files) {
            if(itemFile.isDirectory()) {
                continue;
            }

            try {
                zipParse(itemFile);
            } catch (FileNotFoundException e) {
                // 这个异常不处理
            } catch (IOException e) {
                //e.printStackTrace();
                //这个异常需要记录
            }
        }
    }
    // 1. ZIP报表包解包
    public static void zipParse(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry nextEntry = null;
        while ((nextEntry = zis.getNextEntry()) != null) {
            // 判断是否为指定文件
            boolean isTxt = StringUtils.equals(targetFileName1, nextEntry.getName());
            if(isTxt) {
                readTxt(zis);
            }
            boolean isXls = StringUtils.equals(targetFileName2, nextEntry.getName());
            if(isXls) {
                readXls(zis);
            }
            zis.closeEntry();
            if(!isXls && !isTxt)
                continue;

            // 组装信息
            // 文件名|本行/跨行|几号|状态
            Map<String, String> saveInfoMap = null;
            //记录要保存的信息
            save(saveInfoMap);
        }
        zis.close();
    }
    // 2. 解包报表文件分类存储到指定临时目录

    // 3. 记录读取文件
    public static void save(Map<String, String> info) {

    }

    // 读取txt
    public static void readTxt(InputStream is) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);

        FileUtils.readLine(bis, " ",  (a1, row) -> {

        });
        bis.close();
    }

    // 读取xls
    public static void readXls(InputStream is) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);

        XlsUtils.readExcel2003Or2007(bis, (a1, a2) -> {

        });
        bis.close();
    }
}
