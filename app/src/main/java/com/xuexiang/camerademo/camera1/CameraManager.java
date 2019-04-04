package com.xuexiang.camerademo.camera1;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;

/**
 * @author XUE
 * @since 2019/4/4 18:00
 */
public final class CameraManager {

    private static volatile CameraManager sInstance = null;

    private Context mContext;

    private Camera mCamera;

    private CameraManager() {

    }

    /**
     * 获取单例
     * @return
     */
    public static CameraManager get() {
        if (sInstance == null) {
            synchronized (CameraManager.class) {
                if (sInstance == null) {
                    sInstance = new CameraManager();
                }
            }
        }
        return sInstance;
    }

    public static void init(Context context) {
        get().mContext = context.getApplicationContext();
    }

    /**
     * @return 判断相机是否支持
     */
    public static boolean supportCameraHardware() {
        return get().mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public boolean openCamera() {
        if (!supportCameraHardware()){
            return false;
        }
        mCamera = Camera.open();
        //获取相机参数实例
        Camera.Parameters parameters = mCamera.getParameters();
        //将相机顺时针旋转90度
        mCamera.setDisplayOrientation(90);
        //设置输出图片类型
        parameters.setPictureFormat(ImageFormat.JPEG);
        return true;
    }

}
