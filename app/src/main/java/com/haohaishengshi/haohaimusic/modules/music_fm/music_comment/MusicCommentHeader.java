package com.haohaishengshi.haohaimusic.modules.music_fm.music_comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haohaishengshi.haohaimusic.R;
import com.haohaishengshi.haohaimusic.base.AppApplication;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;

import java.io.Serializable;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description 歌曲评论头部信息
 */
public class MusicCommentHeader {
    private View mMusicCommentHeader;
    private Context mContext;

    private TextView mTitle;
    private TextView mListenCount;
    private TextView mCommentCount;
    private ImageView mHeaderImag;
    private ImageLoader mImageLoader;
    private FrameLayout mCommentCountView;
    private HeadlerClickEvent mHeadlerClickEvent;

    public MusicCommentHeader(Context context) {
        this.mContext = context;
        mMusicCommentHeader = LayoutInflater.from(context).inflate(R.layout
                .view_header_music_comment, null);
        mMusicCommentHeader.setOnClickListener(v -> {
            if (mHeadlerClickEvent != null) {
                mHeadlerClickEvent.headClick();
            }
        });
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        mMusicCommentHeader.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mCommentCountView = (FrameLayout) mMusicCommentHeader.findViewById(R.id.head_info_music_comment_count);
        mTitle = (TextView) mMusicCommentHeader.findViewById(R.id.head_info_music_title);
        mListenCount = (TextView) mMusicCommentHeader.findViewById(R.id.head_info_music_listen);
        mCommentCount = (TextView) mMusicCommentHeader.findViewById(R.id.tv_comment_count);
        mHeaderImag = (ImageView) mMusicCommentHeader.findViewById(R.id.head_info_music_comment);
    }

    public View getMusicCommentHeader() {
        return mMusicCommentHeader;
    }

    public void hide() {
        mMusicCommentHeader.setVisibility(View.GONE);
    }

    public void show() {
        mMusicCommentHeader.setVisibility(View.VISIBLE);
    }

    public void setHeadInfo(HeaderInfo headInfo) {
        if (headInfo == null) {
            return;
        }
        show();
        mTitle.setText(headInfo.getTitle());
        mListenCount.setText(headInfo.getLitenerCount());
        mImageLoader.loadImage(mContext, GlideImageConfig.builder()
                .imagerView(mHeaderImag)
                .url(headInfo.getImageUrl())
                .build());
        setCommentList(headInfo.getCommentCount());
    }

    public void setCommentList(int size) {
        if (size > 0) {
            mCommentCountView.setVisibility(View.VISIBLE);
            mCommentCount.setText(mContext.getResources().getString(R.string.dynamic_comment_count, "" + size));
        } else {
            mCommentCountView.setVisibility(View.GONE);
        }

    }

    public void setHeadlerClickEvent(HeadlerClickEvent headlerClickEvent) {
        mHeadlerClickEvent = headlerClickEvent;
    }

    public static class HeaderInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        private int id;
        private String title;
        private String litenerCount;
        private String imageUrl;
        private int commentCount;

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLitenerCount() {
            return litenerCount;
        }

        public void setLitenerCount(String litenerCount) {
            this.litenerCount = litenerCount;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    public interface HeadlerClickEvent {
        void headClick();
    }
}
