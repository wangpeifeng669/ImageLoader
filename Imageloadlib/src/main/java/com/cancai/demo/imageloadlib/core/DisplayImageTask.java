package com.cancai.demo.imageloadlib.core;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import com.cancai.demo.imageloadlib.utils.L;

/**
 * 显示图片的线程
 *
 * @author peter_wang
 * @create-time 15/7/3 11:47
 */
public class DisplayImageTask implements Runnable {
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED = "ViewContainer was collected by GC. Task is cancelled. [%s]";
    private static final String WARNING_CANT_SET_BITMAP = "Can't set a bitmap into view. You should call ImageLoader on UI thread.";

    private ImageLoadingInfo mImageLoadingInfo;
    private Bitmap mBitmap;

    public DisplayImageTask(ImageLoadingInfo imageLoadingInfo, Bitmap bitmap) {
        this.mImageLoadingInfo = imageLoadingInfo;
        this.mBitmap = bitmap;
    }

    @Override
    public void run() {
        //主线程中才能更新UI
        if (Looper.myLooper() == Looper.getMainLooper()) {
            View picView = mImageLoadingInfo.getPicView().getView();
            if (picView != null) {
                if (picView instanceof ImageView) {
                    ((ImageView) picView).setImageBitmap(mBitmap);
                } else {
                    picView.setBackgroundDrawable(new BitmapDrawable(mBitmap));
                }

                //图片显示成功，清除对应控件和图片url的绑定，并发中的其他相同url取消继续访问，在DownloadAndDisplayImageTask中的run中的checkViewReused会抛出终止操作的异常
                mImageLoadingInfo.getImageLoaderEngine().successDisplayTask(mImageLoadingInfo.getPicView());
            }
            else{
                L.i(String.format(LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED, mImageLoadingInfo.getImageUrl()));
            }
        } else {
            L.w(WARNING_CANT_SET_BITMAP);
        }
    }
}
