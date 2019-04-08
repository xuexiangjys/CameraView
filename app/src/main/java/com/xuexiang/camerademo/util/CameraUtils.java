package com.xuexiang.camerademo.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;

import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.data.DateUtils;
import com.xuexiang.xutil.file.FileIOUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author xuexiang
 * @since 2019/4/7 下午8:51
 */
public final class CameraUtils {

    private CameraUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断是否是16：9的Size, 允许误差5%.
     *
     * @param size Size
     * @return 是否是16：9的Size
     */
    public static boolean isWide(Camera.Size size) {
        double ratio = ((double) size.width) / ((double) size.height);
        return ratio > 1.68 && ratio < 1.87;
    }

    /**
     * 从sizeArray中找到满足16:9比例，且不超过maxPicturePixels指定的像素数的最大Size.
     * 若找不到，则选择满足16:9比例的最大Size（像素数可能超过maxPicturePixels)，若仍找不到，返回最大Size。
     *  @param forTakingPicture 寻找拍照尺寸，而不是预览尺寸。
     *                         为true时，尺寸受到maxPicturePixels的限制；
     *                         false时，尺寸不超过1920x1080，否则相机带宽吃紧，这也是Camera2 API的要求.
     * @param sizeList         Camera.Parameters.getSupportedPreviewSizes()
     *                         或Camera.Parameters.getSupportedPictureSizes()返回的sizeList
     * @param maxPicturePixels 最大可接受的照片像素数
     */
    public static Camera.Size findBestSize(boolean forTakingPicture, List<Camera.Size> sizeList, long maxPicturePixels) {
        //满足16:9，但超过maxAcceptedPixels的过大Size
        List<Camera.Size> tooLargeSizes = new ArrayList<>();
        //按面积由大到小排序
        Collections.sort(sizeList, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size o1, Camera.Size o2) {
                return -Integer.compare(o1.width * o1.height, o2.width * o2.height);
            }
        });
        for (Camera.Size size: sizeList) {
            //非16:9的尺寸无视
            if (isWide(size)) {
                boolean notTooLarge;
                if (forTakingPicture) {
                    //若是为了拍摄照片，则尺寸不要超过指定的maxPicturePixels.
                    notTooLarge = ((long) size.width) * ((long) size.height) <= maxPicturePixels;
                } else {
                    //若只是为了预览，则尺寸不要超过1920x1080，否则相机带宽吃紧，这也是Camera2 API的要求.
                    notTooLarge = ((long) size.width) * ((long) size.height) <= 1920 * 1080;
                }
                if (!notTooLarge) {
                    tooLargeSizes.add(size);
                }
            }
        }
        if (tooLargeSizes.size() > 0) {
            return tooLargeSizes.get(0);
        } else {
            return sizeList.get(0);
        }
    }

    /**
     * 在FOCUS_MODE_AUTO模式下使用，触发一次自动对焦.
     *
     * @param camera Camera
     */
    public static boolean autoFocus(Camera camera) {
        try {
            camera.autoFocus(null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 在FOCUS_MODE_AUTO模式下使用，触发一次自动对焦.
     *
     * @param camera Camera
     */
    public static boolean autoFocus(Camera camera, Camera.AutoFocusCallback cb) {
        try {
            camera.autoFocus(cb);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isAutoFocusSupported(Camera.Parameters params) {
        List<String> modes = params.getSupportedFocusModes();
        return modes.contains(Camera.Parameters.FOCUS_MODE_AUTO);
    }

    /**
     * @return 判断相机是否支持
     */
    public static boolean supportCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    public static void followScreenOrientation(Context context, Camera camera){
        final int orientation = context.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            camera.setDisplayOrientation(0);
        }else if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            camera.setDisplayOrientation(90);
        }
    }

    public static final String JPEG = ".jpeg";

    /**
     * 处理拍照的回调
     * @param data
     * @return
     */
    public static String handleOnPictureTaken(byte[] data, String fileSuffix) {
        String picPath = PathUtils.getAppExtCachePath() + "/images/" + DateUtils.getNowMills() + fileSuffix;
        boolean result = FileIOUtils.writeFileFromBytesByStream(picPath, data);
        return result ? picPath : "";
    }

}
