package com.zhiyicx.baseproject.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.R;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 问题详情页底部工具栏
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public class QuestionDetailMenuView extends FrameLayout {
    public static final int DEFAULT_RESOURES_ID = -1; // 默认 id ，当子类使用默认 id 时，进行占位判断
    // item 数量
    private static final int ITEM_NUMS_MAX = 5;
    public static final int ITEM_POSITION_0 = 0;
    public static final int ITEM_POSITION_1 = 1;
    public static final int ITEM_POSITION_2 = 2;
    public static final int ITEM_POSITION_3 = 3;
    public static final int ITEM_POSITION_4 = 4;

    protected View mLlQuestionDetailComment;
    protected View mLlQuestionDetailShare;
    protected View mLlQuestionDetailEdit;
    protected View mLlQuestionDetailCollection;
    protected View mLlQuestionDetailMore;

    protected ImageView mIvQuestionDetailComment;
    protected ImageView mIvQuestionDetailShare;
    protected ImageView mIvQuestionDetailEdit;
    protected ImageView mIvQuestionDetailCollection;
    protected ImageView mIvQuestionDetailMore;

    protected TextView mTvQuestionDetailComment;
    protected TextView mTvQuestionDetailShare;
    protected TextView mTvQuestionDetailEdit;
    protected TextView mTvQuestionDetailCollection;
    protected TextView mTvQuestionDetailMore;

    private OnItemClickListener mOnItemListener;


    protected
    @DrawableRes
    int[] mImageNormalResourceIds = new int[]{
            R.mipmap.home_ico_comment_normal,
            R.mipmap.detail_ico_share_normal,
            R.mipmap.detail_ico_edit_normal,
            R.mipmap.detail_ico_good_uncollect,
            R.mipmap.home_ico_more
    };// 图片 ids 正常状态
    protected
    @DrawableRes
    int[] mImageCheckedResourceIds = new int[]{
            R.mipmap.home_ico_comment_normal,
            R.mipmap.detail_ico_share_normal,
            R.mipmap.detail_ico_edit_normal,
            R.mipmap.detail_ico_collect,
            R.mipmap.home_ico_more
    };// 图片 ids 选中状态
    protected
    @StringRes
    int[] mTexts = new int[]{
            R.string.comment,
            R.string.share,
            R.string.qa_detail_edit,
            R.string.collect,
            R.string.more
    };// 文字 ids

    protected
    @ColorRes
    int mTextNormalColor = R.color.normal_for_disable_button_text;// 正常文本颜色
    protected
    @ColorRes
    int mTextCheckedColor = R.color.normal_for_disable_button_text;// 选中文本颜色


    public QuestionDetailMenuView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public QuestionDetailMenuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public QuestionDetailMenuView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_question_detail_menu, this);
        mLlQuestionDetailEdit = findViewById(R.id.ll_dynamic_detail_like);
        mLlQuestionDetailComment = findViewById(R.id.ll_dynamic_detail_comment);
        mLlQuestionDetailShare = findViewById(R.id.ll_dynamic_detail_share);
        mLlQuestionDetailCollection = findViewById(R.id.ll_question_detail_collection);
        mLlQuestionDetailMore = findViewById(R.id.ll_dynamic_detail_more);

        mIvQuestionDetailEdit = (ImageView) findViewById(R.id.iv_dynamic_detail_like);
        mIvQuestionDetailComment = (ImageView) findViewById(R.id.iv_dynamic_detail_comment);
        mIvQuestionDetailShare = (ImageView) findViewById(R.id.iv_dynamic_detail_share);
        mIvQuestionDetailCollection = (ImageView) findViewById(R.id.iv_question_detail_collection);
        mIvQuestionDetailMore = (ImageView) findViewById(R.id.iv_dynamic_detail_more);

        mTvQuestionDetailEdit = (TextView) findViewById(R.id.tv_dynamic_detail_like);
        mTvQuestionDetailComment = (TextView) findViewById(R.id.tv_dynamic_detail_comment);
        mTvQuestionDetailShare = (TextView) findViewById(R.id.tv_dynamic_detail_share);
        mTvQuestionDetailCollection = (TextView) findViewById(R.id.tv_question_detail_collection);
        mTvQuestionDetailMore = (TextView) findViewById(R.id.tv_dynamic_detail_more);
        initListener();
    }

    private void initListener() {
        RxView.clicks(mLlQuestionDetailEdit)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnItemListener != null) {
                            mOnItemListener.onItemClick((ViewGroup) mLlQuestionDetailEdit, mIvQuestionDetailEdit, ITEM_POSITION_0);
                        }
                    }
                });
        RxView.clicks(mLlQuestionDetailComment)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnItemListener != null) {
                            mOnItemListener.onItemClick((ViewGroup) mLlQuestionDetailComment, mIvQuestionDetailComment, ITEM_POSITION_1);
                        }
                    }
                });
        RxView.clicks(mLlQuestionDetailShare)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnItemListener != null) {
                            mOnItemListener.onItemClick((ViewGroup) mLlQuestionDetailShare, mIvQuestionDetailShare, ITEM_POSITION_2);
                        }
                    }
                });
        RxView.clicks(mLlQuestionDetailCollection)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnItemListener != null) {
                            mOnItemListener.onItemClick((ViewGroup) mLlQuestionDetailShare, mIvQuestionDetailShare, ITEM_POSITION_2);
                        }
                    }
                });
        RxView.clicks(mLlQuestionDetailMore)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnItemListener != null) {
                            mOnItemListener.onItemClick((ViewGroup) mLlQuestionDetailMore, mIvQuestionDetailMore, ITEM_POSITION_3);
                        }
                    }
                });
    }

    /**
     * 设置数据
     */
    public void setData() {
        int length = mImageNormalResourceIds.length;
        for (int i = 0; i < length; i++) { // 最多支持 4 个 Item 多余的数据丢弃
            if (i < ITEM_NUMS_MAX) {
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
     * 设置 item 状态
     *
     * @param isChecked     是否选中
     * @param postion       当前 item 的位置
     * @param isNeedSetText 是否需要设置文字内容
     */
    private void setItemIsChecked(boolean isChecked, int postion, boolean isNeedSetText) {

        switch (postion) {
            case ITEM_POSITION_0:
                setItemState(isChecked, mLlQuestionDetailComment, mIvQuestionDetailComment, mTvQuestionDetailComment, postion, isNeedSetText);
                break;
            case ITEM_POSITION_1:
                setItemState(isChecked, mLlQuestionDetailShare, mIvQuestionDetailShare, mTvQuestionDetailShare, postion, isNeedSetText);
                break;
            case ITEM_POSITION_2:
                setItemState(isChecked, mLlQuestionDetailEdit, mIvQuestionDetailEdit, mTvQuestionDetailEdit, postion, isNeedSetText);
                break;
            case ITEM_POSITION_3:
                setItemState(isChecked, mLlQuestionDetailCollection, mIvQuestionDetailCollection, mTvQuestionDetailCollection, postion, isNeedSetText);
                break;
            case ITEM_POSITION_4:
                setItemState(isChecked, mLlQuestionDetailMore, mIvQuestionDetailMore, mTvQuestionDetailMore, postion, isNeedSetText);
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
        if (mImageNormalResourceIds[position] == DEFAULT_RESOURES_ID) { // 当没有资源的时候是否需要占位置
            if (isNeedPlaceholder()) {
                viewParent.setVisibility(INVISIBLE);
            } else {
                viewParent.setVisibility(GONE);
            }
            return;
        }
        if (isChecked) {
            imageView.setImageResource(mImageCheckedResourceIds[position]);
            textView.setTextColor(ContextCompat.getColor(getContext(), mTextCheckedColor));
        } else {
            imageView.setImageResource(mImageNormalResourceIds[position]);
            textView.setTextColor(ContextCompat.getColor(getContext(), mTextNormalColor));
        }
        if (isNeedSetText) {
            textView.setText(getResources().getString(mTexts[position]));
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

    public interface OnItemClickListener {
        void onItemClick(ViewGroup parent, View v, int postion);
    }

    /**
     * 设置未选中情况下的图片
     */
    public void setImageNormalResourceIds(int[] normalResourceIds) {
        this.mImageNormalResourceIds = normalResourceIds;
        // 初始化所有的控件图片
        mIvQuestionDetailComment.setImageResource(normalResourceIds[0]);
        mIvQuestionDetailShare.setImageResource(normalResourceIds[1]);
        mIvQuestionDetailEdit.setImageResource(normalResourceIds[2]);
        mIvQuestionDetailCollection.setImageResource(normalResourceIds[3]);
        mIvQuestionDetailMore.setImageResource(normalResourceIds[4]);
    }

    /**
     * 设置选中情况下的图片
     */
    public void setImageCheckedResourceIds(int[] checkedResourceIds) {
        this.mImageCheckedResourceIds = checkedResourceIds;
    }

    /**
     * 设置按钮文字内容
     */
    public void setButtonText(int[] texts) {
        this.mTexts = texts;
        // 初始化所有的控件图片
        mTvQuestionDetailComment.setText(texts[0]);
        mTvQuestionDetailEdit.setText(texts[1]);
        mTvQuestionDetailShare.setText(texts[2]);
        mTvQuestionDetailCollection.setText(texts[3]);
        mTvQuestionDetailMore.setText(texts[4]);
    }


}
