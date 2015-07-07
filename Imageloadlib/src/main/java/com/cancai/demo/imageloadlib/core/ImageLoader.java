package com.cancai.demo.imageloadlib.core;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

import com.cancai.demo.imageloadlib.utils.L;

/**
 * @author peter_wang
 * @create-time 15/7/2 13:56
 */
public class ImageLoader {
    private static final String ERROR_CONTEXT_CANNOT_NULL = "context when ImageLoader init cannot be null";
    private static final String ERROR_IMAGELOADER_MUST_INIT = "ImageLoader init must init";
    private static final String WARNING_IMAGELOADER_HAS_BEEN_INIT = "ImageLoader init has been called";

    private ImageLoaderEngine mEngine = new ImageLoaderEngine();
    private ImageDownloader mDownloader;

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例没有绑定关系，
     * 而且只有被调用到才会装载，从而实现了延迟加载
     */
    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static ImageLoader instance = new ImageLoader();
    }

    /**
     * 私有化构造方法
     */
    private ImageLoader() {
    }

    public static ImageLoader getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 初始化 ImageLoader，只能调用一次
     *
     * @param context 上下文
     */
    public void init(Context context) {
        if (context == null) {
            throw new IllegalArgumentException(ERROR_CONTEXT_CANNOT_NULL);
        }

        if (mDownloader == null) {
            mDownloader = new ImageDownloader(context.getApplicationContext());
        } else {
            L.w(WARNING_IMAGELOADER_HAS_BEEN_INIT);
        }
    }

    public void displayImage(ImageView iv, String url) {
        if (mDownloader == null) {
            throw new RuntimeException(ERROR_IMAGELOADER_MUST_INIT);
        }

        ImageLoadingInfo imageLoadingInfo = new ImageLoadingInfo();
        imageLoadingInfo.setImageLoaderEngine(mEngine);
        imageLoadingInfo.setIvPic(iv);
        imageLoadingInfo.setImageUrl(url);
        DownloadAndDisplayImageTask downloadAndDisplayImageTask = new DownloadAndDisplayImageTask(mDownloader, new Handler(), imageLoadingInfo);
        mEngine.submit(downloadAndDisplayImageTask);
    }
}  
