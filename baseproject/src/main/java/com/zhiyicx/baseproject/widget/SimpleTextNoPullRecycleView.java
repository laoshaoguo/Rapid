package com.zhiyicx.baseproject.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.common.widget.NoPullRecycleView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

/**
 * @Describe sinple text  nopull recycleview
 * @Author Jungle68
 * @Date 2017/3/6
 * @Contact master.jungle68@gmail.com
 */

public abstract class SimpleTextNoPullRecycleView<T> extends NoPullRecycleView {

    protected OnIitemClickListener mOnIitemClickListener;

    protected OnIitemLongClickListener mOnIitemLongClickListener;

    protected MultiItemTypeAdapter<T> mAdapter;

    public SimpleTextNoPullRecycleView(Context context) {
        super(context);
        init(null, -1);
    }

    public SimpleTextNoPullRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, -1);
    }

    public SimpleTextNoPullRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    protected void init(@Nullable AttributeSet attrs, int defStyle) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(linearLayoutManager);
        LinearDecoration linearDecoration = new LinearDecoration(0, getResources().getDimensionPixelSize(R.dimen.spacing_small), 0, 0);
        linearDecoration.setNeedLastDecoration(false);
        addItemDecoration(linearDecoration);
    }

    public void setOnIitemClickListener(OnIitemClickListener onIitemClickListener) {
        mOnIitemClickListener = onIitemClickListener;
    }

    public void setOnIitemLongClickListener(OnIitemLongClickListener onIitemLongClickListener) {
        mOnIitemLongClickListener = onIitemLongClickListener;
    }

    /**
     * set data
     *
     * @param data
     */
    public void setData(List<T> data) {
        initData(data);
    }

    /**
     * refresh
     */
    public void refresh() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * refresh curren item
     *
     * @param position
     */
    public void refresh(int position) {
        mAdapter.notifyItemChanged(position);
    }

    protected void initData(final List<T> data) {

        mAdapter = new CommonAdapter<T>(getContext(), R.layout.item_simple_text_comment, data) {
            @Override
            protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, T t, final int position) {
                convertData(holder, t, position);
            }
        };
        setAdapter(mAdapter);
    }

    protected abstract void convertData(com.zhy.adapter.recyclerview.base.ViewHolder holder, T t, int position);


    public interface OnIitemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnIitemLongClickListener {
        void onItemLongClick(View view, int position);
    }
}
