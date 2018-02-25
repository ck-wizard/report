package com.itheima.ck.util;

import java.util.List;
import java.util.Map;

// 处理数据的钩子
public interface XmlHock {
    /**
     * 钩子类
     * @param cellData 每行数据
     * @param row 行号
     */
    void hock(Map<Integer, String> cellData, int row);
}
