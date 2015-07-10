package com.cancai.demo.imageloadlib.core.viewcontainer;

import android.view.View;

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
}  
