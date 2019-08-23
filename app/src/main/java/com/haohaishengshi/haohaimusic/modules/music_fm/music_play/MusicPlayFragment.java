package com.haohaishengshi.haohaimusic.modules.music_fm.music_play;

import android.animation.AnimatorInflater;
import android.animation.LayoutTransition;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaDescription;
import android.media.MediaMetadata;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.haohaishengshi.haohaimusic.R;
import com.haohaishengshi.haohaimusic.base.AppApplication;
import com.haohaishengshi.haohaimusic.config.SharePreferenceTagConfig;
import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumDetailsBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicDetaisBean;
import com.haohaishengshi.haohaimusic.modules.music_fm.media_data.MusicProviderSource;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_album_detail.MusicDetailActivity;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_comment.MusicCommentActivity;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_comment.MusicCommentHeader;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.MediaIDHelper;
import com.haohaishengshi.haohaimusic.utils.ImageUtils;
import com.haohaishengshi.haohaimusic.widget.MusicListPopupWindow;
import com.haohaishengshi.haohaimusic.widget.PlayerSeekBar;
import com.haohaishengshi.haohaimusic.widget.pager_recyclerview.LoopPagerRecyclerView;
import com.haohaishengshi.haohaimusic.widget.pager_recyclerview.PagerRecyclerView;
import com.haohaishengshi.haohaimusic.widget.pager_recyclerview.RecyclerViewUtils;
import com.nineoldandroids.animation.ObjectAnimator;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideMusicBgTransform;
import com.zhiyicx.baseproject.utils.WindowUtils;
import com.zhiyicx.baseproject.widget.InputPasswordView;
import com.zhiyicx.baseproject.widget.popwindow.PayPopWindow;
import com.zhiyicx.baseproject.widget.textview.CenterImageSpan;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;

import static com.haohaishengshi.haohaimusic.config.EventBusTagConfig.EVENT_MUSIC_CHANGE;
import static com.haohaishengshi.haohaimusic.config.EventBusTagConfig.EVENT_MUSIC_COMMENT_COUNT;
import static com.haohaishengshi.haohaimusic.config.EventBusTagConfig.EVENT_MUSIC_LIKE;
import static com.haohaishengshi.haohaimusic.config.EventBusTagConfig.EVENT_SEND_MUSIC_CACHE_PROGRESS;
import static com.haohaishengshi.haohaimusic.config.EventBusTagConfig.EVENT_SEND_MUSIC_COMPLETE;
import static com.haohaishengshi.haohaimusic.config.EventBusTagConfig.EVENT_SEND_MUSIC_LOAD;
import static com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly.PlaybackManager.ORDERLOOP;
import static com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly.PlaybackManager.ORDERSINGLE;
import static com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly.PlaybackManager.ORDER_ACTION;
import static com.haohaishengshi.haohaimusic.modules.music_fm.media_data.MusicDataConvert.METADATA_KEY_GENRE;
import static com.haohaishengshi.haohaimusic.modules.music_fm.music_album_detail.MusicDetailFragment.MUSIC_INFO;
import static com.haohaishengshi.haohaimusic.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT;
import static com.haohaishengshi.haohaimusic.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE;
import static com.haohaishengshi.haohaimusic.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE_MUSIC;
import static com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.MediaIDHelper.MEDIA_ID_MUSICS_BY_GENRE;
import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description 音乐播放主要界面
 */
