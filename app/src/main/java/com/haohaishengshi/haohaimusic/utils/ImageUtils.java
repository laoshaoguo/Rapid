package com.haohaishengshi.haohaimusic.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.haohaishengshi.haohaimusic.R;
import com.haohaishengshi.haohaimusic.base.AppApplication;
import com.haohaishengshi.haohaimusic.data.beans.UserInfoBean;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleBorderTransform;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.SharePreferenceUtils;

import java.util.Locale;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;


import static com.zhiyicx.baseproject.config.ImageZipConfig.IMAGE_100_ZIP;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/7
 * @Contact master.jungle68@gmail.com
 */

public class ImageUtils {
    private static final String SHAREPREFERENCE_USER_HEADPIC_SIGNATURE = "sharepreference_user_headpic_signature";
    private static final String SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE = "sharepreference_user_headpic_signature";

    private static final String SHAREPREFERENCE_USER_COVER_SIGNATURE = "sharepreference_user_cover_signature";
    private static final String SHAREPREFERENCE_CURRENT_LOGIN_USER_COVER__SIGNATURE = "sharepreference_user_cover_signature";
    private static final long DEFAULT_USER_CACHE_TIME = 3 * 24 * 60_1000;
    private static final long DEFAULT_SHAREPREFERENCES_OFFSET_TIME = 10_1000;
    private static long laste_request_time;
    /**
     * 服务器支持的可剪切的最大长度边  阿里 4096
     */
    private static final int MAX_SERVER_SUPPORT_CUT_IMAGE_WITH_OR_HEIGHT = 4000;

    private static long mHeadPicSigture;
    private static long mCoverSigture;

    /**
     * mWidthPixels = DeviceUtils.getScreenWidth(context);
     * mHightPixels = DeviceUtils.getScreenHeight(context);
     * mMargin = 2 * context.getResources().getDimensionPixelSize(R.dimen
     * .dynamic_list_image_marginright);
     * mDiverwith = context.getResources().getDimensionPixelSize(R.dimen.spacing_small);
     * mImageContainerWith = mWidthPixels - mMargin;
     * // 最大高度是最大宽度的4/3 保持 宽高比 3：4
     * mImageMaxHeight = mImageContainerWith * 4 / 3;
     */

    public static int getmWidthPixels() {
        return DeviceUtils.getScreenWidth(AppApplication.getContext());
    }

    public static int getmHightPixels() {
        return DeviceUtils.getScreenHeight(AppApplication.getContext());
    }


    public static int getmMargin() {
        return 2 * AppApplication.getContext().getResources().getDimensionPixelSize(R.dimen.dynamic_list_image_marginright);
    }

    public static int getmDiverwith() {
        return AppApplication.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_small);
    }

    public static int getmImageContainerWith() {
//        int maxWidth = AppApplication.getContext().getResources().getDimensionPixelOffset(R.dimen.dynamic_image_max_width);
        int with = getmWidthPixels() - getmMargin();
//        with = with > maxWidth ? maxWidth : with;
        return with;
    }

    /**
     * 设计图 4：3
     *
     * @return
     */
    public static int getmImageMaxHeight() {
        return getmImageContainerWith() * 4 / 3;
    }

    public static void updateCurrentLoginUserHeadPicSignature(Context context) {
        SharePreferenceUtils.saveLong(context.getApplicationContext(), SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE, System
                .currentTimeMillis() - DEFAULT_USER_CACHE_TIME);
    }

    public static void updateCurrentLoginUserCoverSignature(Context context) {
        SharePreferenceUtils.saveLong(context.getApplicationContext(), SHAREPREFERENCE_CURRENT_LOGIN_USER_COVER__SIGNATURE, System
                .currentTimeMillis() - DEFAULT_USER_CACHE_TIME);
    }

    /**
     * 加载用户背景图
     *
     * @param userInfoBean 用户信息
     * @param imageView    展示的控件
     */
    public static void loadUserCover(UserInfoBean userInfoBean, ImageView imageView) {


        long currentLoginUerId = AppApplication.getmCurrentLoginAuth() == null ? 0 : AppApplication.getmCurrentLoginAuth().getUser_id();

        if (userInfoBean.getUser_id() == currentLoginUerId) {
            mCoverSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(),
                    SHAREPREFERENCE_CURRENT_LOGIN_USER_COVER__SIGNATURE);
        } else {
            mCoverSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(), SHAREPREFERENCE_USER_COVER_SIGNATURE);
        }
        if (System.currentTimeMillis() - mCoverSigture > DEFAULT_USER_CACHE_TIME) {
            mCoverSigture = System.currentTimeMillis();
        }
        SharePreferenceUtils.saveLong(imageView.getContext().getApplicationContext()
                , userInfoBean.getUser_id() == currentLoginUerId ? SHAREPREFERENCE_CURRENT_LOGIN_USER_COVER__SIGNATURE :
                        SHAREPREFERENCE_USER_COVER_SIGNATURE, mHeadPicSigture);
        String cover = "";
        if (userInfoBean.getCover() != null) {
            cover = userInfoBean.getCover().getUrl();
        }
        Glide.with(imageView.getContext())
                .load(cover)
