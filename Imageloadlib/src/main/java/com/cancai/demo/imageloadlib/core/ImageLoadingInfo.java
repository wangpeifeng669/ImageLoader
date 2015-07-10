package com.cancai.demo.imageloadlib.core;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.cancai.demo.imageloadlib.core.viewcontainer.ViewContainer;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 图片加载信息
 *
 * @author peter_wang
 * @create-time 15/7/3 13:45
 */
public class ImageLoadingInfo {
    private ViewContainer mPicView;
    private ImageLoaderEngine mImageLoaderEngine;
    private String mImageUrl;
    private ImageDownloader mDownloader;
    private LruCache<String, Bitmap> mMemoryCache;
    /**
     * 对同一个 url 请求，请求中让其他相同 url 的请求等待
     */
    private ReentrantLock mReentrantLock;

    public ViewContainer getPicView() {
        return mPicView;
    }

    public void setPicView(ViewContainer picView) {
        mPicView = picView;
    }

    public ImageLoaderEngine getImageLoaderEngine() {
        return mImageLoaderEngine;
    }

    public void setImageLoaderEngine(ImageLoaderEngine imageLoaderEngine) {
        mImageLoaderEngine = imageLoaderEngine;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public ImageDownloader getDownloader() {
        return mDownloader;
    }

    public void setDownloader(ImageDownloader downloader) {
        mDownloader = downloader;
    }

    public LruCache<String, Bitmap> getMemoryCache() {
        return mMemoryCache;
    }

    public void setMemoryCache(LruCache<String, Bitmap> memoryCache) {
        mMemoryCache = memoryCache;
    }

    public ReentrantLock getReentrantLock() {
        return mReentrantLock;
    }

    public void setReentrantLock(ReentrantLock reentrantLock) {
        mReentrantLock = reentrantLock;
    }
}
