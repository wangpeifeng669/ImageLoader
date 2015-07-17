package com.cancai.demo.imageloadlib.cache.disk;

import com.cancai.demo.imageloadlib.utils.StorageUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 备用的磁盘缓存，只有简单的磁盘缓存功能
 *
 * @author peter_wang
 * @create-time 15/7/16 14:14
 */
public class ReserveDiskCache implements DiskCacheInterface {
    private File mCacheDirectory;
    /**
     * 保存图片文件流缓存
     */
    private int mBufferSize;

    public ReserveDiskCache(File cacheDirectory, int bufferSize) {
        this.mCacheDirectory = cacheDirectory;
        this.mBufferSize = bufferSize;
    }

    @Override
    public InputStream getImageFromDisk(String url) {
        String key = StorageUtil.hashKeyForDisk(url);
        File imageFile = new File(mCacheDirectory, key);
        if (imageFile.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(imageFile);
                return fileInputStream;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean saveImageToDisk(String url, InputStream imageStream) {
        String key = StorageUtil.hashKeyForDisk(url);
        File imageFile = new File(mCacheDirectory, key);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            OutputStream outputStream = new BufferedOutputStream(fileOutputStream, mBufferSize);
            if (StorageUtil.saveImageStreamToDiskStream(imageStream, outputStream, mBufferSize)) {
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
