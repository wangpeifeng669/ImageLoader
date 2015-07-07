package com.cancai.demo.imageloadlib.core;

import android.graphics.Bitmap;

/**
 * 显示图片的线程
 *
 * @author peter_wang
 * @create-time 15/7/3 11:47
 */
public class DisplayImageTask implements Runnable{

    private ImageLoadingInfo mImageLoadingInfo;
    private Bitmap mBitmap;

    public DisplayImageTask(ImageLoadingInfo imageLoadingInfo,Bitmap bitmap){
        this.mImageLoadingInfo = imageLoadingInfo;
        this.mBitmap = bitmap;
    }

    @Override
    public void run() {
        mImageLoadingInfo.getIvPic().setImageBitmap(mBitmap);
    }
}
