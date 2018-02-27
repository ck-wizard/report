package com.itheima.test;

import com.itheima.ck.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

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
        logger.info("不打印日志了?");
        System.exit(0);
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


}


