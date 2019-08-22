package com.haohaishengshi.haohaimusic.modules.music_fm.music_comment.adapter;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.haohaishengshi.haohaimusic.R;
import com.haohaishengshi.haohaimusic.data.beans.MusicCommentListBean;
import com.haohaishengshi.haohaimusic.data.beans.UserInfoBean;
import com.haohaishengshi.haohaimusic.i.OnCommentTextClickListener;
import com.haohaishengshi.haohaimusic.i.OnUserInfoClickListener;
import com.haohaishengshi.haohaimusic.i.OnUserInfoLongClickListener;
import com.haohaishengshi.haohaimusic.utils.ImageUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public class MusicCommentItem implements ItemViewDelegate<MusicCommentListBean> {
    private OnUserInfoClickListener mOnUserInfoClickListener;
    private OnUserInfoLongClickListener mOnUserInfoLongClickListener;
    protected OnReSendClickListener mOnReSendClickListener;

    public void setOnCommentTextClickListener(OnCommentTextClickListener onCommentTextClickListener) {
        mOnCommentTextClickListener = onCommentTextClickListener;
    }

    private OnCommentTextClickListener mOnCommentTextClickListener;

    public void setOnUserInfoClickListener(OnUserInfoClickListener onUserInfoClickListener) {
        mOnUserInfoClickListener = onUserInfoClickListener;
    }

    public void setOnUserInfoLongClickListener(OnUserInfoLongClickListener onUserInfoLongClickListener) {
        mOnUserInfoLongClickListener = onUserInfoLongClickListener;
    }

    public void setOnReSendClickListener(OnReSendClickListener onReSendClickListener) {
        mOnReSendClickListener = onReSendClickListener;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_detail_comment;
    }

    @Override
    public boolean isForViewType(MusicCommentListBean item, int position) {
        return !TextUtils.isEmpty(item.getComment_content());
    }

    @Override
    public void convert(ViewHolder holder, final MusicCommentListBean musicCommentListBean,
                        MusicCommentListBean lastT, final int position, int itemCounts) {
        if (musicCommentListBean.getFromUserInfoBean() != null) {
//            ImageUtils.loadCircleUserHeadPic(musicCommentListBean.getFromUserInfoBean(), holder.getView(R.id.iv_headpic));
            holder.setText(R.id.tv_name, musicCommentListBean.getFromUserInfoBean().getName());
            holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(musicCommentListBean
                    .getCreated_at()));
            holder.setText(R.id.tv_content, setShowText(musicCommentListBean, position));
            holder.getView(R.id.fl_tip).setVisibility(View.GONE);
//            if (musicCommentListBean.getState() == SEND_ERROR) {
//                holder.getView(R.id.fl_tip).setVisibility(View.VISIBLE);
//            } else {
//
//            }
            RxView.clicks(holder.getView(R.id.fl_tip))
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                    .subscribe(aVoid -> {
                        if (mOnReSendClickListener != null) {
                            mOnReSendClickListener.onReSendClick(musicCommentListBean);
                        }
                    });
            List<Link> links = setLiknks(holder, musicCommentListBean, position);
            if (!links.isEmpty()) {
                ConvertUtils.stringLinkConvert(holder.getView(R.id.tv_content), links);
            }
            holder.setOnClickListener(R.id.tv_content, v -> {
                if (mOnCommentTextClickListener != null) {
                    mOnCommentTextClickListener.onCommentTextClick(position);
                }
            });
            holder.getView(R.id.tv_content).setOnLongClickListener(v -> {
                if (mOnCommentTextClickListener != null) {
                    mOnCommentTextClickListener.onCommentTextLongClick(position);
                }
                return true;
            });
            setUserInfoClick(holder.getView(R.id.tv_name), musicCommentListBean.getFromUserInfoBean());
            setUserInfoClick(holder.getView(R.id.iv_headpic), musicCommentListBean.getFromUserInfoBean());
        }

    }

    private void setUserInfoClick(View v, final UserInfoBean userInfoBean) {
        RxView.clicks(v).subscribe(aVoid -> {
            if (mOnUserInfoClickListener != null) {
                mOnUserInfoClickListener.onUserInfoClick(userInfoBean);
            }
        });
    }

    protected String setShowText(MusicCommentListBean musicCommentListBean, int position) {
        return handleName(musicCommentListBean);
    }

    protected List<Link> setLiknks(ViewHolder holder, final MusicCommentListBean musicCommentListBean, int position) {
        List<Link> links = new ArrayList<>();
        if (musicCommentListBean.getToUserInfoBean() != null && musicCommentListBean.getToUserInfoBean().getName() != null) {
            Link replyNameLink = new Link(musicCommentListBean.getToUserInfoBean().getName())
                    .setTextColor(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.important_for_content))                  // optional, defaults to holo blue
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.general_for_hint)) // optional, defaults to holo blue
                    .setHighlightAlpha(.5f)                                     // optional, defaults to .15f
                    .setUnderlined(false)                                       // optional, defaults to true
                    .setOnLongClickListener((clickedText, linkMetadata) -> {
                        if (mOnUserInfoLongClickListener != null) {
                            mOnUserInfoLongClickListener.onUserInfoLongClick(musicCommentListBean.getToUserInfoBean());
                        }
                    })
                    .setOnClickListener((clickedText, linkMetadata) -> {
                        // single clicked
                        if (mOnUserInfoClickListener != null) {
                            mOnUserInfoClickListener.onUserInfoClick(musicCommentListBean.getToUserInfoBean());
                        }
                    });
            links.add(replyNameLink);
        }


        return links;
    }

    /**
     * 处理名字的颜色与点击
     *
     * @param musicCommentListBean
     * @return
     */
    private String handleName(MusicCommentListBean musicCommentListBean) {
        String content = "";
        if (musicCommentListBean.getReply_user() != 0 && musicCommentListBean.getToUserInfoBean() != null) { // 当没有回复者时，就是回复评论
            content += " 回复 " + musicCommentListBean.getToUserInfoBean().getName() + ": " +
                    musicCommentListBean.getComment_content();
        } else {
            content = musicCommentListBean.getComment_content();
        }
        return content;
    }

    /**
     * resend interface
     */
    public interface OnReSendClickListener {
        void onReSendClick(MusicCommentListBean musicCommentListBean);
    }
}
