package com.cancai.demo.imageloadlib.cache.disk;

import java.io.InputStream;

/**
 * 磁盘缓存接口
 *
 * @author peter_wang
 * @create-time 15/7/16 13:47
 */
public interface DiskCacheInterface {
    /**
     * 获取磁盘中对应图片下载地址的图片流
     *
     * @param url 图片的下载地址
     * @return 图片流
     */
    public InputStream getImageFromDisk(String url);

    /**
     * 保存图片到磁盘
     *
     * @param url         图片的下载地址
     * @param imageStream 图片流
     * @return 是否保存成功
     */
    public boolean saveImageToDisk(String url, InputStream imageStream);
}  
