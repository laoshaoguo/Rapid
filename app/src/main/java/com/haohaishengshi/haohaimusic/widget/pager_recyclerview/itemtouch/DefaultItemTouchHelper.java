/*
 * Copyright © Yolanda. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.haohaishengshi.haohaimusic.widget.pager_recyclerview.itemtouch;

/**
 * @author legendary_tym
 * @Title SpicyCommunity
 * @Package com.zycx.spicycommunity.projcode.quanzi.view.quanzidetail.itemtouch
 * @Description: TODO(用一句话描述该文件做什么)
 * @date: 2016-11-08 18:20
 */
public class DefaultItemTouchHelper extends TItemTouchHelper {

    private DefaultItemTouchHelpCallback itemTouchHelpCallback;

    public DefaultItemTouchHelper(
            DefaultItemTouchHelpCallback.OnItemTouchCallbackListener onItemTouchCallbackListener) {
        super(new DefaultItemTouchHelpCallback(onItemTouchCallbackListener) {
            @Override
            protected boolean canScaleOnLongTouch() {
                return false;
            }
        });
        itemTouchHelpCallback = (DefaultItemTouchHelpCallback) getCallback();
    }

    /**
     * 设置是否可以被拖拽
     *
     * @param canDrag 是true，否false
     */
    public void setDragEnable(boolean canDrag) {
        itemTouchHelpCallback.setDragEnable(canDrag);
    }

    /**
     * 设置是否可以被滑动
     *
     * @param canSwipe 是true，否false
     */
    public void setSwipeEnable(boolean canSwipe) {
        itemTouchHelpCallback.setSwipeEnable(canSwipe);
    }
}
