package com.cancai.demo.imageloadlib.core.viewcontainer;

import android.widget.ImageView;

import com.cancai.demo.imageloadlib.core.assist.ViewScaleType;

/**
 * ImageView 容器类
 *
 * @author peter_wang
 * @create-time 15/7/8 10:13
 */
public class ImageViewContainer extends ViewContainer{
    public ImageViewContainer(ImageView ImageView) {
        super(ImageView);
    }

    @Override
    public ImageView getView() {
        return (ImageView) super.getView();
    }

    @Override
    public ViewScaleType getScaleType() {
        ImageView imageView = (ImageView) mViewReference.get();
        if (imageView != null) {
            return ViewScaleType.fromImageView(imageView);
        }
        return super.getScaleType();
    }
}  
