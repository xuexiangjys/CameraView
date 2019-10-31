package com.google.android.cameraview;

import com.google.android.cameraview.strategy.ICameraStrategy;
import com.google.android.cameraview.strategy.impl.DefaultCameraStrategy;

/**
 * @author XUE
 * @since 2019/4/8 11:51
 */
class _CameraView {

    /**
     * 默认的相机策略
     */
    private static ICameraStrategy sICameraStrategy = new DefaultCameraStrategy();

    private static AspectRatio sDefaultAspectRatio = AspectRatio.of(16, 9);

    public static void setICameraStrategy(ICameraStrategy sICameraStrategy) {
        _CameraView.sICameraStrategy = sICameraStrategy;
    }

    public static ICameraStrategy getICameraStrategy() {
        return sICameraStrategy;
    }

    public static void setDefaultAspectRatio(AspectRatio aspectRatio) {
        _CameraView.sDefaultAspectRatio = aspectRatio;
    }

    public static AspectRatio getDefaultAspectRatio() {
        return sDefaultAspectRatio;
    }
}
