package com.haohaishengshi.haohaimusic.modules.music_fm.music_album_list;

import com.haohaishengshi.haohaimusic.R;
import com.haohaishengshi.haohaimusic.base.AppBasePresenter;
import com.haohaishengshi.haohaimusic.base.BaseSubscribeForV2;
import com.haohaishengshi.haohaimusic.data.beans.MusicAlbumListBean;
import com.haohaishengshi.haohaimusic.data.source.local.MusicAlbumListBeanGreenDaoImpl;
import com.haohaishengshi.haohaimusic.data.source.repository.BaseMusicRepository;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class MusicPresenter extends AppBasePresenter<MusicContract.View>
        implements MusicContract.Presenter {

    @Inject
    MusicAlbumListBeanGreenDaoImpl mMusicAlbumListDao;
    @Inject
    BaseMusicRepository mBaseMusicRepository;
    private Subscription subscribe;

    @Inject
    public MusicPresenter(MusicContract.View rootView) {
        super(rootView);
    }

    @Override
    public void canclePay() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }

    @Override
    public void payNote(int position, int note,String psd) {
        if (handleTouristControl()) {
            return;
        }
        double amount = mRootView.getListDatas().get(position).getPaid_node().getAmount();

//        subscribe = handleIntegrationBlance((long) amount)
//                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.ts_pay_check_handle_doing)))
//                .flatMap(o -> {
//                    return null;
//                        }
////                        mCommentRepository.paykNote(note,psd)
//                )
//                .subscribe(new BaseSubscribeForV2<BaseJsonV2<String>>() {
//                    @Override
//                    protected void onSuccess(BaseJsonV2<String> data) {
//                        mRootView.getListDatas().get(position).getPaid_node().setPaid(true);
//                        mRootView.refreshData(position);
//                        mMusicAlbumListDao.insertOrReplace(mRootView.getListDatas().get(position));
//                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.transaction_success));
//                        mRootView.paySuccess();
//                    }
//
//                    @Override
//                    protected void onFailure(String message, int code) {
//                        super.onFailure(message, code);
//                        if (usePayPassword()){
//                            mRootView.payFailed(message);
//                            return;
//                        }
//                        mRootView.showSnackErrorMessage(message);
//                    }
//
//                    @Override
//                    protected void onException(Throwable throwable) {
//                        super.onException(throwable);
//                        if (isIntegrationBalanceCheck(throwable)) {
//                            mRootView.paySuccess();
//                            return;
//                        }
//                        if (usePayPassword()){
//                            mRootView.payFailed(throwable.getMessage());
//                            return;
//                        }
//                        mRootView.showSnackErrorMessage(throwable.getMessage());
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        super.onCompleted();
//                        mRootView.hideCenterLoading();
//                    }
//                });
        addSubscrebe(subscribe);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Subscription subscription = (mRootView.isCollection() ? mBaseMusicRepository.getCollectMusicList(maxId, TSListFragment
                .DEFAULT_PAGE_SIZE) : mBaseMusicRepository
                .getMusicAblumList(maxId))
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<List<MusicAlbumListBean>>() {
                    @Override
                    protected void onSuccess(List<MusicAlbumListBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.onResponseError(null, false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }


                });
        addSubscrebe(subscription);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(mRootView.isCollection() ? mBaseMusicRepository.getMusicCollectAlbumFromCache(maxId) : mBaseMusicRepository
                        .getMusicAlbumFromCache(maxId),
                isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MusicAlbumListBean> data, boolean isLoadMore) {
        if (data.isEmpty()) {
            mMusicAlbumListDao.saveMultiData(data);
        } else {
            mMusicAlbumListDao.clearTable();
        }
        return false;
    }

    @Override
    public void updateOneMusic(MusicAlbumListBean albumListBean) {
        mMusicAlbumListDao.updateSingleData(albumListBean);
    }
}
