package com.zhiyicx.baseproject.impl.photoselector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuChao
 * @describe 从本地相册选择图片的接口
 * @date 2017/1/13
 * @contact email:450127106@qq.com
 */

public interface IPhotoSelector<T> {
    /**
     * 从本地相册中获取图片
     *
     * @param maxCount       每次能够获取的最大图片数量
     * @param selectedPhotos 已经选择过的图片
     * @return
     */
    void getPhotoListFromSelector(int maxCount, ArrayList<String> selectedPhotos);
    /**
     * 从本地相册中获取图片
     *
     * @param maxCount       每次能够获取的最大图片数量
     * @param selectedPhotos 已经选择过的图片
     * @param isPreviewEnabled 是否可预览
     * @param isShowGif 是否显示gif
     * @return
     */
     void getPhotoListFromSelector(int maxCount, ArrayList<String> selectedPhotos, boolean isPreviewEnabled,boolean isShowGif);
    /**
     * 通过拍照的方式获取图片
     */
    void getPhotoFromCamera(ArrayList<String> selectedPhotos);

    /**
     * 对图片进行裁剪
     */
    void startToCraft(String imgPath);

    /**
     * 判断是否需要裁剪
     */
    boolean isNeededCraft(String imgPath);


}
