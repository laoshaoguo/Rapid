package com.zhiyicx.baseproject.widget;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.R;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 动态列表工具栏
 * 相关文档{@see ThinksnsPlus/document/baseproject/DYNAMICMENU.md}
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public class DynamicListMenuView extends LinearLayout {
    public static final int DEFAULT_RESOURES_ID = -1; // 默认 id ，当子类使用默认 id 时，进行占位判断
    public static final int DEFAULT_RESOURES_ID_ = -2; // 默认 id ，当子类使用默认 id 时，进行占位判断
    // item 数量
    private static final int ITEM_NUMS_MAX = 4;
    private static final int ITEM_POSITION_0 = 0;
    private static final int ITEM_POSITION_1 = 1;
    private static final int ITEM_POSITION_2 = 2;
    private static final int ITEM_POSITION_3 = 3;

    protected View mLlDynamicListLike;
    protected View mLlDynamicListComment;
    protected View mLlDynamicListPageviews;
    protected View mFlDynamicListMore;

    protected ImageView mIvDynamicListLike;
    protected ImageView mIvDynamicListComment;
    protected ImageView mIvDynamicListShare;
    protected ImageView mIvDynamicListMore;

    protected TextView mTvDynamicListLike;
    protected TextView mTvDynamicListComment;
    protected TextView mTvDynamicListPageviewst;

    private OnItemClickListener mOnItemListener;
    protected Context mContext;

    protected
    @DrawableRes
    int[] mImageNormalResourceIds = new int[]{
            R.mipmap.home_ico_good_normal,
            R.mipmap.home_ico_comment_normal,
            R.mipmap.home_ico_eye_normal,
            R.mipmap.home_ico_more
    };// 图片 ids 正常状态
    protected
    @DrawableRes
    int[] mImageCheckedResourceIds = new int[]{
            R.mipmap.home_ico_good_high,
            R.mipmap.home_ico_comment_normal,
            R.mipmap.home_ico_eye_normal,
            R.mipmap.home_ico_more
    };// 图片 ids 选中状态
    protected
    @StringRes
    int[] mTextIds = new int[]{
            R.string.zero,
            R.string.zero,
            R.string.zero,
            R.string.zero,
    };// 文字 ids
    private
    String[] mText = new String[mTextIds.length];// 文字内容

    protected
    @ColorRes
    int mTextNormalColor = R.color.normal_for_disable_button_text;// 正常文本颜色
    protected
    @ColorRes
    int mTextCheckedColor = R.color.important_for_note;// 选中文本颜色

    public DynamicListMenuView(Context context) {
        super(context);
        init(context, null);
    }

    public DynamicListMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_dynamic_list_menu, this);
        mLlDynamicListLike = findViewById(R.id.ll_dynamic_list_like);
        mLlDynamicListComment = findViewById(R.id.ll_dynamic_list_comment);
        mLlDynamicListPageviews = findViewById(R.id.ll_dynamic_list_pageviews);
        mFlDynamicListMore = findViewById(R.id.fl_dynamic_list_more);

        mIvDynamicListLike = (ImageView) findViewById(R.id.iv_dynamic_list_like);
        mIvDynamicListComment = (ImageView) findViewById(R.id.iv_dynamic_list_comment);
        mIvDynamicListShare = (ImageView) findViewById(R.id.iv_dynamic_list_pageviews);
        mIvDynamicListMore = (ImageView) findViewById(R.id.iv_dynamic_list_more);

        mTvDynamicListLike = (TextView) findViewById(R.id.tv_dynamic_list_like);
        mTvDynamicListComment = (TextView) findViewById(R.id.tv_dynamic_list_comment);
        mTvDynamicListPageviewst = (TextView) findViewById(R.id.tv_dynamic_list_pageviews);
        initListener();
        setData();
        setOrientation(HORIZONTAL);
    }

    private void initListener() {
        RxView.clicks(mLlDynamicListLike)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnItemListener != null) {
                            mOnItemListener.onItemClick((ViewGroup) mLlDynamicListLike, mIvDynamicListLike, ITEM_POSITION_0);
                        }
                    }
                });
        RxView.clicks(mLlDynamicListComment)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnItemListener != null) {
                            mOnItemListener.onItemClick((ViewGroup) mLlDynamicListComment, mIvDynamicListComment, ITEM_POSITION_1);
                        }
                    }
                });
        RxView.clicks(mLlDynamicListPageviews)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnItemListener != null) {
                            mOnItemListener.onItemClick((ViewGroup) mLlDynamicListPageviews, mIvDynamicListShare, ITEM_POSITION_2);
                        }
                    }
                });
        RxView.clicks(mIvDynamicListMore)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnItemListener != null) {
                            mOnItemListener.onItemClick((ViewGroup) mFlDynamicListMore, mIvDynamicListMore, ITEM_POSITION_3);
                        }
                    }
                });
    }

    /**
     * 设置数据
     */
    protected void setData() {

        int length = mImageNormalResourceIds.length;
        for (int i = 0; i < length; i++) { // 最多支持 4 个 Item 多余的数据丢弃
            if (i < ITEM_NUMS_MAX) {
                mText[i] = mContext.getResources().getString(mTextIds[i]);
                setItemIsChecked(false, i, true);
            } else {
                break;
            }
        }

    }

    /**
     * 当 item 不足{ ITEM_NUMS_MAX } 时 是否需要占位置
     *
     * @return
     */
    protected boolean isNeedPlaceholder() {
        return false;
    }

    /**
     * 设置 item 状态
     *
     * @param isChecked 是否选中
     * @param postion   当前 item 的位置
     */
    public void setItemIsChecked(boolean isChecked, int postion) {
        setItemIsChecked(isChecked, postion, false);
    }

    /**
     * 设置 item text 内容与状态
     *
     * @param string  当前内容
     * @param postion 当前 item 的位置
     */
    public void setItemTextAndStatus(String string, boolean isChecked, int postion) {
        if (postion >= ITEM_NUMS_MAX) {
            throw new IllegalArgumentException("postion is out of index");
        }
        mText[postion] = string;
        setItemIsChecked(isChecked, postion, true);
    }

    public void setAdvertItemTextAndStatus(String string, boolean isChecked, int postion) {
        if (postion >= ITEM_NUMS_MAX) {
            throw new IllegalArgumentException("postion is out of index");
        }
        mText[postion] = string;
        setAdvertItemIsChecked(isChecked, postion, true, false);
    }

    /**
     * 设置 item 可见
     *
     * @param position
     * @param visibility
     */
    public void setItemPositionVisiable(int position, int visibility) {

        switch (position) {
            case ITEM_POSITION_0:
                mLlDynamicListLike.setVisibility(visibility);
                break;
            case ITEM_POSITION_1:
                mLlDynamicListComment.setVisibility(visibility);

                break;
            case ITEM_POSITION_2:
                mLlDynamicListPageviews.setVisibility(visibility);
                break;
            case ITEM_POSITION_3:
                mFlDynamicListMore.setVisibility(visibility);
                break;
            default:
        }
    }

    /**
     * 设置 item 状态
     *
     * @param isChecked     是否选中
     * @param postion       当前 item 的位置
     * @param isNeedSetText 是否需要设置文字内容
     */
    private void setItemIsChecked(boolean isChecked, int postion, boolean isNeedSetText) {

        switch (postion) {
            case ITEM_POSITION_0:
                setItemState(isChecked, mLlDynamicListLike, mIvDynamicListLike, mTvDynamicListLike, postion, isNeedSetText);
                break;
            case ITEM_POSITION_1:
                setItemState(isChecked, mLlDynamicListComment, mIvDynamicListComment, mTvDynamicListComment, postion, isNeedSetText);

                break;
            case ITEM_POSITION_2:
                setItemState(isChecked, mLlDynamicListPageviews, mIvDynamicListShare, mTvDynamicListPageviewst, postion, isNeedSetText);
                break;
            case ITEM_POSITION_3:
                setItemState(isChecked, mFlDynamicListMore, mIvDynamicListMore, null, postion, isNeedSetText);
                break;
            default:
        }
    }

    private void setAdvertItemIsChecked(boolean isChecked, int postion, boolean isNeedSetText, boolean isNeedImage) {

        switch (postion) {
            case ITEM_POSITION_0:
                setAdvertItemState(isChecked, mLlDynamicListLike, mIvDynamicListLike, mTvDynamicListLike,
                        postion, isNeedSetText, isNeedImage);
                break;
            case ITEM_POSITION_1:
                setAdvertItemState(isChecked, mLlDynamicListComment, mIvDynamicListComment, mTvDynamicListComment, postion, isNeedSetText, isNeedImage);

                break;
            case ITEM_POSITION_2:
                setAdvertItemState(isChecked, mLlDynamicListPageviews, mIvDynamicListShare, mTvDynamicListPageviewst, postion, isNeedSetText, isNeedImage);
                break;
            case ITEM_POSITION_3:
                setAdvertItemState(isChecked, mFlDynamicListMore, mIvDynamicListMore, null, postion, isNeedSetText, isNeedImage);
                break;
            default:
        }

    }

    /**
     * 设置 item 状态
     *
     * @param isChecked  是否选中
     * @param viewParent 当前处理的控件容器
     * @param imageView  当前处理的 imageview
     * @param textView   当前处理的 textview
     * @param position   当前 item 的位置
     */
    private void setItemState(boolean isChecked, View viewParent, ImageView imageView, TextView textView, int position, boolean isNeedSetText) {
        if (mImageNormalResourceIds[position] == DEFAULT_RESOURES_ID && viewParent != null) { // 当没有资源的时候是否需要占位置
            if (isNeedPlaceholder()) {
                viewParent.setVisibility(INVISIBLE);
            } else {
                viewParent.setVisibility(GONE);
            }
            return;
        }
        if (isChecked) {
            if (imageView != null) {
                imageView.setImageResource(mImageCheckedResourceIds[position]);
            }
            if (textView != null) {
                textView.setTextColor(ContextCompat.getColor(getContext(), mTextCheckedColor));
            }
        } else {
            if (imageView != null) {
                imageView.setImageResource(mImageNormalResourceIds[position]);
            }
            if (textView != null) {
                textView.setTextColor(ContextCompat.getColor(getContext(), mTextNormalColor));
            }
        }
        if (textView != null && isNeedSetText && mText[position] != null) {
            textView.setText(mText[position]);
        }
    }

    private void setAdvertItemState(boolean isChecked, View viewParent, ImageView imageView, TextView textView,
                                    int position, boolean isNeedSetText, boolean isNeedImage) {
        if (mImageNormalResourceIds[position] == DEFAULT_RESOURES_ID && viewParent != null) { // 当没有资源的时候是否需要占位置
            if (isNeedPlaceholder()) {
                viewParent.setVisibility(INVISIBLE);
            } else {
                viewParent.setVisibility(GONE);
            }
            return;
        }
        if (!isNeedImage) {
            imageView.setVisibility(GONE);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(textView.getLayoutParams());
        params.gravity = Gravity.CENTER;
        params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        textView.setLayoutParams(params);
        textView.setBackgroundResource(R.drawable.advert_bg);
        if (isChecked) {
            if (imageView != null) {
                imageView.setImageResource(mImageCheckedResourceIds[position]);
            }
            if (textView != null) {
                textView.setTextColor(ContextCompat.getColor(getContext(), mTextCheckedColor));
            }
        } else {
            if (imageView != null) {
                imageView.setImageResource(mImageNormalResourceIds[position]);
            }
            if (textView != null) {
                textView.setTextColor(ContextCompat.getColor(getContext(), mTextNormalColor));
            }
        }
        if (textView != null && isNeedSetText && mText[position] != null) {
            textView.setText(mText[position]);
        }
    }

    /**
     * 设置喜欢点击监听
     *
     * @param listener 喜欢监听器
     */
    public void setItemOnClick(OnItemClickListener listener) {
        mOnItemListener = listener;
    }

    /**
     * 设置未选中情况下的图片
     */
    public void setImageNormalResourceIds(int[] normalResourceIds) {
        if (normalResourceIds == null) {
            return;
        }
        this.mImageNormalResourceIds = normalResourceIds;

        // 初始化所有的控件图片
        mIvDynamicListLike.setImageResource(normalResourceIds[0]);
        mIvDynamicListComment.setImageResource(normalResourceIds[1]);
        mIvDynamicListShare.setImageResource(normalResourceIds[2]);
        mIvDynamicListMore.setImageResource(normalResourceIds[3]);
    }

    public void setMoreButtonRightPadding(int i) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        params.rightMargin = 0;
        mIvDynamicListMore.setPadding(mIvDynamicListMore.getPaddingLeft(), mIvDynamicListMore.getPaddingTop(),
                i, mIvDynamicListMore.getPaddingBottom());
    }

    /**
     * 设置选中情况下的图片
     */
    public void setImageCheckedResourceIds(int[] checkedResourceIds) {
        this.mImageCheckedResourceIds = checkedResourceIds;
    }

    public interface OnItemClickListener {
        void onItemClick(ViewGroup parent, View v, int postion);
    }

}
