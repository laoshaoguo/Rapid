package com.haohaishengshi.haohaimusic.widget.pager_recyclerview.itemtouch;

import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * @author legendary_tym
 * @Title SpicyCommunity
 * @Package com.zycx.spicycommunity.projcode.quanzi.view.quanzidetail.itemtouch
 * @Description: 拖动的recyclerview 回调接口
 * @date: 2016-11-08 18:20
 */


public class TItemTouchHelper extends ItemTouchHelper {
    Callback mCallback;

    public TItemTouchHelper(Callback callback) {
        super(callback);
        this.mCallback = callback;
    }

    public Callback getCallback() {
        return mCallback;
    }
}
