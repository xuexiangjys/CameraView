package com.xuexiang.camerademo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.xuexiang.camerademo.R;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.file.FileUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xuexiang.camerademo.activity.CameraActivity.KEY_IMG_PATH;

/**
 * 图片确认页面
 *
 * @author xuexiang
 * @since 2019-09-27 16:25
 */
public class PictureConfirmActivity extends AppCompatActivity {

    public static final String KEY_PICTURE_PATH = "key_picture_path";
    public static final int REQUEST_CODE_PICTURE_CONFIRM = 1000;

    @BindView(R.id.iv_preview)
    AppCompatImageView ivPreview;

    private String mImgPath;

    public static void open(Activity activity, String imgPath) {
        Intent intent = new Intent(activity, PictureConfirmActivity.class);
        intent.putExtra(KEY_PICTURE_PATH, imgPath);
        activity.startActivityForResult(intent, REQUEST_CODE_PICTURE_CONFIRM);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_confirm);
        ButterKnife.bind(this);

        mImgPath = getIntent().getStringExtra(KEY_PICTURE_PATH);
        if (StringUtils.isEmpty(mImgPath)) {
            finish();
        }

        ivPreview.setImageURI(PathUtils.getUriForFile(FileUtils.getFileByPath(mImgPath)));
    }

    @SingleClick
    @OnClick({R.id.btn_cancel, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                FileUtils.deleteFile(mImgPath);
                finish();
                break;
            case R.id.btn_submit:
                setResult(RESULT_OK, new Intent().putExtra(KEY_IMG_PATH, mImgPath));
                finish();
                break;
            default:
                break;
        }
    }


}
