package com.itheima.ck.builder;

/**
 * 步骤生成器 第一步
 * zip文件解包分类存储
 */
public class ZipOperateStep implements OperateStep{
    private OperateStep nextStep;

    public ZipOperateStep(OperateStep nextStep) {
        this.nextStep = nextStep;
    }
    @Override
    public void invoke(ReportBuilderStepSubResult subResult) {
        // 你需要做的操作....

        nextStep.invoke(subResult);
    }
}
