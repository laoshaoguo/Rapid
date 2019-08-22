package com.haohaishengshi.haohaimusic.base;

import android.text.TextUtils;

import com.haohaishengshi.haohaimusic.R;
import com.haohaishengshi.haohaimusic.data.beans.UserInfoBean;
import com.haohaishengshi.haohaimusic.data.source.local.UserInfoBeanGreenDaoImpl;
import com.haohaishengshi.haohaimusic.data.source.repository.AuthRepository;
import com.haohaishengshi.haohaimusic.data.source.repository.SystemRepository;
import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

import javax.inject.Inject;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/16
 * @Contact master.jungle68@gmail.com
 */

public abstract class AppBasePresenter<V extends IBaseView> extends BasePresenter<V> implements IBaseTouristPresenter {
    private static final String DEFAULT_WALLET_EXCEPTION_MESSAGE = "balance_check";
    private static final String DEFAULT_INTEGRATION_EXCEPTION_MESSAGE = "integration_check";
    @Inject
    protected AuthRepository mAuthRepository;

    @Inject
    protected UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    protected SystemRepository mSystemRepository;

    public AppBasePresenter(V rootView) {
        super(rootView);
    }

    @Override
    public boolean isTourist() {
        return mAuthRepository == null || mAuthRepository.isTourist();
    }

    @Override
    public boolean isLogin() {
        return mAuthRepository != null && mAuthRepository.isLogin();
    }

    @Override
    public boolean usePayPassword() {
        return getSystemConfigBean().isUsePayPassword();
    }

    @Override
    public boolean userHasPassword() {
        boolean hasPsd = false;
        if (handleTouristControl()) {
            return false;
        }
        UserInfoBean userInfoBean = null;
        String phone = null;
        if (AppApplication.getmCurrentLoginAuth() != null && AppApplication.getmCurrentLoginAuth().getUser() != null) {
            userInfoBean = AppApplication.getmCurrentLoginAuth().getUser();
            phone = userInfoBean.getPhone();
            hasPsd = AppApplication.getmCurrentLoginAuth().getUser().getInitial_password();
        }
//        if (!hasPsd) {
//            Activity activity = ((BaseFragment) mRootView).getActivity();
//            if (activity != null) {
//                Intent intent = new Intent(activity, InitPasswordActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt(BUNDLE_BIND_TYPE, DEAL_TYPE_PSD);
//                bundle.putBoolean(BUNDLE_BIND_STATE, !TextUtils.isEmpty(phone));
//                bundle.putParcelable(BUNDLE_BIND_DATA, userInfoBean);
//                intent.putExtras(bundle);
//                activity.startActivity(intent);
//            }
//        }
        return hasPsd;
    }

    @Override
    public boolean handleTouristControl() {
        if (isLogin()) {
            return false;
        } else {
            mRootView.showLoginPop();
            return true;
        }
    }

//    /**
//     * 余额检查处理
//     *
//     * @param amount
//     * @return
//     */
//    protected Observable<Object> handleWalletBlance(long amount) {
//        return mCommentRepository.getCurrentLoginUserInfo()
//                .flatMap(userInfoBean -> {
//                    if (amount > 0) {
//                        mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
//                        if (userInfoBean.getWallet() != null) {
//                            mWalletBeanGreenDao.insertOrReplace(userInfoBean.getWallet());
//                            if (userInfoBean.getWallet().getBalance() < amount) {
//                                mRootView.goTargetActivity(WalletActivity.class);
//                                return Observable.error(new RuntimeException(DEFAULT_WALLET_EXCEPTION_MESSAGE));
//                            }
//                        } else {
//                            mRootView.goTargetActivity(WalletActivity.class);
//                            return Observable.error(new RuntimeException(DEFAULT_WALLET_EXCEPTION_MESSAGE));
//                        }
//                    }
//                    return Observable.just(userInfoBean);
//                });
//    }

//    /**
//     * 积分检查处理
//     *
//     * @param amount
//     * @return
//     */
//    protected Observable<Object> handleIntegrationBlance(long amount) {
//        return mCommentRepository.getCurrentLoginUserInfo()
//                .flatMap(userInfoBean -> {
//                    mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
//                    if (amount > 0) {
//                        if (userInfoBean.getCurrency() != null) {
//                            if (userInfoBean.getCurrency().getSum() < amount) {
//                                if (getSystemConfigBean() != null && getSystemConfigBean().getCurrency().getRecharge() != null && getSystemConfigBean()
//                                        .getCurrency().getRecharge().isOpen()) {
//                                    mRootView.goTargetActivity(MineIntegrationActivity.class);
//                                } else {
//                                    return Observable.error(new RuntimeException(mContext.getString(R.string.integartion_not_enough)));
//                                }
//                                return Observable.error(new RuntimeException(DEFAULT_INTEGRATION_EXCEPTION_MESSAGE));
//                            }
//                        } else {
//                            if (getSystemConfigBean() != null && getSystemConfigBean().getCurrency().getRecharge() != null && getSystemConfigBean()
//                                    .getCurrency().getRecharge().isOpen()) {
//                                mRootView.goTargetActivity(MineIntegrationActivity.class);
//                            } else {
//                                return Observable.error(new RuntimeException(mContext.getString(R.string.integartion_not_enough)));
//                            }
//                            return Observable.error(new RuntimeException(DEFAULT_INTEGRATION_EXCEPTION_MESSAGE));
//                        }
//                    }
//                    return Observable.just(userInfoBean);
//                });
//    }

    /**
     * 检查异常是否是手动抛出的余额检查异常，如果是不做处理，如果不是需要处理
     *
     * @param throwable 抛出的异常
     * @return
     */
    protected boolean isBalanceCheck(Throwable throwable) {
        if (throwable != null && !TextUtils.isEmpty(throwable.getMessage()) && DEFAULT_WALLET_EXCEPTION_MESSAGE.equals(throwable.getMessage())) {
            mRootView.dismissSnackBar();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查异常是否是手动抛出的余额检查异常，如果是不做处理，如果不是需要处理
     *
     * @param throwable 抛出的异常
     * @return
     */
    protected boolean isIntegrationBalanceCheck(Throwable throwable) {
        if (throwable != null && !TextUtils.isEmpty(throwable.getMessage()) && DEFAULT_INTEGRATION_EXCEPTION_MESSAGE.equals(throwable.getMessage())) {
            mRootView.dismissSnackBar();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SystemConfigBean getSystemConfigBean() {
        return mSystemRepository.getAppConfigInfoFromLocal();
    }

    @Override
    public String getGoldName() {
        try {
            return getSystemConfigBean().getSite().getGold_name().getName();

        } catch (Exception e) {
            return mContext.getResources().getString(R.string.defualt_golde_name);
        }
    }

    @Override
    public String getGoldUnit() {
        try {
            return getSystemConfigBean().getSite().getGold_name().getName();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public int getRatio() {
        return getSystemConfigBean().getWallet().getRatio();
    }

    /**
     * 根据配置是否显示出服务器错误信息
     *
     * @param throwable 错误信息
     */
    protected void showErrorTip(Throwable throwable) {
        mRootView.showSnackErrorMessage(BuildConfig.USE_LOG ? throwable.getMessage() : mContext.getString(R.string.err_net_not_work));
    }

}
