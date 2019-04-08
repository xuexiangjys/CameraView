package com.xuexiang.camerademo.strategy;

import com.google.android.cameraview.Size;
import com.google.android.cameraview.strategy.ICameraStrategy;

import java.util.SortedSet;

/**
 * 自适应
 *
 * @author XUE
 * @since 2019/4/8 12:32
 */
public class AutoCameraStrategy implements ICameraStrategy {

    private long mTargetPicturePixels;

    public AutoCameraStrategy(long targetPicturePixels) {
        mTargetPicturePixels = targetPicturePixels;
    }

    /**
     * 找到最合适的尺寸(拍摄的照片）
     *
     * @param sizes
     * @return
     */
    @Override
    public Size chooseOptimalPictureSize(SortedSet<Size> sizes) {
        //从小到大排序
        for (Size size : sizes) {
            //尺寸不要超过指定的PicturePixels.
            if (((long) size.getWidth()) * ((long) size.getHeight()) >= mTargetPicturePixels) {
                return size;
            }
        }
        //找不到就选择最大的尺寸
        return sizes.last();
    }

}
