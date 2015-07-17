package com.cancai.demo.imageloadlib.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.cancai.demo.imageloadlib.cache.disk.BaseDiskLruCache;
import com.cancai.demo.imageloadlib.cache.disk.DiskCacheInterface;
import com.cancai.demo.imageloadlib.cache.disk.DiskLruCache;
import com.cancai.demo.imageloadlib.cache.disk.ReserveDiskCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 存储操作工具类
 *
 * @author peter_wang
 * @create-time 15/7/15 14:44
 */
public class StorageUtil {
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String WARNING_INIT_DISK_CACHE_FAIL = "init disk cache fail";
    /**
     * 保存图片缓存容量
     */
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 2; // 10MB
    /**
     * 保存图片文件流缓存
     */
    private static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 Kb
    /**
     * 缓存的图片目录
     */
    public static final String CACHE_DIRECTORY_NAME = "cache_pic";

    private StorageUtil() {

    }

    /**
     * 获取缓存图片的目录
     *
     * @param context 环境上下文
     * @return 缓存图片的目录
     */
    public static File getCacheDirectory(Context context) {
        File appCacheDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = context.getExternalCacheDir();
        }
        //获得SD卡缓存目录，API 8及以上可直接获取，以下需自行构建目录
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = context.getFilesDir().getPath() + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        appCacheDir = new File(appCacheDir, CACHE_DIRECTORY_NAME);
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 对url进行md5转换为唯一的文件名
     *
     * @param url 图片的 url
     * @return 对url进行md5之后的文件名
     */
    public static String hashKeyForDisk(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 创建磁盘缓存，默认缓存大小为{@link StorageUtil#DISK_CACHE_SIZE}10MB，创建失败则返回一个简单的磁盘缓存类，没有lrc功能
     *
     * @param context 环境上下文
     * @return 磁盘缓存对象
     */
    public static DiskCacheInterface createDiskCache(Context context) {
        DiskCacheInterface diskLruCache = null;
        File cacheDir = StorageUtil.getCacheDirectory(context);
        try {
            diskLruCache = new DiskLruCache(BaseDiskLruCache.open(cacheDir, 1, 1, DISK_CACHE_SIZE), DEFAULT_BUFFER_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
            L.w(WARNING_INIT_DISK_CACHE_FAIL);
        }

        //准备备胎，Lrc磁盘缓存类无法使用的时候，创建一个简单的磁盘缓存类
        if (diskLruCache == null) {
            diskLruCache = new ReserveDiskCache(cacheDir, DEFAULT_BUFFER_SIZE);
        }

        return diskLruCache;
    }

    /**
     * 保存图片流到磁盘流
     *
     * @param imageStream  图片流
     * @param outputStream 磁盘流
     * @return 是否保存成功
     */
    public static boolean saveImageStreamToDiskStream(InputStream imageStream, OutputStream outputStream, int bufferSize) {
        final byte[] bytes = new byte[bufferSize];
        int count;
        try {
            while ((count = imageStream.read(bytes, 0, bufferSize)) != -1) {
                outputStream.write(bytes, 0, count);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}  
