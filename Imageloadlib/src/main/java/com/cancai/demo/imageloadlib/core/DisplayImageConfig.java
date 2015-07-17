package com.cancai.demo.imageloadlib.core;

/**
 * 图片显示配置参数
 *
 * @author peter_wang
 * @create-time 15/7/15 15:50
 */
public class DisplayImageConfig {
    private boolean isCacheOnDisk;

    public boolean isCacheOnDisk() {
        return isCacheOnDisk;
    }

    public void setCacheOnDisk(boolean isCacheOnDisk) {
        this.isCacheOnDisk = isCacheOnDisk;
    }

    public static DisplayImageConfig getDefaultConfig() {
        DisplayImageConfig displayImageConfig = new DisplayImageConfig();
        displayImageConfig.setCacheOnDisk(false);
        return displayImageConfig;
    }
}
