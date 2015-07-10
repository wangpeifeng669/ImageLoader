package com.cancai.demo.imageloadlib.core;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.util.LruCache;

import com.cancai.demo.imageloadlib.utils.L;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 下载并显示图片
 *
 * @author peter_wang
 * @create-time 15/7/6 10:21
 */
public class DownloadAndDisplayImageTask implements Runnable {
    private static final String LOG_WAITING_FOR_IMAGE_LOADED = "Image already is loading. Waiting for finishing. [%s]";
    private static final String LOG_CACHE_IMAGE_IN_MEMORY = "Cache image in memory [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED = "ViewContainer was collected by GC. Task is cancelled. [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_REUSED = "ImageAware is reused for another image. Task is cancelled. [%s]";
    private static final String LOG_TASK_CANCELLED_THREAD_INTERRUPTED = "Thread was Interrupted. Task is cancelled. [%s]";
    private static final String LOG_LOAD_IMAGE_FAIL = "Load image fail [%s]";

    private ImageDownloader mDownloader;
    private LruCache<String, Bitmap> mMemoryCache;
    private Handler mHandler;
    private ImageLoadingInfo mImageLoadingInfo;
    private ImageLoaderEngine mImageLoaderEngine;

    public DownloadAndDisplayImageTask(Handler handler, ImageLoadingInfo imageLoadingInfo) {
        this.mHandler = handler;
        this.mImageLoadingInfo = imageLoadingInfo;
        this.mImageLoaderEngine = mImageLoadingInfo.getImageLoaderEngine();
        this.mDownloader = imageLoadingInfo.getDownloader();
        this.mMemoryCache = imageLoadingInfo.getMemoryCache();
    }

    @Override
    public void run() {
        //判断是否需要终止任务
        if (isTaskShouldCancel()) return;

        //在下载某一 url 的时候，如果再次请求同一 url 设置等待状态，下载成功直接取消继续执行，失败则继续执行
        ReentrantLock urlLock = mImageLoadingInfo.getReentrantLock();
        if (urlLock.isLocked()) {
            L.i(String.format(LOG_WAITING_FOR_IMAGE_LOADED, mImageLoadingInfo.getImageUrl()));
        }
        urlLock.lock();
        try {
            checkTaskShouldCancel();
            Bitmap bitmap = mMemoryCache.get(mImageLoadingInfo.getImageUrl());
            if (bitmap == null) {
                bitmap = mDownloader.getBitmapFromNetwork(mImageLoadingInfo.getImageUrl());
                if (bitmap != null) {
                    L.i(String.format(LOG_CACHE_IMAGE_IN_MEMORY, mImageLoadingInfo.getImageUrl()));
                    mMemoryCache.put(mImageLoadingInfo.getImageUrl(), bitmap);

                    L.d(String.format("image size:%.2f", (float) bitmap.getRowBytes() * bitmap.getHeight() / 1024 / 1024));
                    L.d(String.format("image size used size:%.2f", (float) mMemoryCache.size() / 1024 / 1024));
                    L.d(String.format("image size max size:%.2f", (float) mMemoryCache.maxSize() / 1024 / 1024));
                }
            }
            checkTaskShouldCancel();
            if (bitmap != null) {
                DisplayImageTask displayImageTask = new DisplayImageTask(mImageLoadingInfo, bitmap);
                //主线程中才能更新UI
                mHandler.post(displayImageTask);
            } else {
                L.i(String.format(LOG_LOAD_IMAGE_FAIL, mImageLoadingInfo.getImageUrl()));
            }
        } catch (IOException e) {
            L.e(e);
        } catch (TaskCancelException e) {
            L.e(e);
        } catch (Throwable e) {
            L.e(e);
        } finally {
            urlLock.unlock();
        }
    }

    /**
     * 判断任务是否需要终止
     */
    private void checkTaskShouldCancel() throws TaskCancelException {
        checkViewCollected();
        checkViewReused();
        checkTaskInterrupted();
    }

    /**
     * 判断任务是否需要终止
     *
     * @return 是否需要终止任务
     */
    private boolean isTaskShouldCancel() {
        return isViewReused() || isViewCollected() || isTaskInterrupted();
    }

    /**
     * 判断view是否被回收，被回收抛出异常
     *
     * @throws TaskCancelException
     */
    private void checkViewCollected() throws TaskCancelException {
        if (isViewCollected()) {
            throw new TaskCancelException();
        }
    }

    /**
     * 判断view是否被回收
     *
     * @return 是否被回收
     */
    private boolean isViewCollected() {
        if (mImageLoadingInfo.getPicView().getView() == null) {
            L.i(String.format(LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED, mImageLoadingInfo.getImageUrl()));
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断view是否被再次使用，被再次使用抛出异常
     *
     * @throws TaskCancelException
     */
    private void checkViewReused() throws TaskCancelException {
        if (isViewReused()) {
            throw new TaskCancelException();
        }
    }

    /**
     * 判断view是否被再次使用
     *
     * @return 是否被回收
     */
    private boolean isViewReused() {
        //当前 view 控件正在加载的 url，如果加载成功则为 null，正在加载或加载失败则为绑定的 url，url不同则终止当前进程，url 相同则等待完成再继续执行
        String bindUrl = mImageLoaderEngine.getTaskUrl(mImageLoadingInfo.getPicView());
        if (!mImageLoadingInfo.getImageUrl().equals(bindUrl)) {
            L.i(String.format(LOG_TASK_CANCELLED_IMAGEAWARE_REUSED, mImageLoadingInfo.getImageUrl()));
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断线程是否终止，终止抛出异常
     *
     * @throws TaskCancelException
     */
    private void checkTaskInterrupted() throws TaskCancelException {
        if (isTaskInterrupted()) {
            throw new TaskCancelException();
        }
    }

    /**
     * 判断线程是否终止
     *
     * @return 线程是否终止
     */
    private boolean isTaskInterrupted() {
        if (Thread.interrupted()) {
            L.i(String.format(LOG_TASK_CANCELLED_THREAD_INTERRUPTED, mImageLoadingInfo.getImageUrl()));
            return true;
        } else {
            return false;
        }
    }

    class TaskCancelException extends Exception {

    }
}
