package com.zhiyicx.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Describe Drawable 辅助工具类
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

public class DrawableProvider {


    /**
     * 获得选择器
     */
    public static Drawable getStateListDrawable(Drawable normalDrawable, Drawable pressDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, pressDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);
        return stateListDrawable;
    }

    /**
     * 将 radiobutton 的 drawable 动态的缩放
     */
    public static Drawable getScaleDrawableForRadioButton(float percent, TextView rb) {
        Drawable[] compoundDrawables = rb.getCompoundDrawables();
        Drawable drawable = null;
        for (Drawable d : compoundDrawables) {
            if (d != null) {
                drawable = d;
            }
        }
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * percent + 0.5f), (int) (drawable.getIntrinsicHeight() * percent + 0.5f));
        return drawable;
    }

    /**
     * 将 radiobutton 的 drawable 动态的缩放
     */
    public static Drawable getScaleDrawableForRadioButton2(float width, TextView rb) {
        Drawable[] compoundDrawables = rb.getCompoundDrawables();
        Drawable drawable = null;
        for (Drawable d : compoundDrawables) {
            if (d != null) {
                drawable = d;
            }
        }
        float percent = width * 1.0f / drawable.getIntrinsicWidth();
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * percent + 0.5f), (int) (drawable.getIntrinsicHeight() * percent + 0.5f));
        return drawable;
    }

    /**
     * 传入图片，将图片按传入比例缩小
     */
    public static Drawable getScaleDrawable(float percent, Drawable drawable) {
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * percent + 0.5f), (int) (drawable.getIntrinsicHeight() * percent + 0.5f));
        return drawable;
    }

    /**
     * 传入图片，将图片按传入比例缩小
     */
    public static Drawable getScaleDrawable2(float width, Drawable drawable) {
        float percent = width * 1.0f / drawable.getIntrinsicWidth();
        drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * percent + 0.5f), (int) (drawable.getIntrinsicHeight() * percent + 0.5f));
        return drawable;
    }

    /**
     * 设置左边的 drawable
     */
    public static void setLeftDrawable(TextView tv, Drawable drawable) {
        tv.setCompoundDrawables(drawable, null, null, null);
    }

    /**
     * 改变 Bitmap 的长宽
     */
    public static Bitmap getReSizeBitmap(Bitmap bitmap, float targetWidth, float targetheight) {
        Bitmap returnBm = null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(targetWidth / width, targetheight / height); //长和宽放大缩小的比例
        try {
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其 EXIF 信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片的旋转角度置为0  ，此方法可以解决某些机型拍照后图像，出现了旋转情况
     *
     * @return void
     * @Title: setPictureDegreeZero
     */
    public static void setPictureDegreeZero(String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            // 修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
            // 例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_NORMAL));
            exifInterface.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取文件为图片时的图片宽高
     * 矫正图片角度问题
     * options.outWidth
     * options.outHeight
     */
    public static BitmapFactory.Options getPicsWHByFile(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options); // 此时返回的bitmap为null

        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                int h = options.outWidth;
                options.outWidth = options.outHeight;
                options.outHeight = h;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return options;
    }

    /**
     * 获取imageview的rect属性:上下左右的位置
     */
    public static Rect getBitmapRectFromImageView(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();

        Rect rect = new Rect();
        boolean isVisible = imageView.getGlobalVisibleRect(rect);
        if (!isVisible) {
            int[] location = new int[2];
            imageView.getLocationOnScreen(location);

            rect.left = location[0];
            rect.top = location[1];
            rect.right = rect.left + imageView.getWidth();
            rect.bottom = rect.top + imageView.getHeight();
        }
        if (drawable != null) {

            int bitmapWidth = drawable.getIntrinsicWidth();
            int bitmapHeight = drawable.getIntrinsicHeight();

//            int imageViewWidth = imageView.getWidth() - imageView.getPaddingLeft() - imageView
//                    .getPaddingRight();
            int imageViewWidth = DeviceUtils.getScreenWidth(imageView.getContext());
            int imageviewHeight = imageView.getHeight() - imageView.getPaddingTop() - imageView
                    .getPaddingBottom();

            float startScale;
//            if ((float) imageViewWidth / bitmapWidth
//                    > (float) imageviewHeight / bitmapHeight) {
//                // Extend start bounds horizontally
//                startScale = (float) imageviewHeight / bitmapHeight;
//            } else {
            startScale = (float) imageViewWidth / bitmapWidth;
//            }
//
//            bitmapHeight = (int) (bitmapHeight * startScale);
//            bitmapWidth = (int) (bitmapWidth * startScale);
            bitmapHeight = (int) (bitmapHeight * startScale);
            int deltaX = (imageViewWidth - bitmapWidth) / 2;
            int deltaY = (imageviewHeight - bitmapHeight) / 2;

//            rect.set(rect.left + deltaX, rect.top + deltaY, rect.right - deltaX,
//                    rect.bottom - deltaY);
            rect.set(0, 0, imageViewWidth,
                    bitmapHeight);
            return rect;
        } else {
            return null;
        }
    }

    /**
     * 获取imageview的rect属性:上下左右的位置
     */
    public static Rect getBitmapRectCloseImageView(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();

        Rect rect = new Rect();
        boolean isVisible = imageView.getGlobalVisibleRect(rect);
        if (!isVisible) {
            int[] location = new int[2];
            imageView.getLocationOnScreen(location);

            rect.left = location[0];
            rect.top = location[1];
            rect.right = rect.left + imageView.getWidth();
            rect.bottom = rect.top + imageView.getHeight();
        }
        if (drawable != null) {

            int bitmapWidth = drawable.getIntrinsicWidth();
            int bitmapHeight = drawable.getIntrinsicHeight();

            int imageViewWidth = imageView.getWidth() - imageView.getPaddingLeft() - imageView
                    .getPaddingRight();
            int imageviewHeight = imageView.getHeight() - imageView.getPaddingTop() - imageView
                    .getPaddingBottom();

            float startScale;
            if ((float) imageViewWidth / bitmapWidth
                    > (float) imageviewHeight / bitmapHeight) {
                // Extend start bounds horizontally
                startScale = (float) imageviewHeight / bitmapHeight;
            } else {
                startScale = (float) imageViewWidth / bitmapWidth;
            }

            bitmapHeight = (int) (bitmapHeight * startScale);
            bitmapWidth = (int) (bitmapWidth * startScale);

            int deltaX = (imageViewWidth - bitmapWidth) / 2;
            int deltaY = (imageviewHeight - bitmapHeight) / 2;

            rect.set(rect.left + deltaX, rect.top + deltaY, rect.right - deltaX,
                    rect.bottom - deltaY);

            return rect;
        } else {
            return null;
        }
    }

    /**
     * 保存bitmap到文件
     *
     * @param picName 图片名称
     * @param imgPath 图片目录
     * @return 失败返回各种提示，成功返回图片路径 -2 sd卡不存在 -1 图片保存失败 其他 图片保存成功
     */
    public static String saveBitmap(Bitmap bm, String picName, String imgPath) {
        // 保存在sd卡中
        File dir = new File(Environment.getExternalStorageDirectory(), imgPath);

        if (!dir.exists() || !dir.isDirectory()) {
            // 如果没有这样的文件，或者有同名的文件但不是目录，就需要创建这样的目录
            if (!dir.mkdir()) {
                return "-2";
            }
        }
        try {
            File f = new File(dir + "/" + picName);
            FileOutputStream out = new FileOutputStream(f);
            boolean isSuccess = bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            if (isSuccess) {
                return f.getAbsolutePath();
            } else {
                return "-1";
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return "-1";
    }

}
