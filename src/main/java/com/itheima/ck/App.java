package com.itheima.ck;

import com.itheima.ck.builder.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 入口类
public class App {
    public static Logger logger = LoggerFactory.getLogger(App.class);

//    public static void main(String[] args) {
//        logger.info("给我输出日志啊!!");
//        logger.info("给我输出日志啊!!");
//    }

    public static void main(String[] args) {
        OperateStep step_0 = new EmptyStep();
        OperateStep step_2 = new VendorReportDailyStoreStep(step_0);
       // OperateStep step_1 = new ZipOperateStep(step_2);
        // 配置文件
        ReportBuilderStepSubResult subResult = new ReportBuilderStepSubResult();
        subResult.zipConfig.fileDir = "C:\\Java\\测试数据\\201801\\";
//        subResult.zipConfig.targetFile.add("GroupBalanceDetail\\w{0,7}\\d{8}.xls");
//        subResult.zipConfig.targetFile.add("RetailBalanceDetail\\w{0,7}\\d{8}.txt");
        subResult.zipConfig.targetFile.add("GroupBalanceDetail\\d{8}.xls");
        subResult.zipConfig.targetFile.add("RetailBalanceDetail\\d{8}.txt");
        subResult.zipConfig.targetFile.add("GroupBalanceDetailPaygate\\d{8}.xls");
        subResult.zipConfig.targetFile.add("RetailBalanceDetailPaygate\\d{8}.txt");
        // 执行
        //step_1.invoke(subResult);
        step_2.invoke(subResult);
        System.exit(0);
    }

}