@SuppressWarnings("unchecked")
public class MusicPlayFragment extends TSFragment<MusicPlayContract.Presenter> implements
        MusicPlayContract.View, PagerRecyclerView.OnPageChangedListener {

    @BindView(R.id.fragment_music_paly_phonograph_point)
    ImageView mFragmentMusicPalyPhonographPoint;
    @BindView(R.id.fragment_music_paly_share)
    ImageView mFragmentMusicPalyShare;
    @BindView(R.id.fragment_music_paly_like)
    ImageView mFragmentMusicPalyLike;
    @BindView(R.id.fragment_music_paly_comment)
    ImageView mFragmentMusicPalyComment;
    @BindView(R.id.fragment_music_paly_comment_count)
    TextView mFragmentMusicPalyCommentCount;
    @BindView(R.id.fragment_music_paly_lyrics)
    ImageView mFragmentMusicPalyLyrics;
    @BindView(R.id.fragment_music_paly_progress)
    PlayerSeekBar mFragmentMusicPalyProgress;
    @BindView(R.id.fragment_music_paly_order)
    ImageView mFragmentMusicPalyOrder;
    @BindView(R.id.fragment_music_paly_preview)
    ImageView mFragmentMusicPalyPreview;
    @BindView(R.id.fragment_music_paly_palyer)
    ImageView mFragmentMusicPalyPalyer;
    @BindView(R.id.fragment_music_paly_nextview)
    ImageView mFragmentMusicPalyNextview;
    @BindView(R.id.fragment_music_paly_list)
    ImageView mFragmentMusicPalyList;
    @BindView(R.id.fragment_music_paly_bg)
    ImageView mFragmentMusicPalyBg;
    @BindView(R.id.fragment_music_paly_rv)
    LoopPagerRecyclerView mFragmentMusicPalyRv;
    @BindView(R.id.fragment_music_paly_cur_time)
    TextView mFragmentMusicPalyCurTime;
    @BindView(R.id.fragment_music_paly_total_time)
    TextView mFragmentMusicPalyTotalTime;
    @BindView(R.id.fragment_music_paly_lrc)
    TextView mFragmentMusicPalyLrc;
    @BindView(R.id.fragment_music_paly_deal)
    LinearLayout mFragmentMusicPalyDeal;
    @BindView(R.id.fragment_music_paly_container)
    RelativeLayout mFragmentMusicPalyContainer;

    private PayPopWindow mPayMusicPopWindow;

    /**
     * 播放进度条更新间隔
     */
    private static final long PROGRESS_UPDATE_INTERNAL = 1000;
    /**
     * 播放进度条初始延迟
     */
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;

    private ImageLoader mImageLoader;
    private CommonAdapter mAdapter;
    private CommonAdapter popAdapter;
    private List<MusicDetaisBean> mMusicList = new ArrayList<>();
    /**
     * 播放模式
     */
    private int mDefalultOrder;
    private int mCurrentDuration;
    /**
     * 播放模式图标
     */
    private Integer[] mOrderModule = new Integer[]{R.mipmap.music_ico_random, R.mipmap
            .music_ico_single, R.mipmap.music_ico_inorder};

    /**
     * 音乐服务是否连接
     */
    private boolean isConnected;

    /**
     * 当前歌曲是否播放完
     */
    private boolean isComplete;

    /**
     * 是否滚动磁盘切歌
     */
    private boolean isMediaDataChange;

    private MediaDescription mMediaDescription;
    private MusicAlbumDetailsBean mMusicAlbumDetailsBean;
    private MusicDetaisBean mCurrentMusic;
    private String mCurrentMediaId = "-1";

    /**
     * 指针位置flag
     */
    private boolean isDraging;

    /**
     * 指针动画
     */
    private ObjectAnimator mPointAnimate;

    /**
     * 磁盘动画
     */
    private ObjectAnimator mPhonographAnimate;

    /**
     * 磁盘动画的暂停位置记录
     */
    private float mCurrentValue;

    /**
     * 当前动画view
     */
    private ViewGroup mCurrentView;

    private MediaBrowser mMediaBrowser;

    private PlaybackState mLastPlaybackState;

    /**
     * 歌曲列表
     */
    private MusicListPopupWindow mListPopupWindow;

    /**
     * 进度条控制
     */
    private Subscription mProgressSubscription;
    private Observable<Long> mProgressObservable;
    private ImageView mCurrentImageView;

    /**
     * 音乐播放切换事件回调
     */
    private final MediaController.Callback mCallback = new MediaController.Callback() {
        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackState state) {
            updatePlaybackState(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadata metadata) {
            if (metadata != null && metadata.getDescription() != null) {
                mCurrentMediaId = metadata.getDescription().getMediaId();
                updateCurrentMusic(mCurrentMediaId);
                EventBus.getDefault().post(Integer.valueOf(mCurrentMediaId), EVENT_MUSIC_CHANGE);
                if (WindowUtils.getAblumHeadInfo() != null) {
                    WindowUtils.getAblumHeadInfo().setListenCount(WindowUtils.getAblumHeadInfo()
                            .getListenCount() + 1);
                }
                //noinspection ResourceType
                String musicUrl = metadata.getString(MusicProviderSource
                        .CUSTOM_METADATA_TRACK_SOURCE);
//                boolean isCached = AppApplication.getProxy().isCached(musicUrl);
                boolean isCached=false;
                if (isCached) {
                    mFragmentMusicPalyProgress.setSecondaryProgress((int) metadata.getLong
                            (MediaMetadata.METADATA_KEY_DURATION));
                }
                mToolbarCenter.setText(metadata.getText(MediaMetadata.METADATA_KEY_ALBUM));

                mListPopupWindow.getAdapter().notifyDataSetChanged();
                updateMediaDescription(metadata.getDescription());
                updateDuration(metadata);
            }
        }
    };

    /**
     * 音乐播放服务连接回调
     */
    private final MediaBrowser.ConnectionCallback mConnectionCallback =
            new MediaBrowser.ConnectionCallback() {
                @Override
                public void onConnected() {
                    try {
                        connectToSession(mMediaBrowser.getSessionToken());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            };

    public static MusicPlayFragment newInstance(Bundle args) {
        MusicPlayFragment fragment = new MusicPlayFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediaBrowser = new MediaBrowser(getActivity(), new ComponentName(getActivity(),
                MusicPlayService.class)
                , mConnectionCallback, null);
        if (mMediaBrowser.isConnected()) {
            mMediaBrowser.disconnect();
        }
        if (savedInstanceState == null) {
            updateFromParams(getArguments());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMediaBrowser != null) {
            mDefalultOrder = SharePreferenceUtils.getInterger(getActivity(),
                    SharePreferenceTagConfig.SHAREPREFERENCE_TAG_MUSIC);
            if (mDefalultOrder != -1) {
                mListPopupWindow.setOrder(mDefalultOrder);
                mFragmentMusicPalyOrder.setImageResource(mOrderModule[mDefalultOrder]);
            } else {
                mDefalultOrder = ORDERLOOP;
            }
            if (!mMediaBrowser.isConnected()) {
                mMediaBrowser.connect();
            }

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMediaBrowser != null) {// 状态重置
            isConnected = false;
            mMediaBrowser.disconnect();// 结束断开，启动后重新连接
        }
        if (getActivity().getMediaController() != null) {
            getActivity().getMediaController().unregisterCallback(mCallback);
        }
        rxStopProgress();
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.music_fm);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_music_paly;
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        initData();
        initListener();
        initLyricsAnimation();
        initFirstMusic();
        initPlayRecyclerView();
    }

    @Override
    protected void initData() {
        MediaSession.QueueItem currentMusic = AppApplication.getmQueueManager().getCurrentMusic();
        if (currentMusic == null) {
            getActivity().finish();
            return;
        }
        if (currentMusic.getDescription() != null) {
            mCurrentMediaId = MediaIDHelper.extractMusicIDFromMediaID(currentMusic.getDescription().getMediaId());
        }
        mMusicAlbumDetailsBean = (MusicAlbumDetailsBean) getArguments().getSerializable
                (MUSIC_INFO);
        mMusicList = mMusicAlbumDetailsBean.getMusics();
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();

        mListPopupWindow = MusicListPopupWindow.Builder()
                .with(getActivity())
                .alpha(0.8f)
                .data(mMusicList)
                .adapter(getPopListAdapter())
                .build();
    }

    @Override
    public List<MusicDetaisBean> getListDatas() {
        return popAdapter.getDatas();
    }

    @Override
    public void refreshData(int position) {
        popAdapter.notifyItemChanged(position);
    }

    @Override
    public MusicAlbumDetailsBean getCurrentAblum() {
        return mMusicAlbumDetailsBean;
    }

    @Override
    public MusicDetaisBean getCurrentMusic() {
        return mCurrentMusic;
    }

    @Override
    public void setPresenter(MusicPlayContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void initListener() {

        mFragmentMusicPalyProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener
                () {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mFragmentMusicPalyCurTime.setText(DateUtils.formatElapsedTime(progress / 1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                rxStopProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                getActivity().getMediaController().getTransportControls().seekTo(seekBar
                        .getProgress());
                rxStartProgress();
            }
        });

        mFragmentMusicPalyPhonographPoint.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int w = mFragmentMusicPalyPhonographPoint.getWidth();
            int h = mFragmentMusicPalyPhonographPoint.getHeight();
            mFragmentMusicPalyPhonographPoint.setPivotX(9 * w / 10);
            mFragmentMusicPalyPhonographPoint.setPivotY(h / 2);
        });

        mFragmentMusicPalyRv.addOnPageChangedListener(this);
    }

    private void initPlayRecyclerView() {
        mFragmentMusicPalyRv.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));

        mFragmentMusicPalyRv.setFlingFactor(3f);
        mFragmentMusicPalyRv.setTriggerOffset(0.25f);
        mFragmentMusicPalyRv.setAdapter(getMediaListAdapter());
        mFragmentMusicPalyRv.setHasFixedSize(true);
    }

    private void initFirstMusic() {
        mFragmentMusicPalyLrc.setMovementMethod(ScrollingMovementMethod.getInstance());
        mFragmentMusicPalyProgress.setThumb(R.mipmap.music_pic_progressbar_circle);
        try {
            updateCurrentMusic(MediaIDHelper.extractMusicIDFromMediaID(AppApplication
                    .getmQueueManager().getCurrentMusic().getDescription().getMediaId()));
            mToolbarCenter.setText(AppApplication.getmQueueManager().getCurrentMusic().getDescription
                    ().getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initLyricsAnimation() {
        LayoutTransition transition = new LayoutTransition();
        transition.setDuration(200);
        transition.setAnimator(LayoutTransition.APPEARING, AnimatorInflater.loadAnimator
                (getActivity(), R.animator.view_visible_alpha));
        transition.setAnimator(LayoutTransition.DISAPPEARING, AnimatorInflater.loadAnimator
                (getActivity(), R.animator.view_gone_alpha));
        mFragmentMusicPalyContainer.setLayoutTransition(transition);
    }

    private void updateCurrentMusic(final String mediaId) {
        Observable.from(mMusicList).filter(musicsBean -> {
            LogUtils.i(musicsBean.getId() + ":::" + mediaId);
            return (musicsBean.getId() + "").equals(mediaId);
        }).subscribe(musicsBean -> {
            mCurrentMusic = musicsBean;
            dealCurrentMusic();
            mFragmentMusicPalyComment.setEnabled(true);
        });
    }

    // 歌曲切换后处理界面显示
    private void dealCurrentMusic() {
        MediaController controllerCompat = getActivity().getMediaController();
        if (controllerCompat != null) {
            PlaybackState state = controllerCompat.getPlaybackState();
            if (state.getState() != PlaybackState.STATE_PLAYING) {
                mFragmentMusicPalyProgress.setLoading(true);
            }
        } else {
            mFragmentMusicPalyProgress.setLoading(true);
        }

        if (mCurrentMusic.getComment_count() > 0) {
            mFragmentMusicPalyComment.setImageResource(
                    R.mipmap.music_ico_comment_incomplete);
            mFragmentMusicPalyCommentCount.setText(String.valueOf(mCurrentMusic
                    .getComment_count()));
        } else {
            mFragmentMusicPalyComment.setImageResource(
                    R.mipmap.music_ico_comment_complete);
            mFragmentMusicPalyCommentCount.setText("");
        }
        String lyric = mCurrentMusic.getLyric();
        if (TextUtils.isEmpty(lyric)) {
            lyric = getString(R.string.music_lyric);
        }
        mFragmentMusicPalyLrc.setText(lyric);

        if (mCurrentMusic.isHas_like()) {
            mFragmentMusicPalyLike.setImageResource(R.mipmap.music_ico_like_high);
        } else {
            mFragmentMusicPalyLike.setImageResource(R.mipmap.music_ico_like_normal);
        }
    }

    /**
     * 连接上音乐播放服务
     *
     * @param token
     * @throws RemoteException
     */
    private void connectToSession(MediaSession.Token token) throws RemoteException {
        MediaController mediaController = new MediaController(
                getActivity(), token);
        if (mediaController.getMetadata() == null) {
            showSnackErrorMessage(getString(R.string.music_not_found));
            getActivity().finish();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(ORDER_ACTION, mDefalultOrder);
        mediaController.getTransportControls()
                .sendCustomAction(ORDER_ACTION, bundle);
        getActivity().setMediaController(mediaController);
        mediaController.registerCallback(mCallback);
        PlaybackState state = mediaController.getPlaybackState();

        updatePlaybackState(state);

        MediaMetadata metadata = mediaController.getMetadata();

        if (metadata != null && metadata.getDescription() != null) {
            updateMediaDescription(metadata.getDescription());
            updateDuration(metadata);
        }
        if (state != null && (state.getState() == PlaybackState.STATE_PLAYING ||
                state.getState() == PlaybackState.STATE_BUFFERING)) {
//            rxStartProgress();
        }
        isConnected = true;
    }

    /**
     * 更新音乐信息
     *
     * @param description 音乐信息数据
     */
    private void updateMediaDescription(final MediaDescription description) {
        if (description != null) {
//            mMediaDescription = description;
//            mImageLoader.loadImage(getActivity(), GlideImageConfig.builder()
//                    .transformation(new GlideMusicBgTransform(getActivity()))
//                    .errorPic(R.drawable.shape_default_image)
//                    .placeholder(R.drawable.shape_default_image)
//                    .imagerView(mFragmentMusicPalyBg)
//                    .url(description.getIconUri() + "")
//                    .build());

            View item = RecyclerViewUtils
                    .getCenterXChild(mFragmentMusicPalyRv);

            mCurrentImageView = item.findViewById(R.id.fragment_music_paly_img);

            mImageLoader.loadImage(getActivity(), GlideImageConfig.builder()
                    .imagerView(mCurrentImageView)
                    .errorPic(R.drawable.shape_default_image)
                    .placeholder(R.drawable.shape_default_image)
                    .url(description.getIconUri() + "")
                    .build());

        }

    }

    // 更新音乐时长
    private void updateDuration(MediaMetadata metadata) {
        if (metadata == null) {
            return;
        }
        int duration = (int) metadata.getLong(MediaMetadata.METADATA_KEY_DURATION);
        mCurrentDuration = duration;
        mFragmentMusicPalyProgress.setMax(duration);
        mFragmentMusicPalyTotalTime.setText(DateUtils.formatElapsedTime(duration / 1000));
    }

    // 播放状态改变
    private void updatePlaybackState(PlaybackState state) {
        if (state == null) {
            return;
        }
        mLastPlaybackState = state;
        switch (state.getState()) {
            case PlaybackState.STATE_PLAYING:
                rxStartProgress();
                if (mPhonographAnimate == null || !mPhonographAnimate.isStarted()) {
                    doPhonographAnimation();
                }
                mFragmentMusicPalyPalyer.setImageResource(R.mipmap.music_ico_stop);
                break;
            case PlaybackState.STATE_PAUSED:
                rxStopProgress();
                if (mPhonographAnimate != null && mPhonographAnimate.isStarted()) {
                    pauseAnimation();
                }
                mFragmentMusicPalyPalyer.setImageResource(R.mipmap.music_ico_play);
                break;
            case PlaybackState.STATE_NONE:
            case PlaybackState.STATE_STOPPED:
                rxStopProgress();
                break;
            case PlaybackState.STATE_BUFFERING:
                rxStopProgress();
                break;
            case PlaybackState.STATE_ERROR:
                showSnackErrorMessage(getString(R.string.music_not_found));
                getActivity().finish();
                break;
            default:
                break;
        }

    }

    // 开始音乐播放进度
    private void rxStartProgress() {
        if (mProgressObservable == null) {
            mProgressObservable = Observable.interval(PROGRESS_UPDATE_INITIAL_INTERVAL,
                    PROGRESS_UPDATE_INTERNAL, TimeUnit
                            .MILLISECONDS);
        }
        mProgressSubscription = mProgressObservable
                .subscribe(aLong -> updateProgress());
    }

    // 停止音乐播放进度条
    private void rxStopProgress() {
        if (mProgressSubscription != null) {
            mProgressSubscription.unsubscribe();
        }
    }

    // 更新进度条数据
    private void updateProgress() {

        if (mLastPlaybackState == null || mFragmentMusicPalyProgress == null) {
            return;
        }
        long currentPosition = mLastPlaybackState.getPosition();
        if (mLastPlaybackState.getState() != PlaybackState.STATE_PAUSED) {
            mFragmentMusicPalyProgress.setLoading(false);
            long timeDelta = SystemClock.elapsedRealtime() -
                    mLastPlaybackState.getLastPositionUpdateTime();
            currentPosition += (int) timeDelta * mLastPlaybackState.getPlaybackSpeed();
        }
        mFragmentMusicPalyProgress.setProgress((int) currentPosition);
    }


    private void updateFromParams(Bundle bundle) {
        if (bundle != null) {
            MediaDescription description = bundle.getParcelable(MusicDetailActivity
                    .EXTRA_CURRENT_MEDIA_DESCRIPTION);
            if (description != null) {
                updateMediaDescription(description);
            }
        }
    }

    // 缓冲进度
    @Subscriber(tag = EVENT_SEND_MUSIC_CACHE_PROGRESS, mode = ThreadMode.MAIN)
    public void onBufferingUpdate(int progress) {
        mFragmentMusicPalyProgress.setSecondaryProgress(mCurrentDuration * progress / 100);
    }

    // 是否加载中
    @Subscriber(tag = EVENT_SEND_MUSIC_LOAD, mode = ThreadMode.MAIN)
    public void onMusicLoading(int currentDuration) {
        LogUtils.d("MUSIC_LOADING", "" + (currentDuration > 0) + "");
        mFragmentMusicPalyProgress.setLoading(!(currentDuration > 0));
        if (currentDuration > 0) {
            updateDuration(new MediaMetadata.Builder().putLong(MediaMetadata
                    .METADATA_KEY_DURATION, currentDuration * 1000).build());
        }
    }

    // 播放结束
    @Subscriber(tag = EVENT_SEND_MUSIC_COMPLETE, mode = ThreadMode.MAIN)
    public void onMusicEnd(int orderType) {
        isComplete = true;
        isMediaDataChange = true;
        if (orderType != ORDERSINGLE) {
            doPointOutAnimation(500, 0);
            mFragmentMusicPalyRv.setSpeed(100);
            mFragmentMusicPalyRv.smoothScrollToPosition(mFragmentMusicPalyRv
                    .getCurrentPosition() + 1);
            mFragmentMusicPalyRv.setSpeed(250);
        }
        LogUtils.d("MUSIC_END", "" + orderType);
    }

    // 评论数量改变
    @Subscriber(tag = EVENT_MUSIC_COMMENT_COUNT, mode = ThreadMode.MAIN)
    public void onCommentCountUpdate(MusicCommentHeader.HeaderInfo headerInfo) {
        mCurrentMusic.setComment_count(headerInfo.getCommentCount());
        if (mCurrentMusic.getComment_count() > 0) {
            mFragmentMusicPalyComment.setImageResource(
                    R.mipmap.music_ico_comment_incomplete);
            mFragmentMusicPalyCommentCount.setText(mCurrentMusic.getComment_count() + "");
        } else {
            mFragmentMusicPalyComment.setImageResource(
                    R.mipmap.music_ico_comment_complete);
            mFragmentMusicPalyCommentCount.setText("");
        }
        LogUtils.d("EVENT_MUSIC_COMMENT_COUNT");
    }

    // 指针搭上磁盘动画
    public void doPointInAnimation(int duration, long delay) {
        try {
            mPointAnimate = ObjectAnimator.ofFloat(mFragmentMusicPalyPhonographPoint, "rotation",
                    0,
                    25);
            // 设置持续时间
            mPointAnimate.setDuration(duration);
            mPointAnimate.setStartDelay(delay);
            mPointAnimate.setInterpolator(new AccelerateDecelerateInterpolator());
            mPointAnimate.start();
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }

    }

    // 指针滑出磁盘动画
    public void doPointOutAnimation(int duration, long delay) {
        try {
            mPointAnimate = ObjectAnimator.ofFloat(mFragmentMusicPalyPhonographPoint, "rotation",
                    25,
                    0);
            // 设置持续时间
            mPointAnimate.setDuration(duration);
            mPointAnimate.setStartDelay(delay);
            mPointAnimate.setInterpolator(new AccelerateDecelerateInterpolator());
            mPointAnimate.start();
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }

    }


    // 磁盘旋转动画
    public void doPhonographAnimation() {
        doPointInAnimation(500, 0);
        View target;
        if (mCurrentView == null && mFragmentMusicPalyRv != null) {
            mCurrentView = (ViewGroup) RecyclerViewUtils.getCenterXChild
                    (mFragmentMusicPalyRv);
        }
        target = mCurrentView.getChildAt(0);

        mPhonographAnimate = ObjectAnimator.ofFloat(target, "rotation",
                mCurrentValue,
                mCurrentValue + 360);
        // 设置持续时间
        mPhonographAnimate.setDuration(10000);

        mPhonographAnimate.setInterpolator(new LinearInterpolator());
        mPhonographAnimate.setRepeatCount(ObjectAnimator.INFINITE);
        // 设置动画监听
        mPhonographAnimate.addUpdateListener(animation -> {
            // TODO Auto-generated method stub
            // 监听动画执行的位置，以便下次开始时，从当前位置开始
            mCurrentValue = (float) animation.getAnimatedValue();
        });
        mPhonographAnimate.start();
    }

    // 停止转盘动画
    public void stopAnimation(View target) {
        if (target != null && mPhonographAnimate != null) {
            mPhonographAnimate.setDuration(0);
            mPhonographAnimate.reverse();
            mPhonographAnimate.end();
            target.clearAnimation();
            mCurrentValue = 0;// 重置起始位置
        }
    }

    // 暂停转盘动画
    public void pauseAnimation() {
        if (mCurrentView == null) {
            mCurrentView = (ViewGroup) RecyclerViewUtils.getCenterXChild
                    (mFragmentMusicPalyRv);
        }
        if (mCurrentView != null && mPhonographAnimate != null) {
            mPhonographAnimate.cancel();
            mCurrentView.getChildAt(0).clearAnimation();// 清除此ImageView身上的动画
        }
        doPointOutAnimation(500, 100);
    }

    // 音乐播放列表适配器
    @NonNull
    private CommonAdapter getPopListAdapter() {
        popAdapter = new CommonAdapter<MusicDetaisBean>(getActivity()
                , R.layout.item_music_detail_list, mMusicList) {
            @Override
            protected void convert(ViewHolder holder, MusicDetaisBean item, int
                    position) {
                TextView musicName = holder.getView(R.id.item_music_name);
                TextView authorName = holder.getView(R.id.item_music_author);
                String music_name = item.getTitle();
                musicName.setText(music_name);
                authorName.setText("-" + item.getSinger().getName());

                if (item.getStorage().getAmount() != 0) {// 有收费
                    Drawable top_drawable = getResources().getDrawable(R.mipmap.musici_pic_pay02);//  musicName.getLineHeight()
                    top_drawable.setBounds(0, 0, top_drawable.getIntrinsicWidth(), top_drawable.getIntrinsicHeight());
                    ImageSpan imgSpan = new CenterImageSpan(top_drawable);
                    SpannableString spannableString = SpannableString.valueOf("T" + music_name);
                    spannableString.setSpan(imgSpan, 0, 1, Spannable
                            .SPAN_EXCLUSIVE_EXCLUSIVE);
                    musicName.setText(spannableString);
                } else {
                    musicName.setText(music_name);
                }

                if (mCurrentMediaId.equals(item.getId() + "")) {
                    musicName.setTextColor(getColor(R.color.important_for_theme));
                    authorName.setTextColor(getColor(R.color.important_for_theme));
                } else {
                    musicName.setTextColor(getColor(R.color.important_for_content));
                    authorName.setTextColor(getColor(R.color
                            .normal_for_assist_text));
                }

            }
        };
        popAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position < 0) {
                    return;
                }
                MusicDetaisBean item = mMusicList.get(position);

                if (item.getStorage().getAmount() != 0 && !item.getStorage().isPaid()) {
                    initMusicCenterPopWindow(position, item.getStorage().getAmount(),
                            item.getStorage().getPaid_node(), R.string.buy_pay_single_music_desc);
                    return;
                }

                MediaController controllerCompat = getActivity()
                        .getMediaController();
                String id = MediaIDHelper.createMediaID("" + item.getId(),
                        MEDIA_ID_MUSICS_BY_GENRE, METADATA_KEY_GENRE);
                MediaSession.QueueItem queueItem = AppApplication.getmQueueManager().getCurrentMusic();
                controllerCompat.getTransportControls()
                        .playFromMediaId(id, null);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return false;
            }
        });
        return popAdapter;
    }

    // 音乐磁盘适配器
    @NonNull
    private CommonAdapter getMediaListAdapter() {
        final int imageSize = getResources().getDimensionPixelSize(R.dimen.music_play_head);
        mAdapter = new CommonAdapter<MusicDetaisBean>(getActivity(),
                R.layout.item_music_play, mMusicList) {
            @Override
            protected void convert(ViewHolder holder, MusicDetaisBean o, final
            int position) {
                ImageView image = holder.getView(R.id.fragment_music_paly_img);
                String imageUrl = null;
                if (mMusicAlbumDetailsBean != null && mMusicAlbumDetailsBean.getStorage() != null && mMusicAlbumDetailsBean.getStorage().getId() !=
                        0) {

                    imageUrl = ImageUtils.imagePathConvertV2(
                            mMusicAlbumDetailsBean.getStorage().getId(), imageSize, imageSize, ImageZipConfig.IMAGE_70_ZIP);
                } else {
                    if (o != null && o.getSinger() != null && o.getSinger().getCover() != null && o.getSinger().getCover().getId() != 0) {
                        imageUrl = ImageUtils.imagePathConvertV2(
                                o.getSinger().getCover().getId(), imageSize, imageSize, ImageZipConfig.IMAGE_70_ZIP);
                    }
                }
                if (!TextUtils.isEmpty(imageUrl)) {

                    // 音乐磁盘图片，用的专辑封面
                    mImageLoader.loadImage(getActivity(), GlideImageConfig.builder()
                            .imagerView(image)
                            .errorPic(R.drawable.shape_default_image)
                            .placeholder(R.drawable.shape_default_image)
                            .url(imageUrl)
                            .build());
                }
                // 音乐背景图片，用的图片封面
                String bgImageUrl = null;
                if (o != null && o.getSinger() != null && o.getSinger().getCover() != null && o.getSinger().getCover().getId() != 0) {
                    bgImageUrl = ImageUtils.imagePathConvertV2(
                            o.getSinger().getCover().getId(), imageSize, imageSize, ImageZipConfig.IMAGE_70_ZIP);
                }
                if (!TextUtils.isEmpty(bgImageUrl)) {
                    mImageLoader.loadImage(getActivity(), GlideImageConfig.builder()
                            .transformation(new GlideMusicBgTransform(getActivity()))
                            .errorPic(R.drawable.shape_default_image)
                            .placeholder(R.drawable.shape_default_image)
                            .imagerView(mFragmentMusicPalyBg)
                            .url(bgImageUrl)
                            .build());
                }
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                handleLrc();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return true;
            }
        });
        return mAdapter;
    }


    private void handleLrc() {
        mFragmentMusicPalyRv.setVisibility(mFragmentMusicPalyLrc.getVisibility() == View
                .VISIBLE ? View.VISIBLE : View.GONE);

        mFragmentMusicPalyPhonographPoint.setVisibility(mFragmentMusicPalyLrc.getVisibility() ==
                View.VISIBLE ? View.VISIBLE : View.GONE);

        mFragmentMusicPalyLrc.setVisibility(mFragmentMusicPalyLrc.getVisibility() == View
                .VISIBLE ? View.GONE : View.VISIBLE);

        mFragmentMusicPalyDeal.setVisibility(mFragmentMusicPalyLrc.getVisibility() == View
                .VISIBLE ? View.INVISIBLE : View.VISIBLE);
    }

    @OnClick({R.id.fragment_music_paly_share, R.id.fragment_music_paly_like, R.id
            .fragment_music_paly_comment, R.id.fragment_music_paly_lyrics, R.id
            .fragment_music_paly_order, R.id.fragment_music_paly_preview, R.id
            .fragment_music_paly_palyer, R.id.fragment_music_paly_nextview, R.id
            .fragment_music_paly_list, R.id.fragment_music_paly_bg, R.id.fragment_music_paly_lrc,
            R.id.fragment_music_paly_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_music_paly_share:// 分享
                mPresenter.shareMusic(ConvertUtils.drawable2BitmapWithWhiteBg(getContext(),
                        mCurrentImageView.getDrawable(), R.mipmap.ic_launcher));
                break;
            case R.id.fragment_music_paly_like: // 点赞
                mPresenter.handleLike(!mCurrentMusic.isHas_like(),
                        mMediaDescription.getMediaId());
                if (mCurrentMusic.isHas_like()) {
                    mCurrentMusic.setHas_like(false);
                    mFragmentMusicPalyLike.setImageResource(R.mipmap.music_ico_like_normal);
                } else {
                    mFragmentMusicPalyLike.setImageResource(R.mipmap.music_ico_like_high);
                    mCurrentMusic.setHas_like(true);
                }
                EventBus.getDefault().post(mCurrentMusic, EVENT_MUSIC_LIKE);
                break;
            case R.id.fragment_music_paly_comment: // 评论
                Intent intent = new Intent(getActivity(), MusicCommentActivity.class);
                Bundle musicBundle = new Bundle();
                MusicCommentHeader.HeaderInfo headerInfo = new MusicCommentHeader.HeaderInfo();
                headerInfo.setCommentCount(mCurrentMusic.getComment_count());
                headerInfo.setId(mCurrentMusic.getId().intValue());
                headerInfo.setTitle(mCurrentMusic.getTitle());
                headerInfo.setLitenerCount(mCurrentMusic.getTaste_count() + "");
                headerInfo.setImageUrl(ImageUtils.imagePathConvertV2(mCurrentMusic
                                .getSinger().getCover().getId()
                        , getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_home)
                        , getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_home)
                        , ImageZipConfig.IMAGE_70_ZIP));
                musicBundle.putSerializable(CURRENT_COMMENT, headerInfo);
                musicBundle.putString(CURRENT_COMMENT_TYPE, CURRENT_COMMENT_TYPE_MUSIC);
                intent.putExtra(CURRENT_COMMENT, musicBundle);
                startActivity(intent);
                break;
            case R.id.fragment_music_paly_order: // 播放顺序
                mDefalultOrder++;
                if (mDefalultOrder > 2) {
                    mDefalultOrder = 0;
                }
                mListPopupWindow.setOrder(mDefalultOrder);
                Bundle bundle = new Bundle();
                bundle.putInt(ORDER_ACTION, mDefalultOrder);
                getActivity().getMediaController().getTransportControls()
                        .sendCustomAction(ORDER_ACTION, bundle);

                mFragmentMusicPalyOrder.setImageResource(mOrderModule[mDefalultOrder]);
                SharePreferenceUtils.setInterger(getActivity(),
                        SharePreferenceTagConfig.SHAREPREFERENCE_TAG_MUSIC, mDefalultOrder);

                break;
            case R.id.fragment_music_paly_preview:// 上一首歌
                mFragmentMusicPalyComment.setEnabled(false);
                rxStopProgress();
                if (mPhonographAnimate != null && mPhonographAnimate.isStarted()) {
                    pauseAnimation();
                }
                mFragmentMusicPalyRv.smoothScrollToPosition(
                        mFragmentMusicPalyRv.getCurrentPosition() - 1
                );

                break;
            case R.id.fragment_music_paly_palyer:// 播放暂停
                PlaybackState state = getActivity().getMediaController()
                        .getPlaybackState();
                if (state != null) {
                    MediaController.TransportControls controls =
                            getActivity().getMediaController().getTransportControls();
                    switch (state.getState()) {
                        case PlaybackState.STATE_PLAYING:
                        case PlaybackState.STATE_BUFFERING:
                            controls.pause();
                            rxStopProgress();
                            break;
                        case PlaybackState.STATE_PAUSED:
                        case PlaybackState.STATE_STOPPED:
                            controls.play();
                            rxStartProgress();
                            break;
                        default:
                            break;
                    }
                }
                break;
            case R.id.fragment_music_paly_nextview:// 下一首
                mFragmentMusicPalyComment.setEnabled(false);
                rxStopProgress();
                if (mPhonographAnimate != null && mPhonographAnimate.isStarted()) {
                    pauseAnimation();
                }

                mFragmentMusicPalyRv.smoothScrollToPosition(
                        mFragmentMusicPalyRv.getCurrentPosition() + 1
                );
                break;
            case R.id.fragment_music_paly_list:// 歌曲目录
                mListPopupWindow.show();
                break;
