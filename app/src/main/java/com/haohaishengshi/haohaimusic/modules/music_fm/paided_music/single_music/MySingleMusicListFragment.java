package com.haohaishengshi.haohaimusic.modules.music_fm.paided_music.single_music;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.haohaishengshi.haohaimusic.R;
import com.haohaishengshi.haohaimusic.base.AppApplication;
import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumDetailsBean;
import com.haohaishengshi.haohaimusic.data.beans.MusicDetaisBean;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_album_detail.MusicDetailActivity;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_album_detail.MusicDetailFragment;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.MediaIDHelper;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_play.MusicPlayActivity;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.utils.WindowUtils;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import static com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly.PlaybackManager.MUSIC_ACTION;
import static com.haohaishengshi.haohaimusic.modules.music_fm.bak_paly.PlaybackManager.MUSIC_ACTION_BUNDLE;
import static com.haohaishengshi.haohaimusic.modules.music_fm.media_data.MusicDataConvert.METADATA_KEY_GENRE;
import static com.haohaishengshi.haohaimusic.modules.music_fm.music_album_detail.MusicDetailFragment.MUSIC_INFO;
import static com.haohaishengshi.haohaimusic.modules.music_fm.music_helper.MediaIDHelper.MEDIA_ID_MUSICS_BY_GENRE;


/**
 * @Author Jliuer
 * @Date 2017/08/24/15:06
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MySingleMusicListFragment extends TSListFragment<SingleMusicListContract.Presenter, MusicDetaisBean>
        implements SingleMusicListContract.View {

    @Inject
    SingleMusicListPresenter mSingleMusicListPresenter;

    private MusicAlbumDetailsBean mAlbumDetailsBean;
    private MusicDetailFragment.MediaBrowserProvider mCompatProvider;

    public static MySingleMusicListFragment getInstance() {
        MySingleMusicListFragment mySingleMusicListFragment = new MySingleMusicListFragment();
        return mySingleMusicListFragment;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCompatProvider = (MusicDetailFragment.MediaBrowserProvider) activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mCompatProvider.getMediaBrowser().isConnected()) {
            onConnected();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getActivity() != null) {
            MediaController controller = getActivity()
                    .getMediaController();
            if (controller != null && mAlbumDetailsBean != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MUSIC_ACTION_BUNDLE, mAlbumDetailsBean);
                controller.getTransportControls().sendCustomAction(MUSIC_ACTION, bundle);
                LogUtils.d("sendCustomAction:::onConnected");
            }
        }
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mRvList.addItemDecoration(new LinearDecoration(0, ConvertUtils.dp2px(getContext(), getItemDecorationSpacing()), 0, 0));//设置Item的间隔
    }

    @Override
    protected void initData() {
        mAlbumDetailsBean = new MusicAlbumDetailsBean();
        DaggerSingleMusicLIstComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .singleMusicPresenterModule(new SingleMusicPresenterModule(this))
                .build().inject(this);
        super.initData();
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<MusicDetaisBean> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);
        mAlbumDetailsBean.setMusics(data);

        MediaController controller = getActivity()
                .getMediaController();

        if (controller != null && mAlbumDetailsBean != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(MUSIC_ACTION_BUNDLE, mAlbumDetailsBean);
            controller.getTransportControls().sendCustomAction(MUSIC_ACTION, bundle);
            LogUtils.d("sendCustomAction:::onConnected");
        }

    }

    @Override
    protected float getItemDecorationSpacing() {
        return super.getItemDecorationSpacing();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<MusicDetaisBean>(getActivity()
                , R.layout.item_music_detail_list, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, MusicDetaisBean item, int
                    position) {
                TextView musicName = holder.getView(R.id.item_music_name);
                TextView authorName = holder.getView(R.id.item_music_author);
                String music_name = item.getTitle();
                musicName.setText(music_name);
                authorName.setText("-" + item.getSinger().getName());
                musicName.setText(music_name);
                musicName.setPadding(ConvertUtils.px2dp(getContext(), 15), 0, 0, 0);
//                if (item.getStorage().getAmount() != 0) {// 有收费
//                    Drawable top_drawable = getResources().getCurrentPlayGif(R.mipmap.musici_pic_pay02);//  musicName.getLineHeight()
//                    top_drawable.setBounds(0, 0, top_drawable.getIntrinsicWidth(), top_drawable.getIntrinsicHeight());
//                    ImageSpan imgSpan = new CenterImageSpan(top_drawable);
//                    SpannableString spannableString = SpannableString.valueOf("T" + music_name);
//                    spannableString.setSpan(imgSpan, 0, 1, Spannable
//                            .SPAN_EXCLUSIVE_EXCLUSIVE);
//                    musicName.setText(spannableString);
//                } else {
//
//                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                MusicDetaisBean item = mListDatas.get(position);

                Intent intent = new Intent(getActivity(), MusicPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(MUSIC_INFO, mAlbumDetailsBean);
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

                String id = MediaIDHelper.createMediaID("" + item.getId(),
                        MEDIA_ID_MUSICS_BY_GENRE, METADATA_KEY_GENRE);
                controller.getTransportControls()
                        .playFromMediaId(id, null);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return false;
            }
        });
        return adapter;
    }

    public void onConnected() {
        if (isDetached()) {
            return;
        }
        MediaController controller = getActivity()
                .getMediaController();
        if (controller != null) {
            if (mAlbumDetailsBean != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MUSIC_ACTION_BUNDLE, mAlbumDetailsBean);
                controller.getTransportControls().sendCustomAction(MUSIC_ACTION, bundle);
                LogUtils.d("sendCustomAction:::onConnected");
            }

        }

    }

}