//                .signature(new StringSignature(String.valueOf(mCoverSigture)))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }

//    public static boolean checkImageContext(View imageView) {
//        if (imageView == null || imageView.getContext() == null) {
//            return true;
//        }
//        if (imageView.getContext() instanceof Activity) {
//            if (((Activity) imageView.getContext()).isFinishing()) {
//                return true;
//            }
//        }
//        Activity activity = JZUtils.scanForActivity(imageView.getContext());
//        return activity == null || activity.isDestroyed();
//    }


    /**
     * 加载用户圆形图像
     *
     * @param userInfoBean 用户信息
     * @param imageView    显示头像的控件
     * @param withBorder   是否需要边框
     */
    public static void loadUserHead(UserInfoBean userInfoBean, ImageView imageView, boolean withBorder) {
        loadUserAvatar(userInfoBean, imageView, withBorder);
    }

    /**
     * 加载通用用户信息
     *
     * @param userInfoBean
     * @param imageView
     * @param withBorder
     */
    private static void loadUserAvatar(UserInfoBean userInfoBean, ImageView imageView, boolean withBorder) {
        String avatar = "";
        if (userInfoBean != null && userInfoBean.getUser_id() != null) {
            avatar = userInfoBean.getAvatar() == null ? "" : userInfoBean.getAvatar().getUrl();
            long currentLoginUerId = AppApplication.getmCurrentLoginAuth() == null ? 0 : AppApplication.getmCurrentLoginAuth().getUser_id();
            if (System.currentTimeMillis() - laste_request_time > DEFAULT_SHAREPREFERENCES_OFFSET_TIME || userInfoBean.getUser_id() ==
                    currentLoginUerId) {

                if (userInfoBean.getUser_id() == currentLoginUerId) {
                    mHeadPicSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(),
                            SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE);
                } else {
                    mHeadPicSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(),
                            SHAREPREFERENCE_USER_HEADPIC_SIGNATURE);
                }
                if (System.currentTimeMillis() - mHeadPicSigture > DEFAULT_USER_CACHE_TIME) {
                    mHeadPicSigture = System.currentTimeMillis();
                }
                SharePreferenceUtils.saveLong(imageView.getContext().getApplicationContext()
                        , userInfoBean.getUser_id() == currentLoginUerId ? SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE :
                                SHAREPREFERENCE_USER_HEADPIC_SIGNATURE, mHeadPicSigture);
            }
            laste_request_time = System.currentTimeMillis();
        }
        int defaultErrorAvatar = getDefaultAvatar(userInfoBean);
        if (!TextUtils.isEmpty(avatar) && FileUtils.isFile(avatar)) {

        }
        Glide.with(imageView.getContext())
                .load(TextUtils.isEmpty(avatar) ? R.drawable.shape_default_image : avatar)
