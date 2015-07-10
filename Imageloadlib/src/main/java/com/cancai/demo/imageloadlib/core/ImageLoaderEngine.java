package com.cancai.demo.imageloadlib.core;

import com.cancai.demo.imageloadlib.core.viewcontainer.ViewContainer;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 图片下载展示线程池
 *
 * @author peter_wang
 * @create-time 15/7/2 14:08
 */
class ImageLoaderEngine {
    private final ConcurrentHashMap<Integer, String> mHashMap = new ConcurrentHashMap<>();
    private final Map<String, ReentrantLock> mUriLocks = new WeakHashMap<>();
    private final Executor mExecutor;

    public ImageLoaderEngine() {
        mExecutor = Executors.newFixedThreadPool(3);
    }

    public void submit(DownloadAndDisplayImageTask downloadAndDisplayImageTask) {
        mExecutor.execute(downloadAndDisplayImageTask);
    }

    /**
     * 准备开启线程显示图片
     *
     * @param viewContainer 图片显示的view容器
     * @param url           图片地址
     */
    public void prepareDisplayTask(ViewContainer viewContainer, String url) {
        mHashMap.put(viewContainer.getId(), url);
    }

    /**
     * 获取该view容器对应的图片地址
     *
     * @param viewContainer 图片显示的view容器
     * @return 图片地址
     */
    public String getTaskUrl(ViewContainer viewContainer) {
        return mHashMap.get(viewContainer.getId());
    }

    /**
     * 图片显示成功，取消对控件和下载地址的绑定
     *
     * @param viewContainer 图片显示的view容器
     */
    public void successDisplayTask(ViewContainer viewContainer) {
        mHashMap.remove(viewContainer.getId());
    }

    /**
     * 获取 url 对应的锁，在下载某一 url 的时候，如果再次请求同一 url 设置等待状态，下载成功直接取消继续执行，失败则继续执行
     *
     * @param url 图片显示的 url
     * @return 图片url下载锁
     */
    public ReentrantLock getUrlLock(String url) {
        ReentrantLock reentrantLock = mUriLocks.get(url);
        if (reentrantLock == null) {
            reentrantLock = new ReentrantLock();
            mUriLocks.put(url, reentrantLock);
        }
        return reentrantLock;
    }
}  
