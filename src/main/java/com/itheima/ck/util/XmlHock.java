package com.itheima.ck.util;

import java.util.List;
import java.util.Map;

// 处理数据的钩子
public interface XmlHock {
    /**
     * 钩子类
     * 文本文件读一行
     * 当有多行时,row为最后一个数据的行数.
     * 意思是:若list长度为3.那么get(0)的行号应该是row - length + index
     * @param cellData 每行数据
     * @param row 行号
     *
     */
    void hock(List<Map<Integer, String>> cellData, int row);

    /**
     * 读取一行后进行判断,是否还有,
     * @param line
     * @return 如果为true表示继续读,返回false表示不继续读取.
     */
    boolean hasMore(Map<Integer, String> line);
}
