package com.cancai.demo.imageloadlib.core.viewcontainer;

import android.view.View;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * view容器类
 *
 * @author peter_wang
 * @create-time 15/7/8 10:07
 */
public class ViewContainer implements ViewContainerInterface {
    private Reference<View> mViewReference;

    public ViewContainer(View view) {
        mViewReference = new WeakReference<>(view);
    }

    @Override
    public View getView() {
        return mViewReference.get();
    }

    @Override
    public int getId() {
        return mViewReference.get() == null ? hashCode() : mViewReference.get().hashCode();
    }
}