//            case R.id.fragment_music_paly_bg:
            case R.id.fragment_music_paly_lyrics:// 歌词显示
            case R.id.fragment_music_paly_lrc:
//            case R.id.fragment_music_paly_container:
                handleLrc();
                break;
            default:
                break;
        }
    }

    @Override
    protected boolean setUseShadowView() {
        return true;
    }

    @Override
    protected void onShadowViewClick() {
        showInputPsdView(false);
    }

    @Override
    protected boolean setUseInputPsdView() {
        return true;
    }

    @Override
    public void onSureClick(View v, String text, InputPasswordView.PayNote payNote) {
        mPresenter.payNote(payNote.dynamicPosition, payNote.note, payNote.psd);
    }

    @Override
    public void onForgetPsdClick() {
        showInputPsdView(false);
//        startActivity(new Intent(getActivity(), FindPasswordActivity.class));
//        mActivity.finish();
    }

    @Override
    public void onCancle() {
        dismissSnackBar();
        mPresenter.canclePay();
        showInputPsdView(false);
    }

    private void initMusicCenterPopWindow(final int position, float amout,
                                          final int note, int strRes) {
        mPayMusicPopWindow = PayPopWindow.builder()
                .with(getActivity())
                .isWrap(true)
                .isFocus(true)
                .isOutsideTouch(true)
                .buildLinksColor1(R.color.themeColor)
                .buildLinksColor2(R.color.important_for_content)
                .contentView(R.layout.ppw_for_center)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .buildDescrStr(String.format(getString(strRes), amout, mPresenter
                        .getGoldName()))
                .buildLinksStr(getString(R.string.buy_pay_member))
                .buildTitleStr(getString(R.string.buy_pay))
                .buildItem1Str(getString(R.string.buy_pay_in))
                .buildItem2Str(getString(R.string.buy_pay_out))
                .buildMoneyStr(getString(R.string.buy_pay_integration, "" + amout))
                .buildCenterPopWindowItem1ClickListener(() -> {
                    if (mPresenter.usePayPassword()) {
                        InputPasswordView.PayNote payNote = new InputPasswordView.PayNote(position, 0, note, false, null);
                        payNote.setAmount((int) amout);
                        mIlvPassword.setPayNote(payNote);
                        showInputPsdView(true);
                    } else {
                        mPresenter.payNote(position, note, null);
                    }
                    mPayMusicPopWindow.hide();
                    mListPopupWindow.hide();
                })
                .buildCenterPopWindowItem2ClickListener(() -> mPayMusicPopWindow.hide())
                .buildCenterPopWindowLinkClickListener(new PayPopWindow
                        .CenterPopWindowLinkClickListener() {
                    @Override
                    public void onLongClick() {

                    }

                    @Override
                    public void onClicked() {

                    }
                })
                .build();
        mPayMusicPopWindow.show();

    }


    @Override
    public void OnPageChanged(int oldPosition, int newPosition) {
        mCurrentValue = 0;
        stopAnimation(mCurrentView);
        if (isConnected && !isComplete) {
            isMediaDataChange = true;
            if (newPosition > oldPosition) {
                getActivity().getMediaController().getTransportControls()
                        .skipToNext();
            } else {
                getActivity().getMediaController().getTransportControls()
                        .skipToPrevious();
            }
        }
    }

    @Override
    public void OnDragging(int downPosition) {
        if (!isDraging) {
            pauseAnimation();
        }
        isDraging = true;
    }

    @Override
    public void OnIdle(int position) {
        if (isMediaDataChange) {
            mCurrentView = (ViewGroup) RecyclerViewUtils.getCenterXChild
                    (mFragmentMusicPalyRv);
            isMediaDataChange = false;
        }
        mCurrentView = (ViewGroup) RecyclerViewUtils.getCenterXChild(mFragmentMusicPalyRv);
        isDraging = false;
        if (mCurrentValue != 0 || isComplete) {
            doPhonographAnimation();
        }
        isComplete = false;
    }

    @Override
    public void onDestroyView() {
        if (mFragmentMusicPalyRv != null) {
            mFragmentMusicPalyRv.addOnPageChangedListener(this);
        }
        super.onDestroyView();
    }
}
