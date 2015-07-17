package com.cancai.demo.imageloaderdemo;

import android.app.Application;

import com.cancai.demo.imageloadlib.core.ImageLoader;
import com.cancai.demo.imageloadlib.core.ImageLoaderGlobalConfig;

/**
 * 全局 Application
 *
 * @author peter_wang
 * @create-time 15/7/6 14:27
 */
public class ImageLoadApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderGlobalConfig imageLoaderGlobalConfig = new ImageLoaderGlobalConfig(this);
        ImageLoader.getInstance().init(this, imageLoaderGlobalConfig);
    }
}
