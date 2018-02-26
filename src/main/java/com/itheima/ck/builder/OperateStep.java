package com.itheima.ck.builder;

/**
 * 操作步骤类
 */
public interface OperateStep {

    /**
     * 执行下一步
     * @param subResult 存储中间结果,供下一步使用
     */
    void invoke(ReportBuilderStepSubResult subResult);
}
