package com.zhiyicx.common.mvp;

import android.app.Application;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/14
 * @Contact 335891510@qq.com
 */
public abstract class BasePresenter<V extends IBaseView> implements IBasePresenter {
    protected final String TAG = this.getClass().getSimpleName();
    protected final Observable.Transformer mSchedulersTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    private CompositeSubscription mCompositeSubscription;
    protected V mRootView;

    @Inject
    protected Application mContext;

    public BasePresenter(V rootView) {
        this.mRootView = rootView;
        if (useEventBus())// 如果要使用 eventbus 请将此方法返回 true
        {
            EventBus.getDefault().register(this);// 注册到事件主线
        }
    }

    /**
     * Method injection is used here to safely reference {@code this} after the object is created.
     * For more information, see Java Concurrency in Practice.
     */
    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onDestroy() {
        unSubscribe();
        if (useEventBus())// 如果要使用 eventbus 请将此方法返回 true
        {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public boolean userHasPassword() {
        return false;
    }

    /**
     * 是否使用 eventBus,默认为使用(true)，
     *
     * @return
     */
    protected boolean useEventBus() {
        return false;
    }

    protected void addSubscrebe(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);// 将所有 subscription 放入,集中处理
    }

    protected void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();// 保证 activity 结束时取消所有正在执行的订阅
        }
    }


}
