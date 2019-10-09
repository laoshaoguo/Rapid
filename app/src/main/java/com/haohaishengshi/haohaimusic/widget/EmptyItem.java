package com.haohaishengshi.haohaimusic.widget;

import com.haohaishengshi.haohaimusic.R;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public abstract class EmptyItem<T> implements ItemViewDelegate<T> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_comment_empty;
    }

    @Override
    public abstract boolean isForViewType(T item, int position);

    @Override
    public void convert(ViewHolder holder, T baseListBean, T lastT, int position, int itemCounts) {
        EmptyView emptyView = holder.getView(R.id.comment_emptyview);
        emptyView.setNeedTextTip(false);
        emptyView.setErrorType(EmptyView.STATE_NODATA);
    }


}
