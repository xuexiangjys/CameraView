package com.google.android.cameraview.strategy;

import com.google.android.cameraview.Size;

import java.util.SortedSet;

/**
 * 照相机策略
 *
 * @author XUE
 * @since 2019/4/8 11:30
 */
public interface ICameraStrategy {

    /**
     * 找到最合适的尺寸(拍摄的照片）
     * @param sizes 尺寸集合，从小到大排序
     * @return
     */
    Size chooseOptimalPictureSize(SortedSet<Size> sizes);

}
