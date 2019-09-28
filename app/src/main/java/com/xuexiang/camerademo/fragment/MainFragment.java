package com.xuexiang.camerademo.fragment;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import com.xuexiang.camerademo.activity.CameraActivity;
import com.xuexiang.camerademo.activity.CameraViewActivity;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageContainerListFragment;
import com.xuexiang.xpage.base.XPageSimpleListFragment;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xutil.app.ActivityUtils;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.xuexiang.camerademo.activity.CameraActivity.KEY_IMG_PATH;
import static com.xuexiang.xaop.consts.PermissionConsts.CAMERA;
import static com.xuexiang.xaop.consts.PermissionConsts.STORAGE;

/**
 * @author xuexiang
 * @since 2018/11/7 下午1:16
 */
@Page(name = "Camera开发Demo")
public class MainFragment extends XPageSimpleListFragment {

    private static final int REQUEST_CODE_CUSTOM_CAMERA = 1000;

    /**
     * 初始化例子
     *
     * @param lists
     * @return
     */
    @Override
    protected List<String> initSimpleData(List<String> lists) {
        lists.add("Camera1 自定义照相机");
        lists.add("Camera2 自定义照相机");
        lists.add("Google CameraView（Activity）");
        lists.add("Google CameraView（Fragment）");
        return lists;
    }

    /**
     * 条目点击
     *
     * @param position
     */
    @Override
    @Permission({CAMERA, STORAGE})
    protected void onItemClick(int position) {
        switch (position) {
            case 0:
                startActivityForResult(new Intent(getContext(), CameraActivity.class), REQUEST_CODE_CUSTOM_CAMERA);
                break;
            case 1:

                break;
            case 2:
                startActivityForResult(new Intent(getContext(), CameraViewActivity.class), REQUEST_CODE_CUSTOM_CAMERA);
                break;
            case 3:
                openPageForResult(CameraViewFragment.class, null, REQUEST_CODE_CUSTOM_CAMERA);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CUSTOM_CAMERA) {
                ToastUtils.toast("图片:" + data.getStringExtra(KEY_IMG_PATH));
            }
        }
    }

    @Override
    protected TitleBar initTitleBar() {
        return super.initTitleBar().setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickUtils.exitBy2Click();
            }
        });
    }


    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ClickUtils.exitBy2Click();
        }
        return true;
    }


}
