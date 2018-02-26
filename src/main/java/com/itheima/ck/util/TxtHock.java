package com.itheima.ck.util;

import java.util.List;

public interface TxtHock {
    /**
     * 文本文件读一行
     * 当有多行时,row为最后一个数据的行数.
     * 意思是:若list长度为3.那么get(0)的行号应该是row - length + index
     * @param cellData 行数据
     * @param row 行号
     */
    void hock(List<String[]> cellData, int row);

    /**
     * 读取一行后进行判断,是否还有,
     * @param line
     * @return 如果为true表示继续读,返回false表示不继续读取.
     */
    boolean hasMore(String line);
}
