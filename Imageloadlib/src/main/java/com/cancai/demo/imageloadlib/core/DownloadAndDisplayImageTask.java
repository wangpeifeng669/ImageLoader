package com.cancai.demo.imageloadlib.core;

import android.graphics.Bitmap;
import android.os.Handler;

import com.cancai.demo.imageloadlib.utils.L;

import java.io.IOException;

/**
 * 下载并显示图片
 *
 * @author peter_wang
 * @create-time 15/7/6 10:21
 */
public class DownloadAndDisplayImageTask implements Runnable {
    private ImageDownloader mDownloader;
    private Handler mHandler;
    private ImageLoadingInfo mImageLoadingInfo;

    public DownloadAndDisplayImageTask(ImageDownloader downloader,Handler handler, ImageLoadingInfo imageLoadingInfo) {
        this.mDownloader = downloader;
        this.mHandler = handler;
        this.mImageLoadingInfo = imageLoadingInfo;
    }

    @Override
    public void run() {
        try {
            Bitmap bitmap = mDownloader.getBitmapFromNetwork(mImageLoadingInfo.getImageUrl());
            DisplayImageTask displayImageTask = new DisplayImageTask(mImageLoadingInfo, bitmap);
            mHandler.post(displayImageTask);
        } catch (IOException e) {
            L.e(e);
        }
        catch (Throwable e) {
            L.e(e);
        }
    }
}
