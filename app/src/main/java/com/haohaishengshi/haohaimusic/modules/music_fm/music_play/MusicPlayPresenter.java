package com.haohaishengshi.haohaimusic.modules.music_fm.music_play;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;

import com.haohaishengshi.haohaimusic.R;
import com.haohaishengshi.haohaimusic.base.AppBasePresenter;
import com.haohaishengshi.haohaimusic.data.source.local.MusicAlbumDetailsBeanGreenDaoImpl;
import com.haohaishengshi.haohaimusic.data.source.repository.BaseMusicRepository;
import com.haohaishengshi.haohaimusic.utils.ImageUtils;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.baseproject.share.OnShareCallbackListener;
import com.zhiyicx.baseproject.share.Share;
import com.zhiyicx.baseproject.share.ShareContent;
import com.zhiyicx.baseproject.share.SharePolicy;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.ConvertUtils;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class MusicPlayPresenter extends AppBasePresenter<
        MusicPlayContract.View> implements MusicPlayContract.Presenter, OnShareCallbackListener {

    @Inject
    MusicAlbumDetailsBeanGreenDaoImpl mMusicAlbumDetailsBeanGreenDao;

    @Inject
    BaseMusicRepository mBaseMusicRepository;
    private Subscription subscribe;

    @Inject
    public MusicPlayPresenter(MusicPlayContract
                                      .View rootView) {
        super(rootView);
    }

    @Inject
    public SharePolicy mSharePolicy;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void canclePay() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }
    }

//    @Override
//    public void payNote(int position, int note,String psd) {
//
//        double amount;
//        amount = mRootView.getListDatas().get(position).getStorage().getAmount();
//        subscribe = handleIntegrationBlance((long) amount)
//                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.ts_pay_check_handle_doing)))
//                .flatMap(o -> mCommentRepository.paykNote(note, psd))
//                .subscribe(new BaseSubscribeForV2<BaseJsonV2<String>>() {
//                    @Override
//                    protected void onSuccess(BaseJsonV2<String> data) {
//                        mRootView.getListDatas().get(position).getStorage().setPaid(true);
//                        mRootView.getCurrentAblum().getMusics().get(position).getStorage().setPaid(true);
//                        mRootView.refreshData(position);
//                        EventBus.getDefault().post(mRootView.getListDatas().get(position), EventBusTagConfig.EVENT_MUSIC_TOLL);
//                        mMusicAlbumDetailsBeanGreenDao.insertOrReplace(mRootView.getCurrentAblum());
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
//                });
//        addSubscrebe(subscribe);
//    }

    @Override
    public void shareMusic(Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(mRootView.getCurrentMusic().getTitle());
        shareContent.setContent(TextUtils.isEmpty(mRootView.getCurrentMusic().getLyric()) ? mContext.getString(R.string
                .share_default, mContext.getString(R.string.app_name)) : mRootView.getCurrentMusic().getLyric());
        shareContent.setUrl(ImageUtils.imagePathConvertV2(mRootView.getCurrentMusic().getStorage().getId(), 0, 0, ImageZipConfig.IMAGE_50_ZIP));
        if (bitmap == null) {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher)));
        } else {
            shareContent.setBitmap(bitmap);
        }
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public void payNote(int position, int note, String psd) {

    }

    @Override
    public void onStart(Share share) {

    }

    @Override
    public void onSuccess(Share share) {
        mBaseMusicRepository.shareMusic(mRootView.getCurrentMusic().getId() + "");
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_sccuess));
    }

    @Override
    public void onError(Share share, Throwable throwable) {
        mRootView.showSnackErrorMessage(mContext.getString(R.string.share_fail));
    }

    @Override
    public void onCancel(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_cancel));
    }

    @Override
    public void handleLike(boolean isLiked, String music_id) {
        mBaseMusicRepository.handleLike(isLiked, music_id);
    }
}
