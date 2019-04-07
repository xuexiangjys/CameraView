package com.xuexiang.camerademo.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.xuexiang.camerademo.R;
import com.xuexiang.camerademo.camera1.CameraManager;
import com.xuexiang.camerademo.camera1.CameraPreview;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.data.DateUtils;
import com.xuexiang.xutil.file.FileIOUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author xuexiang
 * @since 2019/4/7 下午10:08
 */
public class CameraActivity extends AppCompatActivity {

    public static final String KEY_IMG_PATH = "img_path";

    @BindView(R.id.fl_container)
    FrameLayout mFlContainer;

    private CameraPreview mCameraPreview;

    private boolean isFocus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        initCamera();
    }

    private void initCamera() {
        final Camera camera = CameraManager.get().openCamera(this);
        if (camera != null && mCameraPreview == null) {
            mCameraPreview = new CameraPreview(this, camera);
            mFlContainer.addView(mCameraPreview);
        } else {
            ToastUtils.toast("相机打开失败！");
        }
    }

    @SingleClick
    @OnClick({R.id.iv_camera_button, R.id.fl_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_camera_button:
                if (isFocus){
                    mCameraPreview.takePicture(mPictureCallback);
                } else {
                    mCameraPreview.autoFocus(mAutoFocusCallback);
                }
                break;
            case R.id.fl_container:
                mCameraPreview.autoFocus();
                break;
            default:
                break;
        }
    }

    /**
     * 自动对焦回调
     */
    private Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            //对焦状态
            isFocus = success;
            if (success) {
                try {
                    camera.takePicture(null, null, mPictureCallback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 拍照回调
     */
    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            String picPath = PathUtils.getAppExtCachePath() + "/images/" + DateUtils.getNowMills() + ".jpeg";
            boolean result = FileIOUtils.writeFileFromBytesByStream(picPath, data);
            if (result) {
                setResult(RESULT_OK, new Intent().putExtra(KEY_IMG_PATH, picPath));
                finish();
            } else {
                mCameraPreview.startPreview();
            }
        }
    };
}
