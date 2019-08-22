package com.haohaishengshi.haohaimusic.modules.music_fm.music_album_list;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.utils.WindowUtils;
import com.zhiyicx.baseproject.widget.InputPasswordView;
import com.zhiyicx.baseproject.widget.popwindow.PayPopWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.TGridDecoration;
import com.haohaishengshi.haohaimusic.R;
import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumListBean;
import com.haohaishengshi.haohaimusic.modules.music_fm.music_album_detail.MusicDetailActivity;
import com.haohaishengshi.haohaimusic.modules.music_fm.paided_music.MyMusicActivity;
import com.haohaishengshi.haohaimusic.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.haohaishengshi.haohaimusic.config.EventBusTagConfig.EVENT_ABLUM_COLLECT;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicListFragment extends TSListFragment<MusicContract.Presenter, MusicAlbumListBean>
        implements MusicContract.View {

    public static final String BUNDLE_MUSIC_ABLUM = "music_ablum";

    /**
     * 数量改变 event_bus 来的
     */
    private MusicAlbumListBean mMusicAlbumListBean;

    private PayPopWindow mPayMusicPopWindow;

    private InputPasswordView.PayNote mPayNote;

    @Override
    public void onResume() {
        super.onResume();
        for (MusicAlbumListBean musicListBean : mListDatas) {
            WindowUtils.AblumHeadInfo ablumHeadInfo = WindowUtils.getAblumHeadInfo();
            if (ablumHeadInfo != null && ablumHeadInfo.getAblumId() == musicListBean.getId()) {
                musicListBean.setComment_count(ablumHeadInfo.getCommentCount());
                musicListBean.setTaste_count(ablumHeadInfo.getListenCount());
                musicListBean.setShare_count(ablumHeadInfo.getShareCount());
                musicListBean.setCollect_count(ablumHeadInfo.getLikeCount());
            }
        }
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        if (mToolbarRight != null && setRightImg() > 0) {
            mToolbarRight.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), setRightImg(), Color.BLACK), null);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mRvList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRvList.setPadding(20, 20, 0, 0);
        mRvList.addItemDecoration(new TGridDecoration(20, 20, true));
        mRvList.setBackgroundColor(0xffffffff);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<MusicAlbumListBean> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);
        if (mListDatas.isEmpty()) {
            mRvList.setBackground(null);
        }
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.ico_me_music;
    }

    @Override
    protected void setRightClick() {
        startActivity(new Intent(mActivity, MyMusicActivity.class));
    }

    @Override
    protected boolean showNoMoreData() {
        return mListDatas.size() >= DEFAULT_ONE_PAGE_SHOW_MAX_SIZE;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getContext(), 2);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.music_fm);
    }

    @Override
    protected CommonAdapter<MusicAlbumListBean> getAdapter() {
        final int width = (DeviceUtils.getScreenWidth(getActivity()) - 60) / 2;
        CommonAdapter<MusicAlbumListBean> comAdapter = new CommonAdapter<MusicAlbumListBean>
                (getActivity(), R.layout.item_music_list, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, MusicAlbumListBean musicListBean, int
                    position) {
                ImageView imag = holder.getView(R.id.music_list_image);
                holder.setVisible(R.id.music_list_toll_flag, musicListBean.getPaid_node() == null
                        /* || !(musicListBean.getPaid_node() != null  && !musicListBean.getPaid_node().isPaid())*/ ? View.GONE : View.VISIBLE);
             String url=   ImageUtils.imagePathConvertV2(musicListBean.getStorage().getId(), width, width,
                        ImageZipConfig.IMAGE_70_ZIP);
                Glide.with(getContext())
                        .load(url)
                        .placeholder(R.drawable.shape_default_image)
                        .override(width, width)
                        .error(R.drawable.shape_default_image)
                        .into(imag);


                holder.setText(R.id.music_list_taste_count, "" + musicListBean.getTaste_count());
                holder.setText(R.id.music_list_title, musicListBean.getTitle());

            }
        };

        comAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                MusicAlbumListBean albumListBean = mListDatas.get(position);
                if (albumListBean.getPaid_node() != null && !albumListBean.getPaid_node().isPaid()) {
                    initMusicCenterPopWindow(position, albumListBean.getPaid_node().getAmount(),
                            albumListBean.getPaid_node().getNode(), R.string.buy_pay_music_ablum_desc);
                    return;
                }

                toMusicDetailActivity(albumListBean);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return false;
            }
        });
        return comAdapter;
    }

    private void toMusicDetailActivity(MusicAlbumListBean albumListBean) {
        Intent intent = new Intent(getActivity(), MusicDetailActivity.class);
        Bundle bundle = new Bundle();

        bundle.putParcelable(BUNDLE_MUSIC_ABLUM, albumListBean);
        intent.putExtra(BUNDLE_MUSIC_ABLUM, bundle);
        WindowUtils.AblumHeadInfo ablumHeadInfo = new WindowUtils.AblumHeadInfo();
        ablumHeadInfo.setCommentCount(albumListBean.getComment_count());
        ablumHeadInfo.setShareCount(albumListBean.getShare_count());
        ablumHeadInfo.setListenCount(albumListBean.getTaste_count());
        ablumHeadInfo.setAblumId(albumListBean.getId());
        ablumHeadInfo.setLikeCount(albumListBean.getCollect_count());
        WindowUtils.setAblumHeadInfo(ablumHeadInfo);
        startActivity(intent);
    }

    @Override
    public void paySuccess() {
        super.paySuccess();
        if (mPayNote != null) {
            MusicAlbumListBean albumListBean = mListDatas.get(mPayNote.dynamicPosition);
            if (albumListBean.getPaid_node() != null && !albumListBean.getPaid_node().isPaid()) {
                return;
            }
            toMusicDetailActivity(albumListBean);
        }
    }

    @Override
    protected Long getMaxId(@NotNull List<MusicAlbumListBean> data) {
        return (long) data.get(data.size() - 1).getId();
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscriber(tag = EVENT_ABLUM_COLLECT, mode = ThreadMode.MAIN)
    public void onCollectCountUpdate(MusicAlbumListBean e_albumListBean) {
        mMusicAlbumListBean = e_albumListBean;
        Observable.from(mListDatas).filter(albumListBean -> mMusicAlbumListBean.getId() == albumListBean.getId()).subscribe(new Action1<MusicAlbumListBean>() {
            @Override
            public void call(MusicAlbumListBean albumListBean_same) {
                albumListBean_same.setCollect_count(mMusicAlbumListBean.getCollect_count());
                albumListBean_same.setShare_count(mMusicAlbumListBean.getShare_count());
                albumListBean_same.setHas_collect(mMusicAlbumListBean.getHas_collect());
                albumListBean_same.setComment_count(mMusicAlbumListBean.getComment_count());
                albumListBean_same.setTaste_count(mMusicAlbumListBean.getTaste_count());
                mPresenter.updateOneMusic(albumListBean_same);
                mHeaderAndFooterWrapper.notifyDataSetChanged();
            }
        });
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
        mPayNote = payNote;
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
                        InputPasswordView.PayNote payNote = new InputPasswordView.PayNote(position, 0, note, false, null);
                        payNote.setAmount(amout);
                        mIlvPassword.setPayNote(payNote);
                        showInputPsdView(true);
                    } else {
                        mPresenter.payNote(position, note, null);
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

    @Override
    public boolean isCollection() {
        return false;
    }
}
