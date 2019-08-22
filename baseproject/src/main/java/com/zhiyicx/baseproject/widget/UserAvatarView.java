package com.zhiyicx.baseproject.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.log.LogUtils;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public class UserAvatarView extends FrameLayout {
    private static final float DEFAULT_RATIO = 3.5f;

    private FilterImageView mIvAvatar;
    private ImageView mIvVerify;

    private int mAvatarSize;
    private int mVerifySize;
    private float mVerifyRatio = DEFAULT_RATIO;


    public UserAvatarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserAvatarView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_user_avater, this);
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs,
                    R.styleable.userAvatarView);
            mAvatarSize = array.getDimensionPixelOffset(R.styleable.userAvatarView_ts_avatare_size, getContext().getResources()
                    .getDimensionPixelOffset(R.dimen.headpic_for_list));
            mVerifyRatio = array.getFloat(R.styleable.userAvatarView_ts_ratio, DEFAULT_RATIO);
            array.recycle();
        } else {
            mAvatarSize = getContext().getResources().getDimensionPixelOffset(R.dimen.headpic_for_list);
        }
        mVerifySize = (int) (mAvatarSize / mVerifyRatio);

        mIvAvatar = (FilterImageView) findViewById(R.id.iv_avatar);

        mIvAvatar.getLayoutParams().height = mAvatarSize;
        mIvAvatar.getLayoutParams().width = mAvatarSize;

        mIvVerify = (ImageView) findViewById(R.id.iv_verify);

        mIvVerify.getLayoutParams().height = mVerifySize;
        mIvVerify.getLayoutParams().width = mVerifySize;

    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mIvAvatar.setOnClickListener(l);
    }

    public FilterImageView getIvAvatar() {
        return mIvAvatar;
    }

    public ImageView getIvVerify() {
        return mIvVerify;
    }
}
