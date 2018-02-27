package com.itheima.ck.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * 步骤生成器中间结果
 */
public class ReportBuilderStepSubResult {
    public class ZipConfig {
        // 数据目录
        public String fileDir;
        // 目标文件, 使用正则来匹配
        public List<String> targetFile = new ArrayList<>();
        // zip解压后临时文件保存目录
        public String tmpZipFileDir = "C:\\Java\\测试数据\\201801\\tmp\\zip\\";
    }
    public ZipConfig zipConfig = new ZipConfig();

    public class VendorReportDailyStoreConfig {

        // 商户临时文件的根目录
        public String tmpVendorFileDir;
        public String headFileName = "head.txt";
    }

    public VendorReportDailyStoreConfig vendorConfig = new VendorReportDailyStoreConfig();
}
