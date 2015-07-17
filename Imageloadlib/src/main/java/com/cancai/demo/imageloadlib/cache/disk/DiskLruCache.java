package com.cancai.demo.imageloadlib.cache.disk;

import com.cancai.demo.imageloadlib.utils.IoUtil;
import com.cancai.demo.imageloadlib.utils.StorageUtil;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 磁盘缓存
 *
 * @author peter_wang
 * @create-time 15/7/16 09:53
 */
public class DiskLruCache implements DiskCacheInterface {
    private final BaseDiskLruCache mBaseDiskLruCache;
    /**
     * 保存图片文件流缓存
     */
    private int mBufferSize;

    public DiskLruCache(BaseDiskLruCache mBaseDiskLruCache, int bufferSize) {
        this.mBaseDiskLruCache = mBaseDiskLruCache;
        this.mBufferSize = bufferSize;
    }

    /**
     * 获取磁盘中对应图片下载地址的图片流
     *
     * @param url 图片的下载地址
     * @return 图片流
     */
    @Override
    public InputStream getImageFromDisk(String url) {
        String key = StorageUtil.hashKeyForDisk(url);
        try {
            BaseDiskLruCache.Snapshot snapShot = mBaseDiskLruCache.get(key);
            if (snapShot != null) {
                return snapShot.getInputStream(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存图片到磁盘
     *
     * @param url         图片的下载地址
     * @param imageStream 图片流
     * @return 是否保存成功
     */
    @Override
    public boolean saveImageToDisk(String url, InputStream imageStream) {
        String key = StorageUtil.hashKeyForDisk(url);
        OutputStream outputStream = null;
        try {
            BaseDiskLruCache.Editor editor = mBaseDiskLruCache.edit(key);
            if (editor != null) {
                outputStream = new BufferedOutputStream(editor.newOutputStream(0), mBufferSize);
                if (StorageUtil.saveImageStreamToDiskStream(imageStream, outputStream, mBufferSize)) {
                    editor.commit();
                    return true;
                } else {
                    editor.abort();
                }
            }
            mBaseDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtil.closeStream(outputStream);
        }
        return false;
    }
}
