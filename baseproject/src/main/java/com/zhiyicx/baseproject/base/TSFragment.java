package com.zhiyicx.baseproject.base;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.utils.ExcutorUtil;
import com.zhiyicx.baseproject.utils.WindowUtils;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.InputPasswordView;
import com.zhiyicx.baseproject.widget.dialog.LoadingDialog;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.base.BaseFragment;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.common.widget.popwindow.CustomPopupWindow.POPUPWINDOW_ALPHA;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public abstract class TSFragment<P extends IBasePresenter> extends BaseFragment<P>
        implements WindowUtils.OnWindowDismisslistener,
        InputPasswordView.OnClickListener,
        InputLimitView.OnAtTriggerListener, InputLimitView.OnSendClickListener {

    /**
     * 默认的toolbar
     */
    private static final int DEFAULT_TOOLBAR = R.layout.toolbar_custom;

    /**
     * 默认的toolbar背景色
     */
    private static final int DEFAULT_TOOLBAR_BACKGROUD_COLOR = R.color.white;

    /**
     * 默认的toolbar下方分割线颜色
     */
    private static final int DEFAULT_DIVIDER_COLOR = R.color.general_for_line;

    /**
     * 默认的toolbar左边的图片，一般是返回键
     */
    private static final int DEFAULT_TOOLBAR_LEFT_IMG = R.mipmap.topbar_back;

    /**
     * 顶部导航栏左边控件
     */
    protected TextView mToolbarLeft;

    /**
     * 导航栏下的分割线
     */
    protected View mDriver;

    /**
     * 顶部导航栏最右边控件
     */
    protected TextView mToolbarRight;

    /**
     * 顶部导航栏右边相对靠左的控件
     */
    protected TextView mToolbarRightLeft;

    /**
     * 顶部导航栏中间控件
     */
    protected TextView mToolbarCenter;

    /**
     * 当侵入状态栏时， 状态栏的占位控件
     */
    protected View mStatusPlaceholderView;

    /**
     * 加载
     */
    protected View mCenterLoadingView;

    /**
     * 密码输入控件
     */
    protected InputPasswordView mIlvPassword;

    /**
     * 阴影布局
     */
    protected View mVShadow;

    /**
     * 评论输入
     */
    protected InputLimitView mIlvComment;

    /**
     * 头部左边的刷新控件
     */
    protected ImageView mIvRefresh;

    /**
     * 用于顶部弹出提示控件的根视图
     */
    protected ViewGroup mSnackRootView;

    /**
     * 缺省图是否需要点击
     */
    private boolean mIsNeedClick = true;

    /**
     * 右上角的按钮因为音乐播放悬浮显示，是否已经偏左移动
     */
    private boolean rightViewHadTranslated = false;

    /**
     * View 树监听订阅器
     */
    private Subscription mViewTreeSubscription = null;
    /**
     * View 树监听订阅器
     */
    private Subscription mStatusbarSupport = null;

    /**
     * 中心加载弹框
     */
    private LoadingDialog mCenterLoadingDialog;

    /**
     * 顶部弹出提示控件
     */
    private TSnackbar mSnackBar;

    /**
     * 音乐播放时右上角的控件
     */
    private View mMusicWindowView;

    /**
     * 系统消息数据
     */
    protected SystemConfigBean mSystemConfigBean;

    /**
     * 删除二次确认弹框
     */
    private ActionPopupWindow mDeleteTipPopupWindow;

    /**
     * 限制弹窗
     */
    private ActionPopupWindow mIntroducePop;

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    /**
     * fragment 的布局初始化
     *
     * @return
     */
    @Override
    protected View getContentView() {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // 是否添加和状态栏等高的占位 View
        if (setUseSatusbar() && setUseStatusView()) {
            mStatusPlaceholderView = new View(getContext());
            mStatusPlaceholderView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtils.getStatuBarHeight
                    (getContext())));
            if (StatusBarUtils.intgetType(getActivity().getWindow()) == 0 && ContextCompat.getColor(getContext(), setToolBarBackgroud()) == Color
                    .WHITE) {
                mStatusPlaceholderView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.themeColor));
            } else {
                mStatusPlaceholderView.setBackgroundColor(ContextCompat.getColor(getContext(), setToolBarBackgroud()));
            }
            linearLayout.addView(mStatusPlaceholderView);
        }
        // 在需要显示toolbar时，进行添加
        if (showToolbar()) {
            View toolBarContainer = mLayoutInflater.inflate(getToolBarLayoutId(), null);
            initDefaultToolBar(toolBarContainer);
            linearLayout.addView(toolBarContainer);
        }
        // 在需要显示分割线时，进行添加
        if (showToolBarDivider()) {
            mDriver = new View(getContext());
            mDriver.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen
                    .divider_line)));
            mDriver.setBackgroundColor(ContextCompat.getColor(getContext(), setToolBarDividerColor()));
            linearLayout.addView(mDriver);
        }
        if (setUseSatusbar()) {
            // 状态栏顶上去
            StatusBarUtils.transparencyBar(getActivity());
            linearLayout.setFitsSystemWindows(false);
        } else {
            // 状态栏不顶上去
            StatusBarUtils.setStatusBarColor(getActivity(), setToolBarBackgroud());
            linearLayout.setFitsSystemWindows(true);
        }
        // 是否设置状态栏文字图标灰色，对 小米、魅族、Android 6.0 及以上系统有效
        if (setStatusbarGrey()) {
            StatusBarUtils.statusBarLightMode(getActivity());
        }
        setToolBarTextColor();
        // 内容区域
        final View bodyContainer = mLayoutInflater.inflate(getBodyLayoutId(), null);
        bodyContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // 加载动画
        if (setUseCenterLoading() || setUseInputPsdView() || setUseShadowView() || setUseInputCommentView()) {
            FrameLayout frameLayout = new FrameLayout(getActivity());
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            frameLayout.addView(bodyContainer);

            if (setUseCenterLoading()) {
                mCenterLoadingView = mLayoutInflater.inflate(R.layout.view_center_loading, null);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                if (!showToolbar()) {
                    params.setMargins(0, getstatusbarAndToolbarHeight(), 0, 0);
                }
                mCenterLoadingView.setLayoutParams(params);
                if (setUseCenterLoadingAnimation()) {
                    ((AnimationDrawable) ((ImageView) mCenterLoadingView.findViewById(R.id.iv_center_load)).getDrawable()).start();
                }
                RxView.clicks(mCenterLoadingView.findViewById(R.id.iv_center_holder))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                        .compose(this.<Void>bindToLifecycle())
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                if (mIsNeedClick) {
                                    setLoadingViewHolderClick();
                                }
                            }
                        });
                frameLayout.addView(mCenterLoadingView);
            }
            if (setUseShadowView()) {
                mVShadow = mLayoutInflater.inflate(R.layout.view_shadow, null);
                mVShadow.setVisibility(View.GONE);
                RxView.clicks(mVShadow)
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                        .filter(new Func1<Void, Boolean>() {
                            @Override
                            public Boolean call(Void aVoid) {
                                return setNeedShadowViewClick();
                            }
                        })
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                onShadowViewClick();
                            }
                        });
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mVShadow.setLayoutParams(params);
                frameLayout.addView(mVShadow);
            }


            if (setUseInputCommentView() || setUseInputPsdView()) {
                FrameLayout view = (FrameLayout) mLayoutInflater.inflate(R.layout.view_input_psd_and_comment, null);
                view.setFitsSystemWindows(true);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.BOTTOM;
                if (setUseInputCommentView()) {
                    mIlvComment = view.findViewById(R.id.ilv_comment);
                    mIlvComment.setOnSendClickListener(this);
                    mIlvComment.setOnAtTriggerListener(this);
                    mIlvComment.setLayoutParams(params);
                    mIlvComment.setVisibility(View.GONE);
                }
                if (setUseInputPsdView()) {
                    mIlvPassword = view.findViewById(R.id.ilv_password);
                    mIlvPassword.setOnClickListener(this);
                    mIlvPassword.setLayoutParams(params);
                    mIlvPassword.setVisibility(View.GONE);
                }
                frameLayout.addView(view);
            }
            linearLayout.addView(frameLayout);
        } else {
            linearLayout.addView(bodyContainer);
        }

        mSnackRootView = (ViewGroup) getActivity().findViewById(android.R.id.content).getRootView();
        if (needCenterLoadingDialog()) {
            mCenterLoadingDialog = new LoadingDialog(getActivity());
        }
        return linearLayout;
    }

    /**
     * Activity 创建成果后的回调
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 添加音乐悬浮窗
//        if (getParentFragment() == null && needMusicWindowView()) {
//
//            mMusicWindowView = mLayoutInflater.inflate(R.layout.windows_music, null);
//
//            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(getResources().getDimensionPixelOffset(R.dimen.music_icon_size),
//                    getResources().getDimensionPixelOffset(R.dimen.music_icon_size));
//            layoutParams.gravity = Gravity.RIGHT;
//            int marginTop = getMusicWindowExtraTopMargin() + (getResources().getDimensionPixelOffset(R.dimen.toolbar_height) - getResources().getDimensionPixelOffset(R.dimen.music_icon_size)) / 2;
//            layoutParams.setMargins(0, marginTop, getResources().getDimensionPixelOffset(R.dimen.spacing_normal), 0);
//            mMusicWindowView.setLayoutParams(layoutParams);
//            mMusicWindowView.setVisibility(View.GONE);
//            mMusicWindowView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent("android.intent.action.MAIN");
//                    intent.setClassName(getActivity(), "com.zhiyicx.thinksnsplus.modules.music_fm.music_play.MusicPlayActivity");
//                    intent.putExtra("music_info", WindowUtils.getMusicAlbumDetailsBean());
//                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    getActivity().startActivity(intent);
//                }
//            });
//
//            if (mActivity instanceof BaseActivity) {
//                ((ViewGroup) mActivity.findViewById(R.id.fl_fragment_container)).addView(mMusicWindowView);
//            }
//        }
    }

    protected int getMusicWindowExtraTopMargin() {
        return setMusicWindowUseSatusbar() ? DeviceUtils.getStatuBarHeight(mActivity) : 0;
    }

    protected boolean setMusicWindowUseSatusbar() {
        boolean useStatusBar = Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        return !useStatusBar || setUseSatusbar();
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isShow = WindowUtils.getIsShown();
        musicWindowsStatus(isShow);
        WindowUtils.setWindowDismisslistener(this);
        if (isShow && mMusicWindowView != null && needMusicWindowView()) {
            mMusicWindowView.setVisibility(View.VISIBLE);
            RotateAnimation mRotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim
                    .music_window_rotate);
            mMusicWindowView.setAnimation(mRotateAnimation);
            mRotateAnimation.start();
        }
    }

    @Override
    public void onPause() {
        WindowUtils.removeWindowDismisslistener(this);
        if (setUseShadowView()) {
            onShadowViewClick();
        }
        super.onPause();
    }

    /**
     * Presenter 初始化，在 Presenter Inject 成功后自动调用
     *
     * @param presenter
     */
    @Override
    public void setPresenter(P presenter) {
        this.mPresenter = presenter;
    }

    /**
     * 显示加载
     */
    @Override
    public void showLoading() {

    }

    /**
     * 隐藏加载
     */
    @Override
    public void hideLoading() {

    }

    /**
     * 隐藏顶部弹出的提示控件
     */
    @Override
    public void dismissSnackBar() {
        if (mSnackBar != null) {
            mSnackBar.dismiss();
        }
    }

    /**
     * 弹出顶部弹出提示框，可自定义 弹出框种类
     *
     * @param message
     * @param prompt
     */
    @Override
    public void showSnackMessage(final String message, final Prompt prompt) {
        if (mSnackBar != null) {
            mSnackBar.dismiss();
            mSnackBar = null;
        }
        mSnackBar = TSnackbar.make(mSnackRootView, message, TSnackbar.LENGTH_SHORT)
                .setPromptThemBackground(prompt)
                .setCallback(new TSnackbar.Callback() {
                    @Override
                    public void onDismissed(TSnackbar tsnackbar, @DismissEvent int event) {
                        super.onDismissed(tsnackbar, event);
                        switch (event) {
                            case DISMISS_EVENT_TIMEOUT:
                                try {
                                    snackViewDismissWhenTimeOut(prompt, message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                        }
                    }
                });
        mSnackBar.show();
    }

    /**
     * 支付密码界面-确认
     *
     * @return
     */
    @Override
    public void onSureClick(View v, String text, InputPasswordView.PayNote payNote) {

    }

    /**
     * 支付密码界面-忘记密码
     *
     * @return
     */
    @Override
    public void onForgetPsdClick() {

    }

    /**
     * 支付密码界面-取消
     *
     * @return
     */
    @Override
    public void onCancle() {

    }

    /**
     * 阴影点击
     *
     * @return
     */
    protected void onShadowViewClick() {
    }

    /**
     * 评论界面-发送
     *
     * @return
     */
    @Override
    public void onSendClick(View v, String text) {

    }

    /**
     * 评论界面-触发@
     *
     * @return
     */
    @Override
    public void onAtTrigger() {

    }

    /**
     * 当弹出框关闭后回调
     *
     * @param prompt  弹框类型
     * @param message 提示内容
     */
    protected void snackViewDismissWhenTimeOut(Prompt prompt, String message) {
        snackViewDismissWhenTimeOut(prompt);
    }

    /**
     * 当弹出框关闭后回调
     *
     * @param prompt 弹框类型
     */
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
    }

    /**
     * 显示成功提示弹框
     *
     * @param message 提示内容
     */
    @Override
    public void showSnackSuccessMessage(String message) {
        showSnackMessage(message, Prompt.SUCCESS);
    }

    /**
     * 显示错误提示弹框
     *
     * @param message 提示内容
     */
    @Override
    public void showSnackErrorMessage(String message) {
        showSnackMessage(message, Prompt.ERROR);
    }

    /**
     * 显示警告提示弹框
     *
     * @param message 提示内容
     */
    @Override
    public void showSnackWarningMessage(String message) {
        showSnackMessage(message, Prompt.WARNING);
    }

    /**
     * 显示加载中的提示弹框
     *
     * @param message 提示内容
     */
    @Override
    public void showSnackLoadingMessage(String message) {
        if (mSnackBar != null) {
            mSnackBar.dismiss();
            mSnackBar = null;
        }
        mSnackBar = TSnackbar.make(mSnackRootView, message, TSnackbar.LENGTH_INDEFINITE)
                .setPromptThemBackground(Prompt.SUCCESS)
                .addIconProgressLoading(R.drawable.frame_loading_grey, true, false);

        mSnackBar.show();
    }

    /**
     * 根据类名跳转页面
     *
     * @param cls 目标类名
     */
    @Override
    public void goTargetActivity(Class<?> cls) {
        startActivity(new Intent(getActivity(), cls));
    }

    /**
     * 默认显示提示的方法，具体实现在子类处理
     *
     * @param message 提示内容
     */
    @Override
    public void showMessage(String message) {

    }

    @Override
    public void paySuccess() {
        dismissSnackBar();
        if (mIlvPassword == null) {
            return;
        }
        mIlvPassword.setLoading(false);
        showInputPsdView(false);
    }

    @Override
    public void payFailed(String msg) {
        dismissSnackBar();
        if (mIlvPassword == null) {
            return;
        }
        mIlvPassword.setErrorTip(msg);
    }

    /**
     * 音乐图标消失
     */
    @Override
    public void onMusicWindowDismiss() {
        if (mMusicWindowView != null) {
            mMusicWindowView.clearAnimation();
            mMusicWindowView.setVisibility(View.GONE);
        }
        View view = getRightViewOfMusicWindow();
        if (view != null && WindowUtils.getIsPause()) {
            if (view.getTag() != null) {
                rightViewHadTranslated = false;
                int right = (int) view.getTag();
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight() - right, view.getPaddingBottom());
                view.setTag(null);
            }

        }
        if (WindowUtils.getIsPause()) {
            WindowUtils.removeWindowDismisslistener(this);
        }

    }

    /**
     * 关闭中心放大缩小加载动画
     */
    @Override
    public void closeLoadingView() {
        if (mCenterLoadingView == null) {
            return;
        }
        if (mCenterLoadingView.getVisibility() == View.VISIBLE) {
            ((AnimationDrawable) ((ImageView) mCenterLoadingView.findViewById(R.id.iv_center_load)).getDrawable()).stop();
            mCenterLoadingView.animate().alpha(0.3f).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (mCenterLoadingView != null) {
                        mCenterLoadingView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
    }

    /**
     * 开启中心放大缩小加载动画
     */
    protected void showLoadingView() {
        if (mCenterLoadingView == null) {
            return;
        }
        if (mCenterLoadingView.getVisibility() == View.GONE) {
            mCenterLoadingView.findViewById(R.id
                    .iv_center_load).setVisibility(View.VISIBLE);
            ((AnimationDrawable) ((ImageView) mCenterLoadingView.findViewById(R.id
                    .iv_center_load)).getDrawable()).start();
            mCenterLoadingView.setAlpha(1);
            mCenterLoadingView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载失败，占位图点击事件
     */
    protected void setLoadingViewHolderClick() {
        if (mCenterLoadingView == null) {
            return;
        }
        mCenterLoadingView.setVisibility(View.VISIBLE);
        mCenterLoadingView.findViewById(R.id.iv_center_load).setVisibility(View.VISIBLE);
        mCenterLoadingView.findViewById(R.id.iv_center_holder).setVisibility(View.GONE);
        ((AnimationDrawable) ((ImageView) mCenterLoadingView.findViewById(R.id.iv_center_load)).getDrawable()).start();

    }

    /**
     * 显示加载失败
     */
    protected void showLoadViewLoadError() {
        showErrorImage();
        mIsNeedClick = true;
    }

    /**
     * 显示加载失败
     */
    protected void showLoadViewLoadErrorDisableClick() {
        showErrorImage();
        mIsNeedClick = false;
    }

    /**
     * 显示加载失败
     *
     * @param isNeedClick 是否需要回调
     */
    protected void showLoadViewLoadErrorDisableClick(boolean isNeedClick) {
        showErrorImage();
        mIsNeedClick = isNeedClick;
    }

    /**
     * 显示错误图片
     */
    private void showErrorImage() {
        if (mCenterLoadingView == null) {
            return;
        }
        mCenterLoadingView.setVisibility(View.VISIBLE);
        ((AnimationDrawable) ((ImageView) mCenterLoadingView.findViewById(R.id.iv_center_load)).getDrawable()).stop();
        mCenterLoadingView.findViewById(R.id.iv_center_load).setVisibility(View.GONE);
        mCenterLoadingView.findViewById(R.id.iv_center_holder).setVisibility(View.VISIBLE);
    }


    /**
     * 登录提示框
     */
    @Override
    public void showLoginPop() {
        goLogin();
    }

    /**
     * 设置加载失败占位图
     *
     * @param resId
     */
    protected void setLoadViewHolderImag(@DrawableRes int resId) {
        if (mCenterLoadingView == null) {
            return;
        }
        ((ImageView) mCenterLoadingView.findViewById(R.id.iv_center_holder)).setImageResource(resId);
    }

    /**
     * 获取状态栏和操作栏的高度
     *
     * @return
     */
    protected int getstatusbarAndToolbarHeight() {
        return DeviceUtils.getStatuBarHeight(getContext()) + getResources().getDimensionPixelOffset(R.dimen.toolbar_height) + getResources()
                .getDimensionPixelOffset(R.dimen.divider_line);
    }

    /**
     * 中心菊花
     *
     * @param msg
     */
    @Override
    public void showCenterLoading(String msg) {
        if (mCenterLoadingDialog != null) {
            mCenterLoadingDialog.showStateIng(msg);
        }
    }

    /**
     * 中心菊花
     */
    @Override
    public void hideCenterLoading() {
        if (mCenterLoadingDialog != null) {
            mCenterLoadingDialog.onDestroy();
        }
    }

    /**
     * 是否开启中心加载布局 对应 closeLoadingView
     *
     * @return
     */
    protected boolean setUseCenterLoading() {
        return false;
    }

    /**
     * 是否使用 支付密码界面
     *
     * @return
     */
    protected boolean setUseInputPsdView() {
        return false;
    }

    /**
     * 是否使用 阴影
     *
     * @return
     */
    protected boolean setUseShadowView() {
        return false;
    }

    /**
     * 用户是否有登录密码
     *
     * @return
     */
    protected boolean userHasPassword() {
        if (mPresenter == null) {
            return false;
        }
        return mPresenter.userHasPassword();
    }

    /**
     * 是否使用 阴影点击事件
     *
     * @return
     */
    protected boolean setNeedShadowViewClick() {
        return true;
    }

    /**
     * 是否使用 评论界面
     *
     * @return
     */
    protected boolean setUseInputCommentView() {
        return false;
    }

    /**
     * @return 是否需要中心加载动画，对应  hideCenterLoading()
     */
    protected boolean setUseCenterLoadingAnimation() {
        return true;
    }

    /**
     * 状态栏默认为灰色
     * 支持小米、魅族以及 6.0 以上机型
     *
     * @return
     */
    protected boolean setStatusbarGrey() {
        return true;
    }

    /**
     * 状态栏是否可用
     *
     * @return 默认不可用
     */
    protected boolean setUseSatusbar() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * 设置是否需要添加和状态栏等高的占位 view
     *
     * @return
     */
    protected boolean setUseStatusView() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }


    /**
     * 是否显示toolbar,默认显示
     */
    protected boolean showToolbar() {
        return true;
    }

    /**
     * @return 是否需要显示音乐播放图标
     */
    protected boolean needMusicWindowView() {
        return true;
    }

    /**
     * 获取toolbar的布局文件,如果需要返回自定义的toolbar布局，重写该方法；否则默认返回缺省的toolbar
     */
    protected int getToolBarLayoutId() {
        return DEFAULT_TOOLBAR;
    }

    /**
     * @return toolbar 背景
     */
    protected int setToolBarBackgroud() {
        return DEFAULT_TOOLBAR_BACKGROUD_COLOR;
    }

    /**
     * 是否显示分割线,默认显示
     */
    protected boolean showToolBarDivider() {
        return false;
    }

    /**
     * 音乐悬浮窗是否正在显示
     */
    protected void musicWindowsStatus(final boolean isShow) {
        WindowUtils.changeToBlackIcon();
        final View view = getRightViewOfMusicWindow();
        if (getRightViewOfMusicWindow() != null) {
            if (view != null && isShow && !rightViewHadTranslated) {
                // 向左移动一定距离
                int rightX = ConvertUtils.dp2px(getContext(), 24) + ConvertUtils.dp2px(getContext(), 10);
                view.setTag(rightX);
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight() + rightX, view
                        .getPaddingBottom());
                rightViewHadTranslated = true;
            }
        }
    }

    /**
     * @return 音乐图标显示需要占位的 View, View 会左移，留出音乐图标的位置
     */
    protected View getRightViewOfMusicWindow() {
        return mToolbarRight;
    }

    /**
     * 是否需要中心加载弹窗
     *
     * @return
     */
    protected boolean needCenterLoadingDialog() {
        return false;
    }

    /**
     * 是否显示分割线,默认显示
     */
    protected int setToolBarDividerColor() {
        return DEFAULT_DIVIDER_COLOR;
    }

    /**
     * 获取toolbar下方的布局文件
     */
    protected abstract int getBodyLayoutId();

    /**
     * 初始化toolbar布局,如果进行了自定义toolbar布局，就应该重写该方法
     */
    protected void initDefaultToolBar(View toolBarContainer) {
        toolBarContainer.setBackgroundResource(setToolBarBackgroud());
        mToolbarLeft = (TextView) toolBarContainer.findViewById(R.id.tv_toolbar_left);
        mToolbarRight = (TextView) toolBarContainer.findViewById(R.id.tv_toolbar_right);
        mToolbarRightLeft = (TextView) toolBarContainer.findViewById(R.id.tv_toolbar_right_left);
        mToolbarCenter = (TextView) toolBarContainer.findViewById(R.id.tv_toolbar_center);
        mIvRefresh = (ImageView) toolBarContainer.findViewById(R.id.iv_refresh);
        // 如果标题为空，就隐藏它
        mToolbarCenter.setVisibility(TextUtils.isEmpty(setCenterTitle()) ? View.GONE : View.VISIBLE);
        mToolbarCenter.setText(setCenterTitle());
        mToolbarLeft.setVisibility(TextUtils.isEmpty(setLeftTitle()) && setLeftImg() == 0 ? View.GONE : View.VISIBLE);
        mToolbarLeft.setText(setLeftTitle());
        mToolbarRight.setVisibility(TextUtils.isEmpty(setRightTitle()) && setRightImg() == 0 ? View.GONE : View.VISIBLE);
        mToolbarRight.setText(setRightTitle());
        mToolbarRightLeft.setVisibility(TextUtils.isEmpty(setRightLeftTitle()) && setRightLeftImg() == 0 ? View.GONE : View.VISIBLE);
        mToolbarRightLeft.setText(setRightLeftTitle());

        setToolBarLeftImage(setLeftImg());
        setToolBarRightImage(setRightImg());
        setToolBarRightLeftImage(setRightLeftImg());
        RxView.clicks(mToolbarLeft)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        setLeftClick();
                    }
                });
        RxView.clicks(mToolbarRight)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        setRightClick();
                    }
                });

        RxView.clicks(mToolbarRightLeft)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        setRightLeftClick();
                    }
                });
        RxView.clicks(mToolbarCenter)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        setCenterClick();
                    }
                });
    }

    /**
     * set ToolBar left image
     *
     * @param resImg image resourse
     */
    protected void setToolBarLeftImage(int resImg) {
        mToolbarLeft.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), resImg), null, null, null);
    }

    /**
     * set ToolBar left image
     *
     * @param resImg image resourse
     * @param color  use color
     */
    protected void setToolBarLeftImage(int resImg, int color) {
        mToolbarLeft.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), resImg, color), null, null, null);
    }

    /**
     * set ToolBar left image
     *
     * @param resImg image resourse
     */
    protected void setToolBarRightImage(int resImg) {
        mToolbarRight.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), resImg), null);
    }

    protected void setToolBarRightLeftImage(int resImg) {
        mToolbarRightLeft.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), resImg), null);
    }

    protected void setToolBarRightImage(int resImg, int color) {
        mToolbarRight.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), resImg, color), null);
    }

    protected void setToolBarRightLeftImage(int resImg, int color) {
        mToolbarRightLeft.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), resImg, color), null);
    }

    /**
     * 设置中间的标题
     */
    protected String setCenterTitle() {
        return "";
    }

    /**
     * 设置左边的标题
     */
    protected String setLeftTitle() {
        return "";
    }

    /**
     * 显示右上角的加载动画
     */
    protected void showLeftTopLoading() {
        mIvRefresh.setVisibility(View.VISIBLE);
        ((AnimationDrawable) mIvRefresh.getDrawable()).start();
    }

    /**
     * 隐藏右上角的加载动画
     */
    protected void hideLeftTopLoading() {
        mIvRefresh.setVisibility(View.GONE);
        ((AnimationDrawable) mIvRefresh.getDrawable()).stop();
    }

    /**
     * 设置 Toolbar 左边控件的文字颜色
     *
     * @param resId
     */
    protected void setLeftTextColor(@ColorRes int resId) {
        mToolbarLeft.setTextColor(ContextCompat.getColor(getContext(), resId));
    }

    /**
     * 设置右边的标题
     */
    protected String setRightTitle() {
        return "";
    }

    /**
     * 设置右边靠左边那个的标题
     */
    protected String setRightLeftTitle() {
        return "";
    }

    /**
     * 设置左边的图片
     */
    protected int setLeftImg() {
        return DEFAULT_TOOLBAR_LEFT_IMG;
    }

    /**
     * 设置右边的图片
     */
    protected int setRightImg() {
        return 0;
    }

    /**
     * 设置右边靠左边那个的图片
     */
    protected int setRightLeftImg() {
        return 0;
    }

    /**
     * 设置左边的点击事件，默认为关闭activity，有必要重写该方法
     */
    protected void setLeftClick() {
        DeviceUtils.hideSoftKeyboard(mActivity, mRootView);
        getActivity().finish();
    }

    /**
     * 设置右边的点击时间，有必要重写该方法
     */
    protected void setRightClick() {
    }

    /**
     * 设置右边的点击时间，有必要重写该方法
     */
    protected void setRightLeftClick() {
    }

    /**
     * 中间文本点击事件回调，需要的时候子类实现
     */
    protected void setCenterClick() {
    }

    /**
     * 根据toolbar的背景设置它的文字颜色
     */
    protected void setToolBarTextColor() {
        // 如果toolbar背景是白色的，就将文字颜色设置成黑色
        if (showToolbar() && ContextCompat.getColor(getContext(), setToolBarBackgroud()) == Color.WHITE) {
            mToolbarCenter.setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_content));
            mToolbarRight.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.selector_text_color));
            mToolbarRightLeft.setTextColor(ContextCompat.getColorStateList(getContext(), R.color.selector_text_color));
            mToolbarLeft.setTextColor(ContextCompat.getColor(getContext(), getLeftTextColor()));
        }
    }

    protected int getLeftTextColor() {
        return R.color.important_for_content;
    }

    /**
     * 设置 title 的文字颜色
     *
     * @param resId color  resource id
     */
    protected void setCenterTextColor(@ColorRes int resId) {
        mToolbarCenter.setTextColor(ContextCompat.getColor(getContext(), resId));
    }

    /**
     * 设置右侧文字
     *
     * @param rightText 设置右侧文字 ，文字内容
     */
    protected void setRightText(String rightText) {
        changeText(mToolbarRight, rightText);
    }

    /**
     * 设置左侧文字内容
     *
     * @param leftText 左侧文字内容
     */
    protected void setLeftText(String leftText) {
        changeText(mToolbarLeft, leftText);
    }

    /**
     * 设置中间文字内容
     *
     * @param centerText 中间文字内容
     */
    protected void setCenterText(String centerText) {
        changeText(mToolbarCenter, centerText);
    }

    /**
     * 文本修改方法
     *
     * @param view   需要修改的文本控件
     * @param string 文本内容
     */
    private void changeText(TextView view, String string) {
        if (TextUtils.isEmpty(string)) {
            view.setVisibility(View.GONE);
        } else {
            view.setText(string);
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置状态栏占位图背景色
     *
     * @param resId
     */
    public void setStatusPlaceholderViewBackgroundColor(int resId) {
        if (mStatusPlaceholderView != null) {
            mStatusPlaceholderView.setBackgroundColor(resId);
        }
    }

    /**
     * 设置状态栏占位图背景色
     *
     * @param resId
     */
    public void setStatusPlaceholderViewBackground(int resId) {
        if (mStatusPlaceholderView != null) {
            mStatusPlaceholderView.setBackgroundResource(resId);
        }
    }

    /**
     * 登录跳转
     */
    private void goLogin() {
        //创建一个隐式的 Intent 对象，
        Intent intent = new Intent();
        intent.setAction(mActivity.getPackageName() + ".intent.action.LOGIN");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("bundle_tourist_login", true);
        intent.setType("text/plain");
        // Verify that the intent will resolve to an activity
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, 0);
        }
    }

    /**
     * 返回颜色
     *
     * @param resId
     * @return
     */
    protected int getColor(int resId) {
        return getResources().getColor(resId);
    }


    /**
     * 显示删除二次提示弹框
     */
    protected void showDeleteTipPopupWindow(String tipStr, final ActionPopupWindow.ActionPopupWindowItem1ClickListener listener, boolean
            createEveryTime) {
        if (TextUtils.isEmpty(tipStr)) {
            return;
        }
        if (mDeleteTipPopupWindow == null || createEveryTime) {
            mDeleteTipPopupWindow = ActionPopupWindow.builder()
                    .item1Str(tipStr)
                    .item1Color(ContextCompat.getColor(getContext(), R.color.important_for_note))
                    .bottomStr(getString(R.string.cancel))
                    .isOutsideTouch(true)
                    .isFocus(true)
                    .backgroundAlpha(POPUPWINDOW_ALPHA)
                    .with(getActivity())
                    .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                        @Override
                        public void onItemClicked() {
                            mDeleteTipPopupWindow.dismiss();
                            if (listener != null) {
                                listener.onItemClicked();
                            }
                        }
                    })
                    .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                        @Override
                        public void onItemClicked() {
                            mDeleteTipPopupWindow.hide();
                        }
                    }).build();
        }
        mDeleteTipPopupWindow.show();

    }

    /**
     * 限制说明提示弹框
     */
    protected void showAuditTipPopupWindow(String tipStr) {
        mIntroducePop = ActionPopupWindow.builder()
                .item1Str(getString(R.string.explain))
                .desStr(tipStr)
                .bottomStr(getString(R.string.i_know))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mIntroducePop.hide();
                    }
                }).build();
        mIntroducePop.show();
    }

    /**
     * 默认 toolbar 的标题文字左右间距
     *
     * @param resId
     */
    protected void setToolbarCenterSmallMargin(@DimenRes Integer resId) {
        try {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mToolbarCenter.getLayoutParams();
            if (resId == null) {
                params.leftMargin = getResources().getDimensionPixelOffset(R.dimen.toolbar_center_margin_small_60dp);
                params.rightMargin = getResources().getDimensionPixelOffset(R.dimen.toolbar_center_margin_small_60dp);
            } else {
                params.leftMargin = getResources().getDimensionPixelOffset(resId);
                params.rightMargin = getResources().getDimensionPixelOffset(resId);
            }
            mToolbarCenter.setLayoutParams(params);
        } catch (Exception ignore) {

        }
    }

    /**
     * Fragment 销毁时控件销毁周期
     */
    @Override
    public void onDestroyView() {
        if (mSnackBar != null) {
            mSnackBar.setCallback(null);
            if (mSnackBar.isShownOrQueued()) {
                mSnackBar.dismiss();
            }
            mSnackBar = null;
        }
        if (mStatusbarSupport != null && !mStatusbarSupport.isUnsubscribed()) {
            mStatusbarSupport.unsubscribe();
        }
        if (mViewTreeSubscription != null && !mViewTreeSubscription.isUnsubscribed()) {
            mViewTreeSubscription.unsubscribe();
        }
        dismissPop(mDeleteTipPopupWindow);
        dismissPop(mIntroducePop);
        super.onDestroyView();
    }

    protected void showInputPsdView(boolean show) {
        if (setUseShadowView() && setUseInputPsdView()) {
            if (!show) {
                if (mIlvPassword.trySetVisibility(View.GONE)) {
                    mIlvPassword.setVisibility(View.GONE);
                    mVShadow.setVisibility(View.GONE);
                    DeviceUtils.hideSoftKeyboard(getActivity(), mIlvPassword.getEtContent());
                }
            } else {
                if (userHasPassword()) {
                    mIlvPassword.getEtContent().requestFocus();
                    mIlvPassword.setVisibility(View.VISIBLE);
                    mVShadow.setVisibility(View.VISIBLE);
                    ExcutorUtil.startRunInNewThread(new Runnable() {
                        @Override
                        public void run() {
                            mIlvPassword.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (getActivity() != null) {
                                        DeviceUtils.showSoftKeyboard(getActivity(), mIlvPassword.getEtContent());
                                    }
                                }
                            }, 100);
                        }
                    });
                }
            }
        }
    }

    /**
     * 取消 pop
     *
     * @param popupWindow
     */
    protected void dismissPop(PopupWindow popupWindow) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
}
