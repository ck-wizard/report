package com.itheima.ck.util.servant;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * 转换数据为指定类的仆人模式
 */
public interface BeanServant<T> {

    /**
     * 读取跨行零售
     * @param t 当前读取的bean, 若检查读取字段包括清算日志,那么新创建bean,返回新的bean
     * @param map 数据
     * @return
     */
    T transferXls(T t, Map<Integer, String> map);

    /**
     * 读取本行零售
     * @param t 当前读取的bean, 若检查读取字段包括清算日志,那么新创建bean,返回新的bean
     * @param lists 数据
     * @return
     */
    T transferTxt(T t, String[] lists);

    /**
     * 格式输出
     * @param t
     */
    void formatPrint(PrintWriter pw, T t);

}
