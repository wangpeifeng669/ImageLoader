package com.cancai.demo.imageloadlib.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.view.View;

import com.cancai.demo.imageloadlib.cache.disk.DiskCacheInterface;
import com.cancai.demo.imageloadlib.core.viewcontainer.ViewContainer;
import com.cancai.demo.imageloadlib.utils.IoUtil;
import com.cancai.demo.imageloadlib.utils.L;

import java.io.IOException;
import java.io.InputStream;
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
    private static final String LOG_TASK_CANCELLED_VIEW_COLLECTED = "ViewContainer was collected by GC. Task is cancelled. [%s]";
    private static final String LOG_TASK_CANCELLED_VIEW_REUSED = "ImageAware is reused for another image. Task is cancelled. [%s]";
    private static final String LOG_TASK_CANCELLED_THREAD_INTERRUPTED = "Thread was Interrupted. Task is cancelled. [%s]";
    private static final String LOG_LOAD_IMAGE_FAIL = "Load image fail [%s]";
    private static final String LOG_SAVE_IMAGE_TO_DISK_FAIL = "Save image to disk fail [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_MEMORY_CACHE = "Load image from memory cache [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_DISK_CACHE = "Load image from disk cache [%s]";

    private ImageDownloader mDownloader;
    private LruCache<String, Bitmap> mMemoryCache;
    private boolean isCacheOnDisk;
    private DiskCacheInterface mDiskLruCache;
    private Handler mHandler;
    private ImageLoadingInfo mImageLoadingInfo;
    private String mImageUrl;
    private ImageLoaderEngine mImageLoaderEngine;
    private ImageLoaderGlobalConfig mImageLoaderGlobalConfig;

    public DownloadAndDisplayImageTask(Handler handler, ImageLoadingInfo imageLoadingInfo) {
        this.mHandler = handler;
        this.mImageLoadingInfo = imageLoadingInfo;
        this.mImageUrl = mImageLoadingInfo.getImageUrl();
        this.mImageLoaderEngine = mImageLoadingInfo.getImageLoaderEngine();
        this.mDownloader = imageLoadingInfo.getDownloader();
        this.mMemoryCache = imageLoadingInfo.getMemoryCache();
        this.isCacheOnDisk = imageLoadingInfo.isCacheOnDisk();
        this.mDiskLruCache = imageLoadingInfo.getDiskLruCache();
        this.mImageLoaderGlobalConfig = imageLoadingInfo.getImageLoaderGlobalConfig();
    }

    @Override
    public void run() {
        //判断是否需要终止任务
        if (isTaskShouldCancel()) return;

        //在下载某一 url 的时候，如果再次请求同一 url 设置等待状态，下载成功直接取消继续执行，失败则继续执行
        ReentrantLock urlLock = mImageLoadingInfo.getReentrantLock();
        if (urlLock.isLocked()) {
            L.i(String.format(LOG_WAITING_FOR_IMAGE_LOADED, mImageUrl));
        }
        urlLock.lock();
        InputStream bitmapStream = null;
        try {
            checkTaskShouldCancel();
            Bitmap bitmap = mMemoryCache.get(mImageUrl);
            if (bitmap == null) {
                StreamType streamType = StreamType.NET;
                bitmapStream = mDiskLruCache.getImageFromDisk(mImageUrl);
                if (bitmapStream == null) {
                    bitmapStream = mDownloader.getStreamFromNetwork(mImageUrl);

                    if (isCacheOnDisk && mDiskLruCache.saveImageToDisk(mImageUrl, bitmapStream)) {
                        L.w(String.format(LOG_SAVE_IMAGE_TO_DISK_FAIL, mImageUrl));
                    }
                } else {
                    streamType = StreamType.FILE;
                    L.i(String.format(LOG_LOAD_IMAGE_FROM_DISK_CACHE, mImageUrl));
                }

                //从磁盘或者网络获取到的图片流保存到内存中，方便下次直接使用
                if (bitmapStream != null) {
                    bitmap = optimizationBitmap(bitmapStream, mImageLoadingInfo.getPicView(), streamType);
//                    bitmap = decodeSampledBitmap(bitmapStream, 1);

                    L.i(String.format(LOG_CACHE_IMAGE_IN_MEMORY, mImageUrl));
                    mMemoryCache.put(mImageUrl, bitmap);

                    L.d(String.format("image size:%.2f", (float) bitmap.getRowBytes() * bitmap.getHeight() / 1024 / 1024));
                    L.d(String.format("image size used size:%.2f", (float) mMemoryCache.size() / 1024 / 1024));
                    L.d(String.format("image size max size:%.2f", (float) mMemoryCache.maxSize() / 1024 / 1024));
                }
            } else {
                L.i(String.format(LOG_LOAD_IMAGE_FROM_MEMORY_CACHE, mImageUrl));
            }
            checkTaskShouldCancel();
            if (bitmap != null) {
                DisplayImageTask displayImageTask = new DisplayImageTask(mImageLoadingInfo, bitmap);
                //主线程中才能更新UI
                mHandler.post(displayImageTask);
            } else {
                L.i(String.format(LOG_LOAD_IMAGE_FAIL, mImageUrl));
            }
        } catch (IOException e) {
            L.e(e);
        } catch (TaskCancelException e) {
            L.e(e);
        } catch (Throwable e) {
            L.e(e);
        } finally {
            IoUtil.closeStream(bitmapStream);
            urlLock.unlock();
        }
    }

    /**
     * 优化图片，只保存需要显示的部分
     *
     * @param bitmapStream 图片流
     * @param picView      图片显示控件
     * @param streamType   图片流类型，FILE文件流，NET网络流
     * @return 图片bitmap
     * @throws TaskCancelException
     * @throws IOException
     */
    private Bitmap optimizationBitmap(InputStream bitmapStream, ViewContainer picView, StreamType streamType) throws TaskCancelException, IOException {
        View view = picView.getView();
        if (view != null) {
            int srcWidth = view.getWidth();
            int srcHeight = view.getHeight();
            //如果是从磁盘加载图片，速度过快，导致整个 activity 页面未加载完成，则此时的 view 宽高都为0，此时取最大宽高
            if (srcHeight <= 0 || srcHeight <= 0) {
                srcWidth = mImageLoaderGlobalConfig.getMaxWidth();
                srcHeight = mImageLoaderGlobalConfig.getMaxHeight();
            }
            L.d("src width:" + srcWidth + ",src height:" + srcHeight);
            //测量流中图片长宽
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(bitmapStream, null, options);

            int sampleSize = 1;
            switch (picView.getScaleType()) {
                case FIT_INSIDE:
                    sampleSize = Math.max(options.outWidth / srcWidth, options.outHeight / srcHeight);
                    break;
                case CROP:
                    sampleSize = Math.min(options.outWidth / srcWidth, options.outHeight / srcHeight);
                    break;
            }

            bitmapStream = resetStream(bitmapStream, streamType);
            return decodeSampledBitmap(bitmapStream, sampleSize);
        } else {
            throw new TaskCancelException();
        }
    }

    /**
     * 流被读取后必须重置才能重新读取
     *
     * @param imageStream 图片流
     * @param streamType  图片流类型，FILE文件流，NET网络流
     * @return 图片流
     * @throws IOException
     */
    private InputStream resetStream(InputStream imageStream, StreamType streamType) throws IOException {
        try {
            imageStream.reset();
        } catch (IOException e) {
            IoUtil.closeStream(imageStream);
            if (streamType == StreamType.NET) {
                imageStream = mDownloader.getStreamFromNetwork(mImageUrl);
            } else if (streamType == StreamType.FILE) {
                imageStream = mDiskLruCache.getImageFromDisk(mImageUrl);
            }
        }
        return imageStream;
    }

    private Bitmap decodeSampledBitmap(InputStream bitmapStream, int sampleSize) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(bitmapStream, null, options);
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
            L.i(String.format(LOG_TASK_CANCELLED_VIEW_COLLECTED, mImageUrl));
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
        if (!mImageUrl.equals(bindUrl)) {
            L.i(String.format(LOG_TASK_CANCELLED_VIEW_REUSED, mImageUrl));
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

    private enum StreamType {
        NET, FILE
    }
}
