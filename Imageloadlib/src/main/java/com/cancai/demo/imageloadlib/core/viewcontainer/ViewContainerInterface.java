package com.cancai.demo.imageloadlib.core.viewcontainer;

import android.view.View;

import com.cancai.demo.imageloadlib.core.assist.ViewScaleType;

/**
 * view 容器接口
 *
 * @author peter_wang
 * @create-time 15/7/8 09:51
 */
public interface ViewContainerInterface {
    /**
     * 返回对应保存的 view
     *
     * @return 保存的 view
     */
    public View getView();

    /**
     * 获取该下载的唯一标识码，代表此次下载
     *
     * @return 该下载的唯一标识码
     */
    public int getId();

    /**
     * 获取图片显示类别，{@link ViewScaleType#FIT_INSIDE}或{@link ViewScaleType#CROP}
     * @return 图片显示类别，{@link ViewScaleType#FIT_INSIDE}或{@link ViewScaleType#CROP}
     */
    public ViewScaleType getScaleType();
}  
