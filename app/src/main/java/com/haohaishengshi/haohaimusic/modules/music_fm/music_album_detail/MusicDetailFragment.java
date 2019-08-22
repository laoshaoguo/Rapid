package com.haohaishengshi.haohaimusic.modules.music_fm.music_album_detail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadata;
import android.media.browse.MediaBrowser;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.haohaishengshi.haohaimusic.R;
import com.haohaishengshi.haohaimusic.base.AppApplication;
import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumDetailsBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumListBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicDetaisBean;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_comment.MusicCommentActivity;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_comment.MusicCommentHeader;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.MediaIDHelper;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_play.MusicPlayActivity;
import com.haohaishengshi.haohaimusic.utils.ImageUtils;
import com.haohaishengshi.haohaimusic.widget.IconTextView;
import com.haohaishengshi.haohaimusic.widget.NestedScrollLineayLayout;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideStokeTransform;
import com.zhiyicx.baseproject.utils.WindowUtils;
import com.zhiyicx.baseproject.widget.InputPasswordView;
import com.zhiyicx.baseproject.widget.popwindow.PayPopWindow;
import com.zhiyicx.baseproject.widget.textview.CenterImageSpan;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.FastBlur;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;

import static com.haohaishengshi.haohaimusic.config.EventBusTagConfig.EVENT_MUSIC_CHANGE;
import static com.haohaishengshi.haohaimusic.config.EventBusTagConfig.EVENT_MUSIC_COMMENT_COUNT;
import static com.haohaishengshi.haohaimusic.config.EventBusTagConfig.EVENT_MUSIC_LIKE;
import static com.haohaishengshi.haohaimusic.config.EventBusTagConfig.EVENT_MUSIC_TOLL;
import static com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly.PlaybackManager.MUSIC_ACTION;
import static com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly.PlaybackManager.MUSIC_ACTION_BUNDLE;
import static com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly.PlaybackManager.MUSIC_ID;
import static com.haohaishengshi.haohaimusic.modules.music_fm.media_data.MusicDataConvert.METADATA_KEY_GENRE;
import static com.haohaishengshi.haohaimusic.modules.music_fm.music_album_list.MusicListFragment.BUNDLE_MUSIC_ABLUM;
import static com.haohaishengshi.haohaimusic.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT;
import static com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.MediaIDHelper.MEDIA_ID_MUSICS_BY_GENRE;
import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description 专辑详情
 */
