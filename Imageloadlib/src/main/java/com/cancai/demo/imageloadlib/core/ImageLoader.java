package com.cancai.demo.imageloadlib.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.cancai.demo.imageloadlib.cache.disk.DiskCacheInterface;
import com.cancai.demo.imageloadlib.core.viewcontainer.ImageViewContainer;
import com.cancai.demo.imageloadlib.core.viewcontainer.ViewContainer;
import com.cancai.demo.imageloadlib.utils.L;
import com.cancai.demo.imageloadlib.utils.StorageUtil;

/**
 * 图片加载工具类
 *
 * @author peter_wang
 * @create-time 15/7/2 13:56
 */
public class ImageLoader {
    private static final String ERROR_CONTEXT_CANNOT_NULL = "context when ImageLoader init cannot be null";
    private static final String ERROR_IMAGELOADER_CONFIG_CANNOT_NULL = "ImageLoaderGlobalConfig when ImageLoader init cannot be null";
    private static final String ERROR_IMAGELOADER_MUST_INIT = "ImageLoader init must init";
    private static final String WARNING_IMAGELOADER_HAS_BEEN_INIT = "ImageLoader init has been called";
    private static final String LOG_LOAD_IMAGE_FROM_MEMORY_CACHE = "Load image from memory cache [%s]";

    private ImageLoaderGlobalConfig mImageLoaderGlobalConfig;

    private ImageLoaderEngine mEngine = new ImageLoaderEngine();
    private ImageDownloader mDownloader;

    /**
     * 缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存
     */
    private LruCache<String, Bitmap> mMemoryCache;
    private DiskCacheInterface mDiskLruCache;

    private Context mApplicationContext;

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
    public void init(Context context, ImageLoaderGlobalConfig imageLoaderGlobalConfig) {
        if (context == null) {
            throw new IllegalArgumentException(ERROR_CONTEXT_CANNOT_NULL);
        }
        if (imageLoaderGlobalConfig == null) {
            throw new IllegalArgumentException(ERROR_IMAGELOADER_CONFIG_CANNOT_NULL);
        }

        if (mDownloader == null) {
            mApplicationContext = context.getApplicationContext();

            mImageLoaderGlobalConfig = imageLoaderGlobalConfig;

            mDownloader = new ImageDownloader(context.getApplicationContext());
            initMemoryCache();
            initDiskCache();
        } else {
            L.w(WARNING_IMAGELOADER_HAS_BEEN_INIT);
        }
    }

    private void initMemoryCache() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int mCacheSize = maxMemory / 8;
        //给LruCache分配1/8的 app 最大内存
        mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {

            //必须重写此方法，来测量Bitmap的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }

        };
    }

    private void initDiskCache() {
        new InitDiskCacheTask().execute();
    }

    class InitDiskCacheTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            mDiskLruCache = StorageUtil.createDiskCache(mApplicationContext);
            return null;
        }
    }

    public void displayImage(ImageView iv, String url) {
        displayImage(iv, url, mImageLoaderGlobalConfig.getDefaultDisplayImageConfig());
    }

    public void displayImage(ImageView iv, String url, DisplayImageConfig displayImageConfig) {
        if (mDownloader == null || mMemoryCache == null || mImageLoaderGlobalConfig == null) {
            throw new RuntimeException(ERROR_IMAGELOADER_MUST_INIT);
        }

        ImageViewContainer viewContainer = new ImageViewContainer(iv);
        displayImage(viewContainer, url, displayImageConfig);
    }

    private void displayImage(ViewContainer viewContainer, String url, DisplayImageConfig displayImageConfig) {
        mEngine.prepareDisplayTask(viewContainer, url);

        Bitmap bitmap = mMemoryCache.get(url);
        ImageLoadingInfo imageLoadingInfo = new ImageLoadingInfo();
        imageLoadingInfo.setImageLoaderEngine(mEngine);
        imageLoadingInfo.setPicView(viewContainer);
        imageLoadingInfo.setImageUrl(url);
        imageLoadingInfo.setDownloader(mDownloader);
        imageLoadingInfo.setMemoryCache(mMemoryCache);
        imageLoadingInfo.setCacheOnDisk(displayImageConfig.isCacheOnDisk());
        imageLoadingInfo.setDiskLruCache(mDiskLruCache);
        imageLoadingInfo.setReentrantLock(mEngine.getUrlLock(url));
        imageLoadingInfo.setImageLoaderGlobalConfig(mImageLoaderGlobalConfig);

        if (bitmap != null) {
            L.i(String.format(LOG_LOAD_IMAGE_FROM_MEMORY_CACHE, url));

            DisplayImageTask displayImageTask = new DisplayImageTask(imageLoadingInfo, bitmap);
            displayImageTask.run();
        } else {
            DownloadAndDisplayImageTask downloadAndDisplayImageTask = new DownloadAndDisplayImageTask(new Handler(), imageLoadingInfo);
            mEngine.submit(downloadAndDisplayImageTask);
        }
    }
}  
