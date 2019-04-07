package com.xuexiang.camerademo.camera1;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;

import com.xuexiang.camerademo.util.CameraUtils;

import java.util.List;

/**
 * @author XUE
 * @since 2019/4/4 18:00
 */
public final class CameraManager {

    private static volatile CameraManager sInstance = null;

    private Camera mCamera;

    private CameraManager() {

    }

    /**
     * 获取单例
     *
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

    public Camera openCamera(Context context) {
        return openCamera(context, 3840 * 2160);
    }

    public Camera openCamera(Context context, long mMaxPicturePixels) {
        if (!CameraUtils.supportCameraHardware(context)) {
            return null;
        }

        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        //获取相机参数实例
        Camera.Parameters parameters = mCamera.getParameters();
        //设置相机角度
        CameraUtils.followScreenOrientation(context, mCamera);
        //设置输出图片类型
        parameters.setPictureFormat(ImageFormat.JPEG);

        //设置预览的尺寸
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size previewSize = CameraUtils.findBestSize(false, previewSizes, 1920 * 1080);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        //设置拍照图片的尺寸
        List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
        Camera.Size pictureSize = CameraUtils.findBestSize(true, pictureSizes, mMaxPicturePixels);
        parameters.setPreviewSize(pictureSize.width, pictureSize.height);

        //设置相机参数
        initFocusParams(parameters);
        return mCamera;
    }

    void initFocusParams(Camera.Parameters params) {
        //若支持连续对焦模式，则使用.
        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            setParameters(params);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            //进到这里，说明不支持连续对焦模式，退回到点击屏幕进行一次自动对焦.
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            setParameters(params);
        }
    }

    void setParameters(Camera.Parameters params) {
        try {
            mCamera.setParameters(params);
        } catch (Exception e) {
            //非常罕见的情况
            //个别机型在SupportPreviewSizes里汇报了支持某种预览尺寸，但实际是不支持的，设置进去就会抛出RuntimeException.
            e.printStackTrace();
            try {
                //遇到上面所说的情况，只能设置一个最小的预览尺寸
                params.setPreviewSize(1920, 1080);
                mCamera.setParameters(params);
            } catch (Exception e1) {
                //到这里还有问题，就是拍照尺寸的锅了，同样只能设置一个最小的拍照尺寸
                e1.printStackTrace();
                try {
                    params.setPictureSize(1920, 1080);
                    mCamera.setParameters(params);
                } catch (Exception ignored) {
                }
            }
        }
    }

    public boolean autoFocus() {
        if (mCamera != null) {
            return CameraUtils.autoFocus(mCamera);
        }
        return false;
    }

    public boolean autoFocus(Camera.AutoFocusCallback cb) {
        if (mCamera != null) {
            return CameraUtils.autoFocus(mCamera, cb);
        }
        return false;
    }

    public boolean takePicture(Camera.PictureCallback callback) {
        if (mCamera != null) {
            mCamera.takePicture(null, null, callback);
            return true;
        }
        return false;
    }


    /**
     * 释放相机资源
     */
    public void closeCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    public Camera getCamera() {
        return mCamera;
    }
}
