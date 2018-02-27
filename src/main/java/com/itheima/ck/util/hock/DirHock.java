package com.itheima.ck.util.hock;

import java.io.File;
import java.io.IOException;

// 目录的钩子
public interface DirHock {

    /**
     * 当文件真实被创建后会被调用
     * @param dirFile
     */
    void create(File dirFile) throws IOException;
}
