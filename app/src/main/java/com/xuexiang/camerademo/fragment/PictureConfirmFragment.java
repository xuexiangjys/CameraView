package com.xuexiang.camerademo.fragment;

import android.support.v7.widget.AppCompatImageView;
import android.view.KeyEvent;
import android.view.View;

import com.xuexiang.camerademo.R;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xpage.base.XPageFragment;
import com.xuexiang.xpage.core.PageOption;
import com.xuexiang.xpage.utils.TitleBar;
import com.xuexiang.xrouter.annotation.AutoWired;
import com.xuexiang.xrouter.launcher.XRouter;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.file.FileUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xuexiang.camerademo.activity.PictureConfirmActivity.KEY_PICTURE_PATH;

/**
 * @author xuexiang
 * @since 2019-09-28 23:39
 */
@Page(params = {KEY_PICTURE_PATH})
public class PictureConfirmFragment extends XPageFragment {

    @BindView(R.id.iv_preview)
    AppCompatImageView ivPreview;

    @AutoWired(name = KEY_PICTURE_PATH)
    String mImgPath;

    public static void open(XPageFragment fragment, String imgPath) {
        PageOption.to(PictureConfirmFragment.class)
                .putString(KEY_PICTURE_PATH, imgPath)
                .setNewActivity(true)
                .open(fragment);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_picture_confirm;
    }

    @Override
    protected TitleBar initTitleBar() {
        return null;
    }

    @Override
    protected void initArgs() {
        XRouter.getInstance().inject(this);
    }

    @Override
    protected void initViews() {
        if (StringUtils.isEmpty(mImgPath)) {
            popToBack();
        }

        ivPreview.setImageURI(PathUtils.getUriForFile(FileUtils.getFileByPath(mImgPath)));
    }

    @Override
    protected void initListeners() {

    }

    @SingleClick
    @OnClick({R.id.btn_cancel, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                FileUtils.deleteFile(mImgPath);
                popToBack();
                break;
            case R.id.btn_submit:
                ToastUtils.toast("图片:" + mImgPath);
                popToBack();
                break;
            default:
                break;
        }
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            FileUtils.deleteFile(mImgPath);
            popToBack();
        }
        return true;
    }

}
