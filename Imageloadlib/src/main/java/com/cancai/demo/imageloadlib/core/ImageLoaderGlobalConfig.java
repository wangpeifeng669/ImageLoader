package com.cancai.demo.imageloadlib.core;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 设置固定的图片显示配置参数，与{@link DisplayImageConfig}不同，这个是固定不变的配置信息，而{@link DisplayImageConfig}是可以修改的配置信息
 *
 * @author peter_wang
 * @create-time 15/7/15 15:29
 */
public class ImageLoaderGlobalConfig {
    private DisplayImageConfig mDefaultDisplayImageConfig;

    /**
     * 图片显示的最大宽度，在获取图片控件大小小于等于0时使用
     */
    private int mMaxWidth;
    /**
     * 图片显示的最大高度，在获取图片控件大小小于等于0时使用
     */
    private int mMaxHeight;

    public ImageLoaderGlobalConfig(Context context) {
        this.mDefaultDisplayImageConfig = DisplayImageConfig.getDefaultConfig();

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        this.mMaxWidth = displayMetrics.widthPixels;
        this.mMaxHeight = displayMetrics.heightPixels;
    }

    public DisplayImageConfig getDefaultDisplayImageConfig() {
        return mDefaultDisplayImageConfig;
    }

    public void setDefaultDisplayImageConfig(DisplayImageConfig defaultDisplayImageConfig) {
        mDefaultDisplayImageConfig = defaultDisplayImageConfig;
    }

    public int getMaxWidth() {
        return mMaxWidth;
    }

    public int getMaxHeight() {
        return mMaxHeight;
    }
}
