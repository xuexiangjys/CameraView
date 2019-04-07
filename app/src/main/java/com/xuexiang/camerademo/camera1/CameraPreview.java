package com.xuexiang.camerademo.camera1;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.xuexiang.camerademo.util.CameraUtils;

/**
 * 相机预览界面
 *
 * @author XUE
 * @since 2019/4/4 17:49
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private Camera mCamera;
    private SurfaceHolder mHolder;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.setKeepScreenOn(true);
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera == null) return;
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (holder.getSurface() == null) {
            return;
        }
        if (mCamera == null) return;

        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //重新打开相机
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera == null) return;

        mCamera.stopPreview();
        mCamera.release();
    }

    public boolean startPreview() {
        if (mCamera != null) {
            mCamera.startPreview();
            return true;
        }
        return false;
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


}
