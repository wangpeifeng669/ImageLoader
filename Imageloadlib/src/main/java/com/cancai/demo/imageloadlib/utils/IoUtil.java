package com.cancai.demo.imageloadlib.utils;

import java.io.Closeable;

/**
 * 流工具类
 *
 * @author peter_wang
 * @create-time 15/7/2 15:10
 */
public class IoUtil {
    /**
     * 关闭流
     *
     * @param closeable 流
     */
    public static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
    }
}  