//                .signature(new StringSignature(String.valueOf(mHeadPicSigture)));
                .placeholder(withBorder ? defaultErrorAvatar : defaultErrorAvatar)
                .transform(withBorder ?
                        new GlideCircleBorderTransform(imageView.getContext().getApplicationContext(), imageView.getResources()
                                .getDimensionPixelSize(R.dimen.spacing_tiny), ContextCompat.getColor(imageView.getContext(), R.color.white))
                        : new GlideCircleTransform(imageView.getContext().getApplicationContext()))
                .into(imageView);
    }

    private static void loadUserAvatar(UserInfoBean userInfoBean, ImageView imageView, boolean withBorder, int width, int color) {
        String avatar = "";
        if (userInfoBean != null && userInfoBean.getUser_id() != null) {
            avatar = userInfoBean.getAvatar() == null ? "" : userInfoBean.getAvatar().getUrl();
            long currentLoginUerId = AppApplication.getmCurrentLoginAuth() == null ? 0 : AppApplication.getmCurrentLoginAuth().getUser_id();
            if (System.currentTimeMillis() - laste_request_time > DEFAULT_SHAREPREFERENCES_OFFSET_TIME || userInfoBean.getUser_id() ==
                    currentLoginUerId) {

                if (userInfoBean.getUser_id() == currentLoginUerId) {
                    mHeadPicSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(),
                            SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE);
                } else {
                    mHeadPicSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(),
                            SHAREPREFERENCE_USER_HEADPIC_SIGNATURE);
                }
                if (System.currentTimeMillis() - mHeadPicSigture > DEFAULT_USER_CACHE_TIME) {
                    mHeadPicSigture = System.currentTimeMillis();
                }
                SharePreferenceUtils.saveLong(imageView.getContext().getApplicationContext()
                        , userInfoBean.getUser_id() == currentLoginUerId ? SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE :
                                SHAREPREFERENCE_USER_HEADPIC_SIGNATURE, mHeadPicSigture);
            }
            laste_request_time = System.currentTimeMillis();
        }
        int defaultErrorAvatar = getDefaultAvatar(userInfoBean);
        if (!TextUtils.isEmpty(avatar) && FileUtils.isFile(avatar)) {

        }
        Glide.with(imageView.getContext())
                .load(TextUtils.isEmpty(avatar) ? R.drawable.shape_default_image : avatar)
