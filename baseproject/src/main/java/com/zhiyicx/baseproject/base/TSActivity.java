package com.zhiyicx.baseproject.base;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

import com.umeng.analytics.MobclickAgent;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.base.BaseActivity;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.ActivityUtils;
import com.zhiyicx.common.utils.ParcelableDataUtil;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * @Describe activity只是作为fragment的容器，具体的功能逻辑在fragment中完成
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public abstract class TSActivity<P extends BasePresenter, F extends Fragment> extends BaseActivity<P> {

    /**
     * 内容 fragment
     */
    protected F mContanierFragment;

    /**
     * token 过期弹框，重新登录等
     */
    private AlertDialog mAlertdialog;
    private Subscription mSubscribe;


    public F getContanierFragment() {
        return mContanierFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ts;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        // 添加fragment
        if (mContanierFragment == null) {
            mContanierFragment = getFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mContanierFragment, R.id.fl_fragment_container);
        }
    }

    @Override
    protected void initData() {

    }

    ///解决重叠，方法去除状态保存
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //如果用以下这种做法则不保存状态，再次进来的话会显示默认tab
        //super.onSaveInstanceState(outState);
    }

    /**
     * @return 当前页的Fragment
     */
    protected abstract F getFragment();

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ParcelableDataUtil.getSingleInstance().clear();
        if (mAlertdialog != null && mAlertdialog.isShowing()) {
            mAlertdialog.dismiss();
        }
        if (mSubscribe != null && !mSubscribe.isUnsubscribed()) {
            mSubscribe.unsubscribe();
        }
    }

    /**
     * @param tipStr
     * @param listener
     */
    public void showWarnningDialog(final String tipStr, final DialogInterface.OnClickListener listener) {
        if (listener == null) {
            return;
        }
        // 跳到登陆页面，销毁之前的所有页面,添加弹框处理提示
        // 通过rxjava在主线程处理弹框
        mSubscribe = Observable.empty()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        if ((mAlertdialog != null && mAlertdialog.isShowing())) {
                            return;
                        }
                        mAlertdialog = new AlertDialog.Builder(TSActivity.this, R.style.TSWarningAlertDialogStyle)
                                .setMessage(tipStr)
                                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                        return mAlertdialog.isShowing() && keyCode == KeyEvent.KEYCODE_BACK
                                                && event.getRepeatCount() == 0;
                                    }
                                })
                                .setPositiveButton(R.string.determine, listener)
                                .create();
                        mAlertdialog.setCanceledOnTouchOutside(false);
                        try {
                            mAlertdialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                })
                .subscribe();
    }
}
