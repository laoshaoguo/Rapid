package com.zhiyicx.baseproject.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
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
 * @Describe 动态详情页底部工具栏
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public class DynamicDetailMenuView extends FrameLayout {
    public static final int DEFAULT_RESOURES_ID = -1; // 默认 id ，当子类使用默认 id 时，进行占位判断
    // item 数量
    private static final int ITEM_NUMS_MAX = 6;
    public static final int ITEM_POSITION_0 = 0;
    public static final int ITEM_POSITION_1 = 1;
    public static final int ITEM_POSITION_2 = 2;
    public static final int ITEM_POSITION_3 = 3;
    public static final int ITEM_POSITION_4 = 4; // 编辑
    public static final int ITEM_POSITION_5 = 5; // 收藏

    protected View mLlDynamicDetailLike;
    protected View mLlDynamicDetailComment;
    protected View mLlDynamicDetailShare;
    protected View mLlDynamicDetailMore;
    protected View mLlDynamicDetailEdit;
    protected View mLlDynamicDetailCollection;

    protected ImageView mIvDynamicDetailLike;
    protected ImageView mIvDynamicDetailComment;
    protected ImageView mIvDynamicDetailShare;
    protected ImageView mIvDynamicDetailMore;
    protected ImageView mIvDynamicDetailEdit;
    protected ImageView mIvDynamicDetailCollection;

    protected TextView mTvDynamicDetailLike;
    protected TextView mTvDynamicDetailComment;
    protected TextView mTvDynamicDetailShare;
    protected TextView mTvDynamicDetailMore;
    protected TextView mTvDynamicDetailEdit;
    protected TextView mTvDynamicDetailCollection;

    private OnItemClickListener mOnItemListener;
    private boolean mIsQuestion = false;
    private boolean mIsAnswer = false;
    protected
    @DrawableRes
    int[] mImageNormalResourceIds = new int[]{
            R.mipmap.home_ico_good_normal,
            R.mipmap.home_ico_comment_normal,
            R.mipmap.detail_ico_share_normal,
            R.mipmap.home_ico_more,
            R.mipmap.detail_ico_edit_normal,
            R.mipmap.detail_ico_good_uncollect
    };// 图片 ids 正常状态
    protected
    @DrawableRes
    int[] mImageCheckedResourceIds = new int[]{
            R.mipmap.home_ico_good_high,
            R.mipmap.home_ico_comment_normal,
            R.mipmap.detail_ico_share_normal,
            R.mipmap.home_ico_more,
            R.mipmap.detail_ico_edit_normal,
            R.mipmap.detail_ico_collect
    };// 图片 ids 选中状态
    protected
    @StringRes
    int[] mTexts = new int[]{
            R.string.like,
            R.string.comment,
            R.string.share,
            R.string.more,
            R.string.qa_detail_edit,
            R.string.collect
    };// 文字 ids

    protected
    @ColorRes
    int mTextNormalColor = R.color.normal_for_disable_button_text;// 正常文本颜色
    protected
    @ColorRes
    int mTextCheckedColor = R.color.normal_for_disable_button_text;// 选中文本颜色

    public DynamicDetailMenuView(Context context) {
        super(context);
        init(context, null);
    }

    public DynamicDetailMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_dynamic_detail_menu, this);
        mLlDynamicDetailLike = findViewById(R.id.ll_dynamic_detail_like);
        mLlDynamicDetailComment = findViewById(R.id.ll_dynamic_detail_comment);
        mLlDynamicDetailShare = findViewById(R.id.ll_dynamic_detail_share);
        mLlDynamicDetailMore = findViewById(R.id.ll_dynamic_detail_more);
        mLlDynamicDetailEdit = findViewById(R.id.ll_question_detail_edit);
        mLlDynamicDetailCollection = findViewById(R.id.ll_question_detail_collection);

        mIvDynamicDetailLike = (ImageView) findViewById(R.id.iv_dynamic_detail_like);
        mIvDynamicDetailComment = (ImageView) findViewById(R.id.iv_dynamic_detail_comment);
        mIvDynamicDetailShare = (ImageView) findViewById(R.id.iv_dynamic_detail_share);
        mIvDynamicDetailMore = (ImageView) findViewById(R.id.iv_dynamic_detail_more);
        mIvDynamicDetailEdit = (ImageView) findViewById(R.id.iv_question_detail_edit);
        mIvDynamicDetailCollection = (ImageView) findViewById(R.id.iv_question_detail_collection);

        mTvDynamicDetailLike = (TextView) findViewById(R.id.tv_dynamic_detail_like);
        mTvDynamicDetailComment = (TextView) findViewById(R.id.tv_dynamic_detail_comment);
        mTvDynamicDetailShare = (TextView) findViewById(R.id.tv_dynamic_detail_share);
        mTvDynamicDetailMore = (TextView) findViewById(R.id.tv_dynamic_detail_more);
        mTvDynamicDetailEdit = (TextView) findViewById(R.id.tv_question_detail_edit);
        mTvDynamicDetailCollection = (TextView) findViewById(R.id.tv_question_detail_collection);
        initListener();
    }

    private void initListener() {
        RxView.clicks(mLlDynamicDetailLike)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnItemListener != null) {
                            mOnItemListener.onItemClick((ViewGroup) mLlDynamicDetailLike, mIvDynamicDetailLike, ITEM_POSITION_0);
                        }
                    }
                });
        RxView.clicks(mLlDynamicDetailComment)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnItemListener != null) {
                            mOnItemListener.onItemClick((ViewGroup) mLlDynamicDetailComment, mIvDynamicDetailComment, ITEM_POSITION_1);
                        }
                    }
                });
        RxView.clicks(mLlDynamicDetailShare)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnItemListener != null) {
                            mOnItemListener.onItemClick((ViewGroup) mLlDynamicDetailShare, mIvDynamicDetailShare, ITEM_POSITION_2);
                        }
                    }
                });
        RxView.clicks(mLlDynamicDetailMore)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnItemListener != null) {
                            mOnItemListener.onItemClick((ViewGroup) mLlDynamicDetailMore, mIvDynamicDetailMore, ITEM_POSITION_3);
                        }
                    }
                });
        RxView.clicks(mLlDynamicDetailEdit)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnItemListener != null) {
                            mOnItemListener.onItemClick((ViewGroup) mLlDynamicDetailEdit, mIvDynamicDetailEdit, ITEM_POSITION_4);
                        }
                    }
                });
        RxView.clicks(mLlDynamicDetailCollection)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mOnItemListener != null) {
                            mOnItemListener.onItemClick((ViewGroup) mLlDynamicDetailCollection, mIvDynamicDetailCollection, ITEM_POSITION_5);
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
                setItemState(isChecked, mLlDynamicDetailLike, mIvDynamicDetailLike, mTvDynamicDetailLike, postion, isNeedSetText);
                break;
            case ITEM_POSITION_1:
                setItemState(isChecked, mLlDynamicDetailComment, mIvDynamicDetailComment, mTvDynamicDetailComment, postion, isNeedSetText);

                break;
            case ITEM_POSITION_2:
                setItemState(isChecked, mLlDynamicDetailShare, mIvDynamicDetailShare, mTvDynamicDetailShare, postion, isNeedSetText);
                break;
            case ITEM_POSITION_3:
                setItemState(isChecked, mLlDynamicDetailMore, mIvDynamicDetailMore, mTvDynamicDetailMore, postion, isNeedSetText);
                break;
            case ITEM_POSITION_4:
                setItemState(isChecked, mLlDynamicDetailEdit, mIvDynamicDetailEdit, mTvDynamicDetailEdit, postion, isNeedSetText);
                break;
            case ITEM_POSITION_5:
                setItemState(isChecked, mLlDynamicDetailCollection, mIvDynamicDetailCollection, mTvDynamicDetailCollection, postion, isNeedSetText);
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
            if (ITEM_POSITION_0 == position) {
                // 点赞文字特殊处理，红色
                textView.setTextColor(Color.RED);
            }
            if (ITEM_POSITION_4 == position) {
                // 点赞文字特殊处理，红色
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.themeColor));
            }
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
        mIvDynamicDetailLike.setImageResource(normalResourceIds[0]);
        mIvDynamicDetailComment.setImageResource(normalResourceIds[1]);
        mIvDynamicDetailShare.setImageResource(normalResourceIds[2]);
        mIvDynamicDetailMore.setImageResource(normalResourceIds[3]);
        if (mIsQuestion) {
            mIvDynamicDetailEdit.setImageResource(normalResourceIds[4]);
            mIvDynamicDetailCollection.setImageResource(normalResourceIds[5]);
        }
        if (mIsAnswer){
            mIvDynamicDetailEdit.setImageResource(normalResourceIds[4]);
        }
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
        mTvDynamicDetailLike.setText(texts[0]);
        mTvDynamicDetailComment.setText(texts[1]);
        mTvDynamicDetailShare.setText(texts[2]);
        mTvDynamicDetailMore.setText(texts[3]);
        if (mIsQuestion) {
            mTvDynamicDetailEdit.setText(texts[4]);
            mTvDynamicDetailCollection.setText(texts[5]);
        }
        if (mIsAnswer){
            mTvDynamicDetailEdit.setText(texts[4]);
        }
    }

    public void useEditView(boolean isNeed) {
        mLlDynamicDetailEdit.setVisibility(VISIBLE);
    }

    public void showQuestionTool(boolean isMine) {
        mIsQuestion = true;
        mLlDynamicDetailLike.setVisibility(GONE);
        mLlDynamicDetailEdit.setVisibility(isMine ? VISIBLE : GONE);
        mLlDynamicDetailCollection.setVisibility(GONE);
    }

    public void showAnswerTool(boolean isMine){
        mIsAnswer = true;
        mLlDynamicDetailEdit.setVisibility(!isMine ? VISIBLE : GONE);
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
                mLlDynamicDetailLike.setVisibility(visibility);
                break;
            case ITEM_POSITION_1:
                mLlDynamicDetailComment.setVisibility(visibility);

                break;
            case ITEM_POSITION_2:
                mLlDynamicDetailShare.setVisibility(visibility);
                break;
            case ITEM_POSITION_3:
                mLlDynamicDetailMore.setVisibility(visibility);
                break;
            case ITEM_POSITION_4:
                mLlDynamicDetailEdit.setVisibility(visibility);
                break;
            case ITEM_POSITION_5:
                mLlDynamicDetailCollection.setVisibility(visibility);
                break;
            default:
        }
    }
}