//                .signature(new StringSignature(String.valueOf(mHeadPicSigture)));
                .placeholder(withBorder ? defaultErrorAvatar : defaultErrorAvatar)
                .transform(withBorder ?
                        new GlideCircleBorderTransform(imageView.getContext().getApplicationContext(), width, color)
                        : new GlideCircleTransform(imageView.getContext().getApplicationContext()))
                .into(imageView);
    }


    /**
     * 获取用户头像地址
     *
     * @param userId user's  id
     */
    public static String getUserAvatar(Long userId) {
        if (userId == null) {
            userId = 0L;
        }
        return String.format(ApiConfig.IMAGE_AVATAR_PATH_V2, userId);

    }

    /**
     * 获取用户头像地址
     *
     * @param userInfoBean user's  info
     */
    public static String getUserAvatar(UserInfoBean userInfoBean) {
        if (userInfoBean == null || userInfoBean.getAvatar() == null) {
            return "";
        } else {
            return userInfoBean.getAvatar() == null ? "" : userInfoBean.getAvatar().getUrl();
        }
    }

    /**
     * 获取用户默认头像
     *
     * @param userInfoBean user's  info
     */
    public static int getDefaultAvatar(UserInfoBean userInfoBean) {
        int defaultAvatar;
        if (userInfoBean == null) {
            return R.mipmap.pic_default_secret;
        }
        switch (userInfoBean.getSex()) {

            case UserInfoBean.FEMALE:
                defaultAvatar = R.mipmap.pic_default_woman;
                break;
            case UserInfoBean.MALE:
                defaultAvatar = R.mipmap.pic_default_man;

                break;
            case UserInfoBean.SECRET:
                defaultAvatar = R.mipmap.pic_default_secret;
                break;
            default:
                defaultAvatar = R.mipmap.pic_default_secret;

        }
        return defaultAvatar;
    }



    /**
     * 图片地址转换 V2 api
     *
     * @param canLook 是否可以查看
     * @param storage 图片对应的 id 号，也可能是本地的图片路径
     * @param part    压缩比例 0-100
     */
    public static GlideUrl imagePathConvertV2(boolean canLook, int storage, int w, int h, int part, String token) {
        //阿里云限制访问图片宽高不超过4099
        String url = imagePathConvertV2(storage, w, h, part, canLook);
        return imagePathConvertV2(url, token);
    }


    /**
     * 图片地址转换 V2 api
     *
     * @param url   图片地址
     * @param token 图片token
     */
    public static GlideUrl imagePathConvertV2(String url, String token) {
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization", token)
                .build());
    }

    /**
     * @param storage 图片资源id
     * @param w       宽
     * @param h       高
     * @param part    压缩比例
     * @param token   token
     */
    public static GlideUrl imagePathConvertV2(int storage, int w, int h, int part, String token) {
        return new GlideUrl(imagePathConvertV2(storage, w, h, part), new LazyHeaders.Builder()
                .addHeader("Authorization", token)
                .build());
    }

    /**
     * 通过 图片 id 、with、height、quality
     *
     * @param storage id
     * @param w       with
     * @param h       height
     * @param part    quality
     * @return 请求图片的地址
     */
    public static String imagePathConvertV2(int storage, int w, int h, int part) {
        if (part == IMAGE_100_ZIP) {
            return String.format(Locale.getDefault(), ApiConfig.APP_DOMAIN + ApiConfig.IMAGE_PATH_V2_ORIGIN, storage);
        } else {
            if (part < 40) {
                part = 40;
            }
            return String.format(Locale.getDefault(), ApiConfig.APP_DOMAIN + ApiConfig.IMAGE_PATH_V2, storage, w, h, part);
        }

    }

    /**
     * 通过 图片 id 、with、height、quality
     *
     * @param storage id
     * @param w       with
     * @param h       height
     * @param part    quality
     * @return 请求图片的地址
     */
    public static String imagePathConvertV2(int storage, int w, int h, int part, boolean canLook) {
        if (canLook && part == IMAGE_100_ZIP) {
//            if (!ApiConfig.APP_DOMAIN.equals(ApiConfig.APP_DOMAIN_FORMAL)){
                return String.format(Locale.getDefault(), ApiConfig.APP_DOMAIN + ApiConfig.IMAGE_PATH_V2_ORIGIN, storage);
//            }else{
//                return String.format(Locale.getDefault(), ApiConfig.APP_DOMAIN + ApiConfig.IMAGE_PATH_NQ, storage,w,h);
//            }
        } else {
            if (part < 40) {
                part = 40;
            }
            if (!ApiConfig.APP_DOMAIN.equals(ApiConfig.APP_DOMAIN_FORMAL)){
                return String.format(Locale.getDefault(), ApiConfig.APP_DOMAIN + ApiConfig.IMAGE_PATH_V2, storage, w, h, part);
            }else {
                return String.format(Locale.getDefault(), ApiConfig.APP_DOMAIN + ApiConfig.IMAGE_PATH_NQ, storage,w,h);
            }

        }

    }

    /**
     * 通过 file path 获取 bitmap size
     *
     * @param url file path
     * @return bitmap size
     */
    public static long[] getBitmapSize(String url) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, op);
        return new long[]{op.outWidth, op.outHeight};
    }

    /**
     * 默认加载图片
     *
     * @param imageView target view to display image
     * @param url       image resuorce path
     */
    public static void loadImageDefault(ImageView imageView, String url) {

        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.shape_default_image)
                .error(R.drawable.shape_default_error_image)
                .into(imageView);

    }

    /**
     * 默认加载图片
     *
     * @param imageView target view to display image
     * @param url       image resuorce path
     */
    public static void loadImageDefaultNoHolder(ImageView imageView, String url) {

        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.shape_default_error_image)
                .into(imageView);

    }

    /**
     * 加载圆图
     *
     * @param imageView
     * @param url
     */
    public static void loadCircleImageDefault(ImageView imageView, String url) {
        loadCircleImageDefault(imageView, url, R.drawable.shape_default_error_image, R.drawable.shape_default_image);
    }

    /**
     * 加载圆图
     *
     * @param imageView
     * @param url
     */
    public static void loadCircleImageDefault(ImageView imageView, String url, int errorResId, int placeResId) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(placeResId)
                .error(errorResId)
                .bitmapTransform(new GlideCircleTransform(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 加载圆图
     *
     * @param imageView
     * @param resId
     */
    public static void loadCircleImageDefault(ImageView imageView, int resId, int errorResId, int placeResId) {
        Glide.with(imageView.getContext())
                .load(resId)
                .placeholder(placeResId)
                .error(errorResId)
                .bitmapTransform(new GlideCircleTransform(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 获取 iamgeview 的 bitmap
     *
     * @param showImageView
     * @return
     */
    public static Bitmap getImageViewBitMap(ImageView showImageView) {
        showImageView.setDrawingCacheEnabled(true);

        Bitmap bitmap = showImageView.getDrawingCache();

        showImageView.setDrawingCacheEnabled(false);
        return bitmap;
    }



    /**
     * 是否是长图
     *
     * @param netHeight
     * @param netWidth
     * @return
     */
    public static boolean isLongImage(float netHeight, float netWidth) {
        float ratio = netHeight / netWidth;
        float result = 0;
        if (ratio >= 3 || ratio <= .3f) {

            result = getmWidthPixels() / netWidth;

            if (result <= .3f) {

            } else {
                result = result * netHeight / getmHightPixels();
            }
        }
        return (result >= 3 || result <= .3f) && result > 0;

    }

    /**
     * 图片宽高是否超过了限制
     *
     * @return
     */
    public static boolean isWithOrHeightOutOfBounds(int with, int height) {
        return with > MAX_SERVER_SUPPORT_CUT_IMAGE_WITH_OR_HEIGHT
                || height > MAX_SERVER_SUPPORT_CUT_IMAGE_WITH_OR_HEIGHT;
    }

    /**
     * 得到不同设备对加载图片支持的最大尺寸
     * (解决魅族手机加载超长图，无法正常显示的问题)
     * @return
     */
    public static int getOpenglRenderLimitValue() {
        int maxsize ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            maxsize =getOpenglRenderLimitEqualAboveLollipop();
        }else {
            maxsize =getOpenglRenderLimitBelowLollipop();
        }
        return maxsize ==0 ? 4096 : maxsize;
    }

    private static int getOpenglRenderLimitBelowLollipop() {
        int[] maxSize =new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize,0);
        return maxSize[0];
    }

    private static int getOpenglRenderLimitEqualAboveLollipop() {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int[] vers =new int[2];
        egl.eglInitialize(dpy, vers);
        int[] configAttr = {
                EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RGB_BUFFER,
                EGL10.EGL_LEVEL,0,
                EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PBUFFER_BIT,
                EGL10.EGL_NONE};
        EGLConfig[] configs =new EGLConfig[1];
        int[] numConfig =new int[1];
        egl.eglChooseConfig(dpy, configAttr, configs,1, numConfig);
        if (numConfig[0] ==0) {// TROUBLE! No config found.
             }
        EGLConfig config = configs[0];
        int[] surfAttr = {
                EGL10.EGL_WIDTH,64,
                EGL10.EGL_HEIGHT,64,
                EGL10.EGL_NONE};
        EGLSurface surf = egl.eglCreatePbufferSurface(dpy, config, surfAttr);
        final int EGL_CONTEXT_CLIENT_VERSION =0x3098;// missing in EGL10
        int[] ctxAttrib = {EGL_CONTEXT_CLIENT_VERSION,1,
                EGL10.EGL_NONE};
        EGLContext ctx = egl.eglCreateContext(dpy, config, EGL10.EGL_NO_CONTEXT, ctxAttrib);
        egl.eglMakeCurrent(dpy, surf, surf, ctx);
        int[] maxSize =new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize,0);
        egl.eglMakeCurrent(dpy, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(dpy, surf);
        egl.eglDestroyContext(dpy, ctx);
        egl.eglTerminate(dpy);
        return maxSize[0];
    }


}
