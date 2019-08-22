package com.zhiyicx.baseproject.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.R;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 空置页面或错误提示
 * @Author Jungle68
 * @Date 2017/2/7
 * @Contact master.jungle68@gmail.com
 */
public class EmptyView extends LinearLayout {

    // view 当前状态
    /**
     * default
     */
    public static final int STATE_DEFAULT = 0;
    /**
     * net error
     */
    public static final int STATE_NETWORK_ERROR = 1;
    /**
     * loading
     */
    public static final int STATE_NETWORK_LOADING = 2;
    /**
     * no data and clickable is false
     */
    public static final int STATE_NODATA = 3;
    /**
     * no data and clickable is true
     */
    public static final int STATE_NODATA_ENABLE_CLICK = 4;
    /**
     * hide this view
     */
    public static final int STATE_HIDE_LAYOUT = 5;


    private ProgressBar mAnimProgress;
    public ImageView mIvError;
    private TextView mTvError;
    private View mLlContent;

    private OnClickListener mOnClickListener;
    private int mErrorState;
    private boolean mClickEnable = true;

    private boolean mIsNeedClickLoadState = true;// 是否需要点击响应时自动 load 状态


    private boolean mIsNeedTextTip = true;// 是否需要文字提示
    private Context mContext;

    public EmptyView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        View view = View.inflate(mContext, R.layout.view_empty, this);
        mLlContent = view.findViewById(R.id.ll_content);
        mIvError = (ImageView) view.findViewById(R.id.iv_error_layout);
        mTvError = (TextView) view.findViewById(R.id.tv_error_layout);
        mAnimProgress = (ProgressBar) view.findViewById(R.id.pb_animation_bar);
        RxView.clicks(mLlContent)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mClickEnable) {
                            if (mIsNeedClickLoadState) {
                                setErrorType(STATE_NETWORK_LOADING);
                            }
                            if (mOnClickListener != null)
                                mOnClickListener.onClick(mLlContent);
                        }
                    }
                });
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE) {
            mErrorState = STATE_HIDE_LAYOUT;
        }
        super.setVisibility(visibility);
    }

    /**
     * 隐藏当前 view
     */
    public void dismiss() {
        mErrorState = STATE_HIDE_LAYOUT;
        setVisibility(View.GONE);
    }


    public int getErrorState() {
        return mErrorState;
    }

    /**
     * 设置错误提示信息内容
     *
     * @param msg 信息内容，not null
     */
    public void setErrorMessage(@NotNull String msg) {
        if (mIsNeedTextTip) {
            mTvError.setVisibility(VISIBLE);
            mTvError.setText(msg);
        }
    }

    /**
     * 当前是否在加载状态
     *
     * @return true, 当前处于加载状态
     */
    public boolean isLoading() {
        return mErrorState == STATE_NETWORK_LOADING;
    }

    /**
     * @return
     */
    public boolean isNeedClickLoadState() {
        return mIsNeedClickLoadState;
    }

    /**
     * 是否自动添加加载状态
     *
     * @param needClickLoadState
     */
    public void setNeedClickLoadState(boolean needClickLoadState) {
        mIsNeedClickLoadState = needClickLoadState;
    }

    public boolean isNeedTextTip() {
        return mIsNeedTextTip;
    }

    /**
     * 设置文字提示
     *
     * @param needTextTip
     */
    public void setNeedTextTip(boolean needTextTip) {
        mIsNeedTextTip = needTextTip;
    }

    /**
     * 设置提示图片
     *
     * @param resId 资源引用 id
     * @return true, 加载中
     */
    public void setErrorImag(@DrawableRes int resId) {
        mIvError.setVisibility(View.VISIBLE);
        mIvError.setImageResource(resId);
    }

    /**
     * 设置当前状态
     *
     * @param type 当前状态类型
     */
    public void setErrorType(int type) {
        setVisibility(View.VISIBLE);
        switch (type) {
            case STATE_NETWORK_ERROR:
                mErrorState = STATE_NETWORK_ERROR;
                mAnimProgress.setVisibility(View.GONE);
                setErrorImag(R.mipmap.img_default_internet);
                setErrorMessage(mContext.getString(R.string.err_net_not_work));
                mClickEnable = true;
                break;
            case STATE_NETWORK_LOADING:
                mErrorState = STATE_NETWORK_LOADING;
                mAnimProgress.setVisibility(View.VISIBLE);
                mIvError.setVisibility(View.GONE);
                setErrorMessage("");
                mClickEnable = false;
                break;
            case STATE_NODATA:
                mErrorState = STATE_NODATA;
                mAnimProgress.setVisibility(View.GONE);
                setErrorImag(R.mipmap.img_default_nothing);
                setErrorMessage(mContext.getString(R.string.no_data));
                mClickEnable = false;
                break;
            case STATE_HIDE_LAYOUT:
                setVisibility(View.GONE);
                mIvError.setVisibility(View.GONE);
                mAnimProgress.setVisibility(View.GONE);
                break;
            case STATE_NODATA_ENABLE_CLICK:
                mErrorState = STATE_NODATA_ENABLE_CLICK;
                mAnimProgress.setVisibility(View.GONE);
                setErrorImag(R.mipmap.img_default_nothing);
                setErrorMessage(mContext.getString(R.string.no_data));
                mClickEnable = true;
                break;
            default:
        }
    }


}

