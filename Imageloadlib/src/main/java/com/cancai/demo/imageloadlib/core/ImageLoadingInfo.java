package com.cancai.demo.imageloadlib.core;

import android.widget.ImageView;

/**
 * 图片加载信息
 *
 * @author peter_wang
 * @create-time 15/7/3 13:45
 */
public class ImageLoadingInfo {
    private ImageView mIvPic;
    private ImageLoaderEngine mImageLoaderEngine;
    private String mImageUrl;

    public ImageView getIvPic() {
        return mIvPic;
    }

    public void setIvPic(ImageView ivPic) {
        mIvPic = ivPic;
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
}
