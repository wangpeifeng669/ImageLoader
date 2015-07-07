package com.cancai.demo.imageloadlib.core;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 图片下载展示线程池
 *
 * @author peter_wang
 * @create-time 15/7/2 14:08
 */
class ImageLoaderEngine {
    private Executor mExecutor;

    public ImageLoaderEngine() {
        mExecutor = Executors.newFixedThreadPool(3);
    }

    public void submit(DownloadAndDisplayImageTask downloadAndDisplayImageTask) {
        mExecutor.execute(downloadAndDisplayImageTask);
    }
}  