public class MusicDetailFragment extends TSFragment<MusicDetailContract.Presenter> implements
        MusicDetailContract.View {

    @BindView(R.id.fragment_music_detail_head_iamge)
    ImageView mFragmentMusicDetailHeadIamge;
    @BindView(R.id.fragment_music_detail_name)
    TextView mFragmentMusicDetailName;
    @BindView(R.id.fragment_music_detail_dec)
    TextView mFragmentMusicDetailDec;
    @BindView(R.id.nestedscroll_target)
    RelativeLayout mFragmentMusicDetailHeadInfo;
    @BindView(R.id.rv_music_detail_list)
    RecyclerView mRvMusicDetailList;
    @BindView(R.id.fragment_music_detail_back)
    TextView mFragmentMusicDetailBack;
    @BindView(R.id.fragment_music_detail_scrollview)
    NestedScrollLineayLayout mFragmentMusicDetailScrollview;

    @BindView(R.id.fragment_music_detail_title)
    RelativeLayout mFragmentMusicDetailTitle;
    @BindView(R.id.fragment_music_detail_empty)
    View mFragmentMusicDetailEmpty;
    @BindView(R.id.fragment_music_detail_center_title)
    TextView mFragmentMusicDetailCenterTitle;
    @BindView(R.id.fragment_music_detail_center_sub_title)
    TextView mFragmentMusicDetailCenterSubTitle;
    @BindView(R.id.fragment_music_detail_playvolume)
    IconTextView mFragmentMusicDetailPlayvolume;
    @BindView(R.id.fragment_music_detail_share)
    IconTextView mFragmentMusicDetailShare;
    @BindView(R.id.fragment_music_detail_comment)
    IconTextView mFragmentMusicDetailComment;
    @BindView(R.id.fragment_music_detail_favorite)
    IconTextView mFragmentMusicDetailFavorite;
    @BindView(R.id.fragment_music_detail_music_count)
    TextView fragmentMusicDetailMusicCount;
    @BindView(R.id.fragment_album_detail)
    FrameLayout fragmentAlbumDetail;

    private CommonAdapter mAdapter;
    private List<MusicDetaisBean> mAdapterList = new ArrayList<>();

    public static final String ARG_MEDIA_ID = "media_id";
    public static final String MUSIC_INFO = "music_info";
    private String mCurrentMediaId = "-1";
    private Bitmap mBgBitmap;

    private String mMediaId;
    private String mMediaId_test;
    private Palette mPalette;

    public static final int STATE_NONE = 0;
    public static final int STATE_PLAYABLE = 1;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_PLAYING = 3;

    private MediaBrowserProvider mCompatProvider;
    private MusicAlbumListBean mMusicAlbumListBean;
    private MusicAlbumDetailsBean mAlbumDetailsBean;
    private PayPopWindow mPayMusicPopWindow;

    /**
     * 音乐切换回掉
     */
    private final MediaController.Callback mMediaControllerCallback =
            new MediaController.Callback() {
                @Override
                public void onMetadataChanged(@Nullable MediaMetadata metadata) {
                    super.onMetadataChanged(metadata);
                    if (metadata == null) {
                        return;
                    }
                    mCurrentMediaId = metadata.getDescription().getMediaId();
                    LogUtils.d("onMetadataChanged::detail:" + mCurrentMediaId);
                    mAdapter.notifyDataSetChanged();
                    mPresenter.getMusicDetails(mCurrentMediaId);
                }

                @Override
                public void onPlaybackStateChanged(@NonNull PlaybackState state) {
                    super.onPlaybackStateChanged(state);
                }
            };

    /**
     * 音乐数据变更回掉
     */
    private final MediaBrowser.SubscriptionCallback mSubscriptionCallback =
            new MediaBrowser.SubscriptionCallback() {
                @Override
                public void onChildrenLoaded(@NonNull String parentId,
                                             @NonNull List<MediaBrowser.MediaItem> children) {
                    if (children.isEmpty()) {
                        return;
                    }
                    MediaSession.QueueItem mCurrentMusic = AppApplication.getmQueueManager().getCurrentMusic();
                    if (mCurrentMusic != null) {
                        mCurrentMediaId = MediaIDHelper.extractMusicIDFromMediaID(mCurrentMusic.getDescription().getMediaId());
                    }
                    fragmentMusicDetailMusicCount.setText(String.format("(共%d首)", children.size()));
                    LogUtils.d("onChildrenLoaded:::" + children.size());
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(@NonNull String id) {

                }
            };

    public static MusicDetailFragment newInstance(Bundle param) {
        MusicDetailFragment fragment = new MusicDetailFragment();
        fragment.setArguments(param);
        return fragment;
    }


    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_music_detail;
    }

    @Override
    protected void initView(View rootView) {
        ViewGroup.LayoutParams titleParam;

        int titleHeight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            titleHeight = ConvertUtils.dp2px(getActivity(), 84);
        } else {
            mFragmentMusicDetailEmpty.setVisibility(View.GONE);
            titleHeight = ConvertUtils.dp2px(getActivity(), 64);
        }
        titleParam = new FrameLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, titleHeight);
        mFragmentMusicDetailScrollview.setNotConsumeHeight(titleHeight);
        mFragmentMusicDetailTitle.setLayoutParams(titleParam);
        closeLoadingView();
    }

    @Override
    public void onResume() {
        super.onResume();
        initHeadInfo(mMusicAlbumListBean);
    }

    @Override
    protected void initData() {
        mMusicAlbumListBean = getArguments().getParcelable(BUNDLE_MUSIC_ABLUM);
        mAlbumDetailsBean = mPresenter.getCacheAblumDetail(mMusicAlbumListBean.getId());
        initHeadInfo(mMusicAlbumListBean);
        mPresenter.getMusicAblum(mMusicAlbumListBean.getId() + "");
        mAdapter = getCommonAdapter();
        mRvMusicDetailList.setAdapter(mAdapter);
        mRvMusicDetailList.setLayoutManager(new LinearLayoutManager(getActivity()));
        initTitle();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCompatProvider = (MediaBrowserProvider) activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mCompatProvider.getMediaBrowser().isConnected()) {
            onConnected();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCompatProvider.getMediaBrowser() != null && mCompatProvider.getMediaBrowser()
                .isConnected() && mMediaId != null) {
            mCompatProvider.getMediaBrowser().unsubscribe(mMediaId);
        }
        MediaController controller = getActivity().getMediaController();
        if (controller != null) {
            controller.unregisterCallback(mMediaControllerCallback);
        }
    }


    @Override
    public void setPresenter(MusicDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setMusicAblum(MusicAlbumDetailsBean musicAblum) {
        closeLoadingView();
        mAlbumDetailsBean = musicAblum;
        // 模拟测试数据，这个音乐播放我也不知道要怎么，一会儿好一会儿坏
//        MusicAlbumDetailsBean.MusicsBean.MusicsBeanIdStorage storage=new
//                MusicAlbumDetailsBean.MusicsBean.MusicsBeanIdStorage();
//        storage.setAmount(10);
//        storage.setId(112);
//        storage.setPaid(false);
//        musicAblum.getMusics().get(0).setStorage(storage);
        mAdapter.dataChange(musicAblum.getMusics());
        WindowUtils.AblumHeadInfo ablumHeadInfo = new WindowUtils.AblumHeadInfo();
        ablumHeadInfo.setCommentCount(musicAblum.getComment_count());
        ablumHeadInfo.setShareCount(musicAblum.getShare_count());
        ablumHeadInfo.setListenCount(musicAblum.getTaste_count());
        ablumHeadInfo.setAblumId(musicAblum.getId().intValue());
        ablumHeadInfo.setLikeCount(musicAblum.getCollect_count());
        WindowUtils.setAblumHeadInfo(ablumHeadInfo);

        mFragmentMusicDetailCenterTitle.setText(mAlbumDetailsBean.getTitle());
        mFragmentMusicDetailCenterSubTitle.setText(mAlbumDetailsBean.getIntro());
        mFragmentMusicDetailDec.setText(mAlbumDetailsBean.getIntro());

        mMusicAlbumListBean.setHas_collect(mAlbumDetailsBean.getHas_collect());

        if (mAlbumDetailsBean.getHas_collect()) {
            mFragmentMusicDetailFavorite.setIconRes(R.mipmap.detail_ico_collect);
        }
        if (mCompatProvider.getMediaBrowser().isConnected()) {
            onConnected();
        }
    }

    @Override
    public List<MusicDetaisBean> getListDatas() {
        return mAdapter.getDatas();
    }

    @Override
    public void refreshData(int position) {
        mAdapter.notifyItemChanged(position);
        if (mAlbumDetailsBean != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(MUSIC_ACTION_BUNDLE, mAlbumDetailsBean);
            MediaController controller = getActivity()
                    .getMediaController();
            controller.getTransportControls().sendCustomAction(MUSIC_ACTION, bundle);

            mCompatProvider.getMediaBrowser().unsubscribe(mMediaId);
            mCompatProvider.getMediaBrowser().subscribe(mMediaId, mSubscriptionCallback);
        }
    }

    @Override
    public void setCollect(boolean isCollected) {
        if (isCollected) {
            mFragmentMusicDetailFavorite.setIconRes(R.mipmap.detail_ico_collect);
        } else {
            mFragmentMusicDetailFavorite.setIconRes(R.mipmap.music_ico_collect);
        }
        mFragmentMusicDetailFavorite.setText(mAlbumDetailsBean.getCollect_count() + "");
    }

    @Override
    protected void musicWindowsStatus(boolean isShow) {
        super.musicWindowsStatus(isShow);
        WindowUtils.changeToWhiteIcon();
    }

    @Override
    protected boolean setUseCenterLoadingAnimation() {
        return false;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    public void albumHasBeDeleted() {
        fragmentAlbumDetail.setVisibility(View.GONE);
        setLoadViewHolderImag(R.mipmap.img_default_delete);
        showLoadViewLoadErrorDisableClick();
    }

    @Override
    protected void setLoadingViewHolderClick() {
        super.setLoadingViewHolderClick();
        mPresenter.getMusicAblum(mMusicAlbumListBean.getId() + "");
    }

    @Override
    public void noNetWork() {
//        fragmentAlbumDetail.setVisibility(View.GONE);
        setLoadViewHolderImag(R.mipmap.img_default_internet);
        showLoadViewLoadErrorDisableClick(true);
    }

    @Override
    protected int getstatusbarAndToolbarHeight() {
        return 0;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    public String getMediaId() {
        return mMediaId;
    }

    public void setMediaId(String mediaId) {
        mMediaId = mediaId;
    }

    public void onConnected() {
        if (isDetached()) {
            return;
        }
        mMediaId = getMediaId();
        if (mMediaId == null) {
            mMediaId = mCompatProvider.getMediaBrowser().getRoot();
        }

        MediaController controller = getActivity()
                .getMediaController();

        if (controller != null) {
            controller.registerCallback(mMediaControllerCallback);
            if (mAlbumDetailsBean != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MUSIC_ACTION_BUNDLE, mAlbumDetailsBean);
                controller.getTransportControls().sendCustomAction(MUSIC_ACTION, bundle);
                LogUtils.d("sendCustomAction:::onConnected");

                mCompatProvider.getMediaBrowser().unsubscribe(mMediaId);
                mCompatProvider.getMediaBrowser().subscribe(mMediaId, mSubscriptionCallback);
            }

        }

    }

    @NonNull
    private CommonAdapter<MusicDetaisBean> getCommonAdapter() {
        mAdapter = new CommonAdapter<MusicDetaisBean>(getActivity(), R.layout
                .item_music_detail_list, mAdapterList) {
            @Override
            protected void convert(ViewHolder holder, MusicDetaisBean item, int
                    position) {
                TextView musicName = holder.getView(R.id.item_music_name);
                TextView authorName = holder.getView(R.id.item_music_author);

                String music_name = item.getTitle();

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

                if (mCurrentMediaId.equals(item.getId())) {
                    musicName.setTextColor(getResources().getColor(R.color.important_for_theme));
                    authorName.setTextColor(getResources().getColor(R.color.important_for_theme));
                } else {
                    musicName.setTextColor(getResources().getColor(R.color.important_for_content));
                    authorName.setTextColor(getResources().getColor(R.color
                            .normal_for_assist_text));
                }
            }
        };

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (TouristConfig.MUSIC_CAN_PLAY || !mPresenter.handleTouristControl()) {

                    MusicDetaisBean item = mAdapterList.get(position);

                    if (item.getStorage().getAmount() != 0 && !item.getStorage().isPaid()) {
                        initMusicCenterPopWindow(position, item.getStorage().getAmount(),
                                item.getStorage().getPaid_node(), R.string.buy_pay_single_music_desc);
                        return;
                    }

                    Intent intent = new Intent(getActivity(), MusicPlayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(MUSIC_INFO, mAlbumDetailsBean);
                    bundle.putString(ARG_MEDIA_ID, item.getId() + "");
                    intent.putExtra(MUSIC_INFO, bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    WindowUtils.setMusicAlbumDetailsBean(bundle);
                    MediaController controller = getActivity().getMediaController();
                    MediaMetadata metadata = controller.getMetadata();
                    if (metadata != null) {
                        intent.putExtra(MusicDetailActivity.EXTRA_CURRENT_MEDIA_DESCRIPTION,
                                metadata.getDescription());
                    }
                    startActivity(intent);
                    MediaSession.QueueItem mCurrentMusic = AppApplication.getmQueueManager().getCurrentMusic();
                    if (mCurrentMusic != null) {
                        mMediaId = mCurrentMusic.getDescription().getMediaId();
                    }


                    String id = MediaIDHelper.createMediaID("" + item.getId(),
                            MEDIA_ID_MUSICS_BY_GENRE, METADATA_KEY_GENRE);
                    mMediaId_test = id;
                    controller.getTransportControls()
                            .playFromMediaId(id, null);

                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return false;
            }
        });

        return mAdapter;
    }

    public int getMediaItemState(MediaBrowser.MediaItem mediaItem) {
        int state = STATE_NONE;
        if (mediaItem.isPlayable()) {
            state = STATE_PLAYABLE;
            if (MediaIDHelper.isMediaItemPlaying(getActivity(), mediaItem)) {
                state = getStateFromController();
            }
        }
        return state;
    }

    public int getStateFromController() {
        MediaController controller = getActivity().getMediaController();
        PlaybackState pbState = controller.getPlaybackState();
        if (pbState == null ||
                pbState.getState() == PlaybackState.STATE_ERROR) {
            return STATE_NONE;
        } else if (pbState.getState() == PlaybackState.STATE_PLAYING) {
            return STATE_PLAYING;
        } else {
            return STATE_PAUSED;
        }
    }

    @Override
    public MusicAlbumDetailsBean getCurrentAblum() {
        return mAlbumDetailsBean;
    }

    @Override
    public MusicAlbumListBean getmMusicAlbumListBean() {
        return mMusicAlbumListBean;
    }

    private void initTitle() {
        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.default_pic_personal);
        mPalette = Palette.from(mBgBitmap).generate();
        BitmapDrawable drawable = new BitmapDrawable(FastBlur.blurBitmap
                (mBgBitmap, mBgBitmap.getWidth(), mBgBitmap.getHeight()));
        mFragmentMusicDetailHeadInfo.setBackgroundDrawable(drawable);

        mFragmentMusicDetailScrollview.setOnHeadFlingListener(new NestedScrollLineayLayout
                .OnHeadFlingListener() {

            @Override
            public void onHeadFling(int scrollY) {

                int distance = mFragmentMusicDetailScrollview.getTopViewHeight();
                int alpha = 255 * scrollY / distance;
                alpha = alpha > 255 ? 255 : alpha;
                mFragmentMusicDetailTitle.setBackgroundColor(mPalette.getDarkMutedColor
                        (0xffdedede));
                if ((float) alpha / 255f > 0.7) {
                    mFragmentMusicDetailCenterTitle.setVisibility(View.VISIBLE);
                    mFragmentMusicDetailCenterSubTitle.setVisibility(View.VISIBLE);
                } else {
                    mFragmentMusicDetailCenterTitle.setVisibility(View.GONE);
                    mFragmentMusicDetailCenterSubTitle.setVisibility(View.GONE);
                }
                mFragmentMusicDetailTitle.getBackground().setAlpha(alpha);
            }

            @Override
            public void onHeadZoom() {
                mPresenter.getMusicDetails(mCurrentMediaId);
            }

            @Override
            public void onHeadRedu() {

            }
        });
    }

    private void initHeadInfo(MusicAlbumListBean albumListBean) {
        int imageSize = getResources().getDimensionPixelSize(R.dimen.music_album_detail_head);
        Glide.with(getContext())
                .load(ImageUtils.imagePathConvertV2(albumListBean.getStorage().getId(), imageSize, imageSize,
                        ImageZipConfig.IMAGE_70_ZIP))
                .asBitmap()
                .transform(new GlideStokeTransform(getActivity(), 5))
                .placeholder(R.drawable.shape_default_image)
                .error(R.drawable.shape_default_image)
                .into(new ImageViewTarget<Bitmap>(mFragmentMusicDetailHeadIamge) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        mBgBitmap = resource.copy(Bitmap.Config.ARGB_8888, false);
                        mFragmentMusicDetailHeadIamge.setImageBitmap(resource);
                        mPalette = Palette.from(mBgBitmap).generate();
                        BitmapDrawable drawable = new BitmapDrawable(FastBlur.blurBitmap
                                (mBgBitmap, mBgBitmap.getWidth(), mBgBitmap.getHeight()));
                        mFragmentMusicDetailHeadInfo.setBackgroundDrawable(drawable);
                    }

                });
        if (WindowUtils.getAblumHeadInfo() != null) {
            WindowUtils.AblumHeadInfo ablumHeadInfo = WindowUtils.getAblumHeadInfo();
            albumListBean.setCollect_count(ablumHeadInfo.getLikeCount());
            albumListBean.setShare_count(ablumHeadInfo.getShareCount());
            albumListBean.setTaste_count(ablumHeadInfo.getListenCount());
            albumListBean.setComment_count(ablumHeadInfo.getCommentCount());
        }
        mFragmentMusicDetailName.setText(albumListBean.getTitle());
        mFragmentMusicDetailShare.setText(albumListBean.getShare_count() + "");
        mFragmentMusicDetailComment.setText(albumListBean.getComment_count() + "");
        mFragmentMusicDetailFavorite.setText(albumListBean.getCollect_count() + "");
        mFragmentMusicDetailPlayvolume.setText(albumListBean.getTaste_count() + "");

    }

    public interface MediaBrowserProvider {
        MediaBrowser getMediaBrowser();
    }

    @OnClick({R.id.fragment_music_detail_playvolume, R.id.fragment_music_detail_share, R.id
            .fragment_music_detail_comment, R.id.fragment_music_detail_favorite,
            R.id.fragment_music_detail_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_music_detail_playvolume:
                break;
            case R.id.fragment_music_detail_share:
                mPresenter.shareMusicAlbum(mBgBitmap);
                break;
            case R.id.fragment_music_detail_comment:
                if (!mPresenter.handleTouristControl()) {
                    Intent intent = new Intent(getActivity(), MusicCommentActivity.class);
                    Bundle musicBundle = new Bundle();
                    MusicCommentHeader.HeaderInfo headerInfo = new MusicCommentHeader.HeaderInfo();
                    headerInfo.setCommentCount(mMusicAlbumListBean.getComment_count());
                    headerInfo.setId(mMusicAlbumListBean.getId());
                    headerInfo.setTitle(mMusicAlbumListBean.getTitle());
                    headerInfo.setLitenerCount(mMusicAlbumListBean.getTaste_count() + "");
                    headerInfo.setImageUrl(ImageUtils.imagePathConvertV2(mMusicAlbumListBean.getStorage().getId()
                            , mMusicAlbumListBean.getStorage().getWidth()
                            , mMusicAlbumListBean.getStorage().getHeight()
                            , ImageZipConfig.IMAGE_70_ZIP));
                    musicBundle.putSerializable(CURRENT_COMMENT, headerInfo);
                    intent.putExtra(CURRENT_COMMENT, musicBundle);
                    startActivity(intent);
                }
                break;
            case R.id.fragment_music_detail_favorite:
                if (!mPresenter.handleTouristControl()) {
                    mPresenter.handleCollect(!mAlbumDetailsBean.getHas_collect(), mAlbumDetailsBean.getId() + "");
                }
                break;
            case R.id.fragment_music_detail_back:
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    @Subscriber(tag = EVENT_MUSIC_LIKE, mode = ThreadMode.MAIN)
    public void onLikeCountUpdate(final MusicDetaisBean e_albumListBean) {
        if (mAlbumDetailsBean != null) {
            Observable.from(mAlbumDetailsBean.getMusics())
                    .filter(musicsBean -> e_albumListBean.getId() == musicsBean.getId())
                    .subscribe(musicsBean -> {
                        musicsBean.setHas_like(e_albumListBean.isHas_like());
                        musicsBean.setComment_count(e_albumListBean.getComment_count());
                        musicsBean.setStorage(e_albumListBean.getStorage());
                    });

        }
        LogUtils.d("EVENT_MUSIC_LIKE");
    }

    @Subscriber(tag = EVENT_MUSIC_TOLL, mode = ThreadMode.MAIN)
    public void onTollUpdate(final MusicDetaisBean e_albumListBean) {
        if (mAlbumDetailsBean != null) {
            Observable.from(mAlbumDetailsBean.getMusics())
                    .filter(musicsBean -> e_albumListBean.getId().intValue() == musicsBean.getId().intValue())
                    .subscribe(musicsBean -> {
                        musicsBean.setHas_like(e_albumListBean.isHas_like());
                        musicsBean.setComment_count(e_albumListBean.getComment_count());
                        musicsBean.setStorage(e_albumListBean.getStorage());
                    });

            Bundle bundle = new Bundle();
            bundle.putSerializable(MUSIC_ACTION_BUNDLE, mAlbumDetailsBean);
            String id = MediaIDHelper.createMediaID("" + e_albumListBean.getId(),
                    MEDIA_ID_MUSICS_BY_GENRE, METADATA_KEY_GENRE);
            bundle.putString(MUSIC_ID, id);

            MediaController controller = getActivity()
                    .getMediaController();
            controller.getTransportControls().sendCustomAction(MUSIC_ACTION, bundle);
        }
        LogUtils.d("EVENT_MUSIC_TOLL");
    }

    @Subscriber(tag = EVENT_MUSIC_COMMENT_COUNT, mode = ThreadMode.MAIN)
    public void onCommentCountUpdate(MusicCommentHeader.HeaderInfo headerInfo) {
        mMusicAlbumListBean.setComment_count((mMusicAlbumListBean.getComment_count() + 1));
        mFragmentMusicDetailComment.setText(mMusicAlbumListBean.getComment_count() + "");
        LogUtils.d("EVENT_MUSIC_COMMENT_COUNT");
    }

    @Subscriber(tag = EVENT_MUSIC_CHANGE, mode = ThreadMode.MAIN)
    public void onMusicChange(int change) {
        mCurrentMediaId = change + "";
        mPresenter.getMusicDetails(change + "");// 增加收听数量
        mAdapter.notifyDataSetChanged();
        LogUtils.d("EVENT_MUSIC_CHANGE");
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

    private void initMusicCenterPopWindow(final int position, int amout,
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
                .buildDescrStr(String.format(getString(strRes), amout, mPresenter.getGoldName()))
                .buildLinksStr(getString(R.string.buy_pay_member))
                .buildTitleStr(getString(R.string.buy_pay))
                .buildItem1Str(getString(R.string.buy_pay_in))
                .buildItem2Str(getString(R.string.buy_pay_out))
                .buildMoneyStr(getString(R.string.buy_pay_integration, "" + amout))
                .buildCenterPopWindowItem1ClickListener(() -> {
                    if (mPresenter.usePayPassword()) {
                        InputPasswordView.PayNote payNote=new  InputPasswordView.PayNote(position, 0, note, false, null);
                        payNote.setAmount(amout);
                        mIlvPassword.setPayNote(payNote);
                        showInputPsdView(true);
                    } else {
                        mPresenter.payNote(position, note,null);
                    }
                    mPayMusicPopWindow.hide();
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
}
