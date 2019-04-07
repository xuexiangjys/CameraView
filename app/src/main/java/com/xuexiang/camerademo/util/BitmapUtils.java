package com.xuexiang.camerademo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.xuexiang.xutil.app.PathUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 *
 * @author xuexiang
 * @since 2019/4/7 下午11:31
 */
public class BitmapUtils {
    
    private BitmapUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 若图片宽小于高，则逆时针旋转90° ; 否则，返回原图片.
     * 适用于调用系统相机进行拍照，且希望图片总是横向的场景；
     * Bitmap占用内存较大，建议先压缩到一个分辨率级别，再进行旋转。
     *
     * @param sourceBitmap 拍照得到的Bitmap
     * @return 若图片宽小于高，返回逆时针旋转90°后的Bitmap ; 否则，返回原Bitmap.
     */
    public static Bitmap rotate(Bitmap sourceBitmap) {
        int sourceWidth = sourceBitmap.getWidth();
        int sourceHeight = sourceBitmap.getHeight();
        //若原图的宽大于高，不需要旋转，直接返回原图
        if (sourceWidth >= sourceHeight) return sourceBitmap;
        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceWidth, sourceHeight, matrix, true);
    }

    /**
     * 将图片文件压缩到所需的大小，返回位图对象.
     * 若原图尺寸小于需要压缩到的尺寸，则返回原图.
     * 该方法通过分辨率面积得到压缩比，因此不存在图片方向、宽高比的问题.
     *
     * @param filePath        File
     * @param reqSquarePixels 要压缩到的分辨率（面积）
     * @return 压缩后的Bitmap
     */
    public static Bitmap compressToResolution(File filePath, long reqSquarePixels) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath.toString(), options);
        double compressRatio = calculateCompressRatioBySquare(options, reqSquarePixels);
        //解析原图
        Bitmap fullBitmap = BitmapFactory.decodeFile(filePath.toString());
        //创建缩放的Bitmap。这里不用inSampleSize，inSampleSize基于向下采样，节省了Bitmap读入后占用的内存，但Bitmap本身的像素还是那么多，文件大小没有改变。
        return Bitmap.createScaledBitmap(fullBitmap, (int) (options.outWidth / compressRatio), (int) (options.outHeight / compressRatio), false);
    }

    /**
     * 计算压缩比.考虑了options的长宽比可能与req中的比例不同的情况.
     * 该方法通过分辨率面积得到压缩比，因此不存在图片方向、宽高比的问题.
     *
     * @param options         BitmapFactory.Options
     * @param reqSquarePixels 压缩后的分辨率面积
     * @return 计算得到的压缩比
     */
    public static double calculateCompressRatioBySquare(BitmapFactory.Options options, long reqSquarePixels) {
        //原图像素数
        long squarePixels = options.outWidth * options.outHeight;
        //若原图像素数少于需要压缩到的像素数，不压缩（压缩比返回1）
        if (squarePixels <= reqSquarePixels) return 1;
        //面积之比，即压缩比（长度之比）的平方
        double powwedScale = ((double) squarePixels) / ((double) reqSquarePixels);
        //开根号得压缩比
        return Math.sqrt(powwedScale);
    }

    /**
     * 将Bitmap写入文件，文件位于内置存储上该应用包名目录的cache子目录中.
     *
     * @param bitmap   要写入的Bitmap
     * @param fileName 文件名
     * @return 文件对应的File.
     */
    public static File writeBitmapToFile(Bitmap bitmap, String fileName) {
        //FileProvider中指定的目录
        File dir = new File(PathUtils.getAppExtCachePath(), "images");
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, fileName);
        FileOutputStream fos;
        try {
            if (file.exists()) file.delete();
            fos = new FileOutputStream(file);
            //JPEG为硬件加速，O(1)时间复杂度，而PNG为O(n)，速度要慢很多，WEBP不常用
            //90%的品质已高于超精细(85%)的标准，已非常精细
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            bitmap.recycle();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
