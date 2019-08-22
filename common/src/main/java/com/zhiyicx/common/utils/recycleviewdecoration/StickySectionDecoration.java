package com.zhiyicx.common.utils.recycleviewdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;

import com.zhiyicx.common.R;

/**
 * @author Jliuer
 * @Date 17/12/08 15:34
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class StickySectionDecoration extends RecyclerView.ItemDecoration {

    private GroupInfoCallback mCallback;
    private int mHeaderHeight;
    private int mDividerHeight;


    //用来绘制Header上的文字
    private TextPaint mTextPaint;
    private Paint mPaint;
    private float mTextSize;
    private Paint.FontMetrics mFontMetrics;

    private float mTextOffsetX;
    private int groupId;
    private int groupIdT;

    public StickySectionDecoration(Context context, GroupInfoCallback callback) {
        this.mCallback = callback;
        mDividerHeight = context.getResources().getDimensionPixelOffset(R.dimen
                .header_divider_height);
        mHeaderHeight = context.getResources().getDimensionPixelOffset(R.dimen.header_height);
        mTextSize = context.getResources().getDimensionPixelOffset(R.dimen.header_textsize);

        mHeaderHeight = (int) Math.max(mHeaderHeight, mTextSize);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.parseColor("#999999"));
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mFontMetrics = mTextPaint.getFontMetrics();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#f4f5f5"));

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {

        int position = parent.getChildAdapterPosition(view);
        if (mCallback != null) {
            GroupInfo groupInfo = mCallback.getGroupInfo(position);
            // 上一个 分组
            GroupInfo lastGroupInfo = null;
            if (position > 0) {
                lastGroupInfo = mCallback.getGroupInfo(position - 1);
            }
            if (groupInfo == null) {
                return;
            }

            boolean isFirst = lastGroupInfo == null || lastGroupInfo.getGroupID() != groupInfo.getGroupID();

            //如果是组内的第一个则将间距撑开为一个Header的高度，或者就是普通的分割线高度
            if (groupInfo.isFirstViewInGroup() || isFirst) {
                outRect.top = mHeaderHeight;
            } else {
                outRect.top = mDividerHeight;
            }
            groupIdT = groupInfo.mGroupID;
        }
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (i == 0) {
                groupId = 0;
            }
            View view = parent.getChildAt(i);
            mTextOffsetX = view.getPaddingLeft();
            int index = parent.getChildAdapterPosition(view);


            if (mCallback != null) {

                GroupInfo groupinfo = mCallback.getGroupInfo(index);
                if (groupinfo != null) {
                    int left = parent.getPaddingLeft();
                    int right = parent.getWidth() - parent.getPaddingRight();
                    //屏幕上第一个可见的 ItemView 时，i == 0;
                    if (i != 0) {
                        //只有组内的第一个ItemView之上才绘制
                        if (groupinfo.isFirstViewInGroup() || groupinfo.mGroupID != groupId) {
                            int top = view.getTop() - mHeaderHeight;
                            int bottom = view.getTop();
                            drawHeaderRect(c, groupinfo, left, top, right, bottom);
                        }
                    } else {
                        //当 ItemView 是屏幕上第一个可见的View 时，不管它是不是组内第一个View
                        //它都需要绘制它对应的 StickyHeader。

                        // 还要判断当前的 ItemView 是不是它组内的最后一个 View

                        int top = parent.getPaddingTop();
                        if (groupinfo.isLastViewInGroup()) {
                            int suggestTop = view.getBottom() - mHeaderHeight;
                            // 当 ItemView 与 Header 底部平齐的时候，判断 Header 的顶部是否小于
                            // parent 顶部内容开始的位置，如果小于则对 Header.top 进行位置更新，
                            //否则将继续保持吸附在 parent 的顶部
                            if (suggestTop < top) {
                                top = suggestTop;
                            }
                        }
                        int bottom = top + mHeaderHeight;
                        drawHeaderRect(c, groupinfo, left, top, right, bottom);
                    }
                    groupId = groupinfo.mGroupID;
                }

            }
        }
    }

    private void drawHeaderRect(Canvas c, GroupInfo groupinfo, int left, int top, int right, int
            bottom) {
        //绘制Header
        c.drawRect(left, top, right, bottom, mPaint);

        float titleX = left + mTextOffsetX;
        float titleY = bottom + mFontMetrics.ascent + -mFontMetrics.descent / 2;
        //绘制Title
        c.drawText(groupinfo.getTitle(), titleX, titleY, mTextPaint);
    }

    public GroupInfoCallback getCallback() {
        return mCallback;
    }

    public void setCallback(GroupInfoCallback callback) {
        this.mCallback = callback;
    }

    public interface GroupInfoCallback {
        GroupInfo getGroupInfo(int position);
    }

    public static class GroupInfo {

        private int mGroupID;

        private String mTitle;

        private int position;

        private int mGroupLength;


        public GroupInfo(int groupId, String title) {
            this.mGroupID = groupId;
            this.mTitle = title;
        }

        public int getGroupID() {
            return mGroupID;
        }

        public void setGroupID(int groupID) {
            this.mGroupID = groupID;
        }

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String title) {
            this.mTitle = title;
        }

        public boolean isFirstViewInGroup() {
            return position == 0;
        }

        public boolean isLastViewInGroup() {
            return position == mGroupLength - 1 && position >= 0;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

        public void setGroupLength(int groupLength) {
            this.mGroupLength = groupLength;
        }
    }
}