package com.itheima.ck.util;

import com.itheima.ck.util.hock.DirHock;
import com.itheima.ck.util.hock.FileIteraHock;
import com.itheima.ck.util.hock.TxtHock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

// 文件工具
public class FileUtils {

    private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;
    // buffer size used for reading and writing
    private static final int BUFFER_SIZE = 8192; //8KB

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);
    /**
     * 读取流,限速型.
     * @param is
     * @param initialSize
     * @return
     */
    public static byte[] readInputStream(InputStream is, int initialSize) {
        BufferedInputStream bis = null;
        if(is instanceof BufferedInputStream) {
            bis = (BufferedInputStream) is;
        } else {
            bis = new BufferedInputStream(is);
        }
        try {
            int available = is.available();
        } catch (IOException e) {
            logger.info("获取可读入总长度失败", e);
        }
        int BufferSize = initialSize;
        byte[] bytes = new byte[initialSize];
        int index = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while((index = bis.read(bytes, 0, BufferSize)) > 0) {
                baos.write(bytes, 0, index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            baos.close();
            bis.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static byte[] read(InputStream source, int initialSize) throws IOException {
        int capacity = initialSize;
        byte[] buf = new byte[capacity];
        // 已读
        int nread = 0;
        // 接收
        int n;

        // 每次读取更多, 每次尝试多读取一位, 因为最后一次读取可能为0, 在读一位就是-1.
        // 这思想真牛逼, 我喜欢.
        for (;;) {
            // read to EOF which may read more or less than initialSize (eg: file
            // 读取到EOF, 读的时候可能比 初始化长度 多或者少
            // is truncated while we are reading)
            // (当我在读取的时候文件被截断.)
            while ((n = source.read(buf, nread, capacity - nread)) > 0)
                nread += n;

            // if last call to source.read() returned -1, we are done
            // 如果最后调用source.read() 返回的 -1, 我是完成的
            // otherwise, try to read one more byte; if that failed we're done too
            // 否则, 尝试去读更多字节; 如果失败了,我们也完蛋了
            if (n < 0 || (n = source.read()) < 0)
                break;

            // one more byte was read; need to allocate a larger buffer
            if (capacity <= MAX_BUFFER_SIZE - capacity) {
                capacity = Math.max(capacity << 1, BUFFER_SIZE);
            } else {
                if (capacity == MAX_BUFFER_SIZE)
                    throw new OutOfMemoryError("Required array size too large");
                capacity = MAX_BUFFER_SIZE;
            }
            buf = Arrays.copyOf(buf, capacity);
            buf[nread++] = (byte)n;
        }
        return (capacity == nread) ? buf : Arrays.copyOf(buf, nread);
    }

    /**
     * 赋权
     * @param path 需要权限的路径
     */
    public static void authorizationAll(Path path) {
        Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        try {
            Files.setPosixFilePermissions(path, perms);
        } catch (Exception e) {
            logger.info("Change folder " + path + " permission failed.");
        }
    }

    /**
     * 以钩子的形式 一行一行的读数据
     * @param is
     * @param splitCharacter
     * @param hock
     */
    public static void readLine(InputStream is, String splitCharacter, TxtHock hock) throws IOException{
        Scanner scanner = new Scanner(is, "gb2312");
        int row = 0;
        List<String[]> complateLine = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] cellData = line.split(splitCharacter);
            row++;

            if(hock.hasMore(line)) {
                complateLine.add(new String[]{line});
            } else {
                complateLine.add(cellData);
                hock.hock(complateLine, row);
                complateLine.clear();
            }
        }
        scanner.close();
    }

    public static void write(InputStream is, String dir, String fileName) throws IOException {
        byte[] read = FileUtils.read(is, 2048);
        write(read, dir, fileName);
    }

    public static void write(byte[] read, String dir, String fileName) throws IOException {
        File dirFile = new File(dir);
        if(!dirFile.exists()) {
            if(!dirFile.mkdirs()) {
                throw new IOException("创建目录"+ dir +"失败");
            }
        }
        File file = new File(dir, fileName);
        if(file.exists()) {
            if(!file.delete()) {
                //删除失败了
                throw new IOException("删除文件" + fileName + "失败");
            }
        }

        write(read, file);
    }

    public static void write(byte[] read, File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        os.write(read);
        os.close();
    }



    /**
     * 文本遍历工具
     * @param dirFile 需要迭代的目录
     * @param hock 钩子
     */
    public static void iterable(File dirFile, FileIteraHock hock) {
        //File dirFile = new File(dir);
        if(dirFile.isDirectory()) {
            File[] files = dirFile.listFiles();
            for(File itemFile : files) {
                if(itemFile.isDirectory()) {
                    iterable(itemFile, hock);
                }
                hock.hock(itemFile);
            }
        } else {
            hock.hock(dirFile);
        }

    }


    public static File createFile(String dirPath, String fileName) throws IOException {
        File file = new File(dirPath, fileName);
        if(!file.exists()) {
            if(!file.createNewFile()) {
                // 创建目录失败了.
                throw new IOException("创建文件"+file.getAbsolutePath()+"失败了");
            }
        }
        return file;
    }

    public static File createDir(String dirPath, String subDirPath) throws IOException {
        File vendorDir = new File(dirPath, subDirPath);
        if(!vendorDir.exists()) {
            if(!vendorDir.mkdir()) {
                // 创建目录失败了.
                throw new IOException("创建文件夹"+vendorDir+"失败了");
            }
        }
        return vendorDir;
    }
}
