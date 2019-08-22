package com.haohaishengshi.haohaimusic.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.haohaishengshi.haohaimusic.R;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.List;

import static com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly.PlaybackManager.ORDERRANDOM;
import static com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly.PlaybackManager.ORDERSINGLE;

/**
 * @Author Jliuer
 * @Date 2017/02/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicListPopupWindow extends PopupWindow {

    private Activity mActivity;
    private View mParentView;
    private View mContentView;
    private boolean mIsOutsideTouch;
    private boolean mIsFocus;
    private float mAlpha;
    private List mDatas;
    private String mTitle;
    private String mCancle;
    private OnItemListener mItemClickListener;
    private int mWidth;
    private int mHeight;
    private int mItemLayout;
    private Drawable mBackgroundDrawable = new ColorDrawable(0x00000000);// 默认为透明;
    private CommonAdapter mAdapter;
    private TextView mTileTextView, mSizeTextView;

    private MusicListPopupWindow() {

    }

    private MusicListPopupWindow(Builder builder) {
        this.mActivity = builder.mActivity;
        this.mParentView = builder.mParentView;
        this.mIsOutsideTouch = builder.mIsOutsideTouch;
        this.mIsFocus = builder.mIsFocus;
        this.mAlpha = builder.mAlpha;
        this.mDatas = builder.mDatas;
        this.mTitle = builder.mTitle;
        this.mCancle = builder.mCancle;
        this.mWidth = builder.mWidth;
        this.mHeight = builder.mHeight;
        this.mItemLayout = builder.mItemLayout;
        this.mItemClickListener = builder.mItemClickListener;
        this.mAdapter = builder.mAdapter;
        initView();
    }

    private void initView() {
        initLayout();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight((int) (DeviceUtils.getScreenHeight(mActivity) * 0.6));
        setFocusable(mIsFocus);
        setOutsideTouchable(mIsOutsideTouch);
        setBackgroundDrawable(mBackgroundDrawable);
        setAnimationStyle(R.style.style_actionPopupAnimation);
        setContentView(mContentView);
    }

    private void initLayout() {
        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.ppw_for_list, null);
        RecyclerView recyclerView = (RecyclerView) mContentView.findViewById(R.id
                .tv_pop_list_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(mAdapter);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAlpha(1.0f);
            }
        });
        mTileTextView = (TextView) mContentView.findViewById(R.id.tv_pop_list_title);
        mSizeTextView = (TextView) mContentView.findViewById(R.id.tv_pop_list_size);
        mSizeTextView.setText(String.format(" (%d)", mDatas.size()));
        mContentView.findViewById(R.id.tv_pop_list_cancle).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setWindowAlpha(float alpha) {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = alpha;
        params.verticalMargin = 100;
        mActivity.getWindow().setAttributes(params);
    }

    public void show() {
        try {
            setWindowAlpha(mAlpha);
            showAtLocation(mParentView == null ? mContentView : mParentView, Gravity.BOTTOM, 0, 0);
        } catch (Exception e) {

        }
    }

    public void hide() {
        dismiss();
    }

    public static MusicListPopupWindow.Builder Builder() {
        return new MusicListPopupWindow.Builder();
    }

    public static final class Builder {
        private Activity mActivity;
        private View mParentView;
        private boolean mIsOutsideTouch = true;
        private boolean mIsFocus = true;
        private float mAlpha;
        private int mWidth = 0;
        private int mHeight = 0;
        private int mItemLayout = 0;
        private List mDatas;
        private String mTitle;
        private CommonAdapter mAdapter;
        private String mCancle;
        private OnItemListener mItemClickListener;

        private Builder() {
        }

        public MusicListPopupWindow.Builder with(Activity mActivity) {
            this.mActivity = mActivity;
            return this;
        }

        public MusicListPopupWindow.Builder adapter(CommonAdapter adapter) {
            this.mAdapter = adapter;
            return this;
        }

        public MusicListPopupWindow.Builder itemLayout(int itemLayout) {
            this.mItemLayout = itemLayout;
            return this;
        }

        public MusicListPopupWindow.Builder width(int width) {
            this.mWidth = width;
            return this;
        }

        public MusicListPopupWindow.Builder height(int height) {
            this.mHeight = height;
            return this;
        }

        public MusicListPopupWindow.Builder parentView(View parentView) {
            this.mParentView = parentView;
            return this;
        }

        public MusicListPopupWindow.Builder isOutsideTouch(boolean isOutsideTouch) {
            this.mIsOutsideTouch = isOutsideTouch;
            return this;
        }

        public MusicListPopupWindow.Builder iFocus(boolean isFocus) {
            this.mIsFocus = isFocus;
            return this;
        }

        public MusicListPopupWindow.Builder title(String title) {
            this.mTitle = title;
            return this;
        }

        public MusicListPopupWindow.Builder cancle(String cancle) {
            this.mCancle = cancle;
            return this;
        }

        public MusicListPopupWindow.Builder alpha(float alpha) {
            this.mAlpha = alpha;
            return this;
        }

        public MusicListPopupWindow.Builder data(List datas) {
            this.mDatas = datas;

            return this;
        }

        public MusicListPopupWindow.Builder itemListener(OnItemListener itemClickListener) {
            this.mItemClickListener = itemClickListener;
            return this;
        }

        public MusicListPopupWindow build() {
            return new MusicListPopupWindow(this);
        }
    }

    public CommonAdapter getAdapter() {
        return mAdapter;
    }

    public void dataChange(List datas) {
        this.mDatas = datas;
        this.mAdapter.notifyDataSetChanged();
    }

    public void dataChangeOne(int position) {
        this.mAdapter.notifyItemChanged(position);
    }

    public void setOrder(int order) {
        switch (order) {
            case ORDERRANDOM:
                mTileTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap
                        .music_ico_random_grey, 0, 0, 0);
                mTileTextView.setText(R.string.music_order_random);
                break;
            case ORDERSINGLE:
                mTileTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap
                        .music_ico_single_grey, 0, 0, 0);
                mTileTextView.setText(R.string.music_order_single);
                break;
            default:
                mTileTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap
                        .music_ico_inorder_grey, 0, 0, 0);
                mTileTextView.setText(R.string.music_order_nomal);
                break;
        }
    }

    public interface OnItemListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);
    }

}
