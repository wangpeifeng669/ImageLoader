package com.cancai.demo.imageloadlib.core.assist;

import android.widget.ImageView;

/**
 * 图片显示类别
 *
 * @author peter_wang
 * @create-time 15/7/13 11:20
 */
public enum ViewScaleType {
    /**
     * 完整显示图片，原始图片最大边和图片控件大小一致
     */
    FIT_INSIDE,
    /**
     * 切割显示图片，只保留最短边跟图片控件大小一致
     */
    CROP;

    /**
     * 获取图片显示方式
     *
     * @param imageView {@link ImageView}
     * @return {@link #FIT_INSIDE} for
     *         <ul>
     *         <li>{@link ImageView.ScaleType#FIT_CENTER}</li>
     *         <li>{@link ImageView.ScaleType#FIT_XY}</li>
     *         <li>{@link ImageView.ScaleType#FIT_START}</li>
     *         <li>{@link ImageView.ScaleType#FIT_END}</li>
     *         <li>{@link ImageView.ScaleType#CENTER_INSIDE}</li>
     *         </ul>
     *         {@link #CROP} for
     *         <ul>
     *         <li>{@link ImageView.ScaleType#CENTER}</li>
     *         <li>{@link ImageView.ScaleType#CENTER_CROP}</li>
     *         <li>{@link ImageView.ScaleType#MATRIX}</li>
     *         </ul>
     */
    public static ViewScaleType fromImageView(ImageView imageView) {
        switch (imageView.getScaleType()) {
            case FIT_CENTER:
            case FIT_XY:
            case FIT_START:
            case FIT_END:
            case CENTER_INSIDE:
                return FIT_INSIDE;
            case MATRIX:
            case CENTER:
            case CENTER_CROP:
            default:
                return CROP;
        }
    }
}  
