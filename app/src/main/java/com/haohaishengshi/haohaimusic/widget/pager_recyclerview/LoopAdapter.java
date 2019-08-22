package com.haohaishengshi.haohaimusic.widget.pager_recyclerview;

import android.support.v7.widget.RecyclerView;

/**
 * @Author Jliuer
 * @Date 2017/02/16
 * @Email Jliuer@aliyun.com
 * @Description 修改 getItemCount 无限循环
 */
public class LoopAdapter<VH extends RecyclerView.ViewHolder>
        extends AdapterWrapper<VH> {

    public LoopAdapter(PagerRecyclerView viewPager, RecyclerView.Adapter<VH> adapter) {
        super(viewPager, adapter);
    }

    /**
     * 正确的 item 个数
     *
     * @return
     */
    public int getActualItemCount() {
        return super.getItemCount();
    }

    public int getActualItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public long getActualItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public int getItemCount() {
        if (getActualItemCount() > 0) {
            return Integer.MAX_VALUE;
        } else {
            return super.getItemCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getActualItemCount() > 0) {
            return super.getItemViewType(getActualPosition(position));
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        super.onBindViewHolder(holder, getActualPosition(position));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(getActualPosition(position));
    }

    /**
     * 正确的 position
     *
     * @param position
     * @return
     */
    public int getActualPosition(int position) {
        int actualPosition = position;
        if (getActualItemCount() > 0 && position >= getActualItemCount()) {
            actualPosition = position % getActualItemCount();
        }
        return actualPosition;
    }
}
