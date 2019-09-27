package com.xuexiang.camerademo.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.xuexiang.camerademo.R;
import com.xuexiang.camerademo.camera1.CameraPreview;
import com.xuexiang.camerademo.util.CameraUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xuexiang.camerademo.activity.PictureConfirmActivity.REQUEST_CODE_PICTURE_CONFIRM;
import static com.xuexiang.camerademo.util.CameraUtils.JPEG;

/**
 * @author xuexiang
 * @since 2019/4/7 下午10:08
 */
public class CameraActivity extends AppCompatActivity {

    public static final String KEY_IMG_PATH = "img_path";

    @BindView(R.id.fl_container)
    FrameLayout mFlContainer;

    private CameraPreview mCameraPreview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        initCamera();
    }

    private void initCamera() {
        if (mCameraPreview == null) {
            mCameraPreview = new CameraPreview(this);
            mFlContainer.addView(mCameraPreview);
        } else {
            ToastUtils.toast("相机打开失败！");
        }
    }

    @Override
    protected void onResume() {
        if (mCameraPreview != null) {
            mCameraPreview.onResume();
        }
        super.onResume();
    }

    @SingleClick
    @OnClick({R.id.iv_camera_button, R.id.fl_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_camera_button:
                takePicture();
                break;
            case R.id.fl_container:
                if (mCameraPreview.isAutoFocusMode()) {
                    mCameraPreview.autoFocus();
                }
                break;
            default:
                break;
        }
    }


    private void takePicture() {
        if (!mCameraPreview.isCameraOpened()) {
            throw new IllegalStateException(
                    "Camera is not ready. Call start() before takePicture().");
        }
        if (mCameraPreview.isAutoFocusMode()) {
            mCameraPreview.cancelAutoFocus();
            mCameraPreview.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    mCameraPreview.takePicture(mPictureCallback);
                }
            });
        } else {
            mCameraPreview.takePicture(mPictureCallback);
        }
    }

    /**
     * 拍照回调
     */
    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            handlePictureTaken(data);
        }
    };

    private void handlePictureTaken(byte[] data) {
        String picPath = CameraUtils.handleOnPictureTaken(data, JPEG);
        if (!StringUtils.isEmpty(picPath)) {
            PictureConfirmActivity.open(this, picPath);
        } else {
            ToastUtils.toast("图片保存失败！");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICTURE_CONFIRM) {
            setResult(RESULT_OK, data);
            finish();
        }
    }

}
