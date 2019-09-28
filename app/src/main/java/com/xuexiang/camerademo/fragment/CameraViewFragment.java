package com.xuexiang.camerademo.fragment;

import android.content.Intent;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.cameraview.AspectRatio;
import com.google.android.cameraview.CameraView;
import com.xuexiang.camerademo.R;
import com.xuexiang.camerademo.cameraview.AspectRatioFragment;
import com.xuexiang.camerademo.util.CameraUtils;
import com.xuexiang.xaop.annotation.IOThread;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.xuexiang.camerademo.activity.PictureConfirmActivity.REQUEST_CODE_PICTURE_CONFIRM;
import static com.xuexiang.camerademo.util.CameraUtils.JPEG;

/**
 * @author xuexiang
 * @since 2019-09-28 23:12
 */
@Page
public class CameraViewFragment extends XPageFragment implements Toolbar.OnMenuItemClickListener, AspectRatioFragment.Listener {

    private static final String FRAGMENT_DIALOG = "dialog";

    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };
    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };
    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            R.string.flash_off,
            R.string.flash_on,
    };

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.camera)
    CameraView mCameraView;

    private int mCurrentFlash;

    @Override
    protected TitleBar initTitleBar() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_camera_view;
    }

    @Override
    protected void initViews() {
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    protected void initListeners() {
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
    }

    @SingleClick
    @OnClick({R.id.iv_camera_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_camera_button:
                if (mCameraView != null) {
                    mCameraView.takePicture();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 拍照的回调
     */
    private CameraView.Callback mCallback = new CameraView.Callback() {
        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(final CameraView cameraView, final byte[] data) {
            handlePictureTaken(data);
        }
    };

    @IOThread
    private void handlePictureTaken(byte[] data) {
        String picPath = CameraUtils.handleOnPictureTaken(data, JPEG);
        if (!StringUtils.isEmpty(picPath)) {
            PictureConfirmFragment.open(this, picPath);
        } else {
            ToastUtils.toast("图片保存失败！");
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aspect_ratio:
                FragmentManager fragmentManager = getChildFragmentManager();
                if (mCameraView != null && fragmentManager.findFragmentByTag(FRAGMENT_DIALOG) == null) {
                    final Set<AspectRatio> ratios = mCameraView.getSupportedAspectRatios();
                    final AspectRatio currentRatio = mCameraView.getAspectRatio();
                    AspectRatioFragment.newInstance(ratios, currentRatio, this).show(fragmentManager, FRAGMENT_DIALOG);
                }
                return true;
            case R.id.switch_flash:
                if (mCameraView != null) {
                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
                    item.setTitle(FLASH_TITLES[mCurrentFlash]);
                    item.setIcon(FLASH_ICONS[mCurrentFlash]);
                    mCameraView.setFlash(FLASH_OPTIONS[mCurrentFlash]);
                }
                return true;
            case R.id.switch_camera:
                if (Camera.getNumberOfCameras() > 1) {
                    if (mCameraView != null) {
                        int facing = mCameraView.getFacing();
                        mCameraView.setFacing(facing == CameraView.FACING_FRONT ? CameraView.FACING_BACK : CameraView.FACING_FRONT);
                    }
                } else {
                    ToastUtils.toast("当前设备不支持切换摄像头！");
                }
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAspectRatioSelected(@NonNull AspectRatio ratio) {
        if (mCameraView != null) {
            ToastUtils.toast(ratio.toString());
            mCameraView.setAspectRatio(ratio);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mCameraView.start();
    }

    @Override
    public void onPause() {
        mCameraView.stop();
        super.onPause();
    }

}
