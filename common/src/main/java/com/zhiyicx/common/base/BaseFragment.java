package com.zhiyicx.common.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.components.support.RxFragment;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.log.LogUtils;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Describe Fragment 基类
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */
public abstract class BaseFragment<P extends IBasePresenter> extends RxFragment implements IBaseView<P>{
    protected final String TAG = this.getClass().getSimpleName();

    protected View mRootView;
    protected Activity mActivity;
    protected P mPresenter;
    private Unbinder mUnbinder;
    protected LayoutInflater mLayoutInflater;
    public RxPermissions mRxPermissions;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mRootView = getContentView();
        // 绑定到 butterknife
        mUnbinder = ButterKnife.bind(this, mRootView);
        //是否需要权限验证，需要防止 initview 之前，防止 rxbinding初始化空
        if (usePermisson()) {
            mRxPermissions = new RxPermissions(getActivity());
            mRxPermissions.setLogging(true);
        }
        initView(mRootView);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 如果要使用 eventbus 请将此方法返回 true
        if (useEventBus()){
            // 注册到事件主线
            EventBus.getDefault().register(this);
        }
        initData();
    }

    /**
     * 是否需要使用权限验证
     *
     * @return
     */
    protected boolean usePermisson() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void onPause() {
        DeviceUtils.hideSoftKeyboard(getActivity(), getActivity().getWindow().getDecorView());
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        // 如果要使用 eventbus 请将此方法返回 true
        if (useEventBus())
        {
            EventBus.getDefault().unregister(this);
        }
    }

    protected abstract View getContentView();

    protected abstract void initView(View rootView);

    protected abstract void initData();

    /**
     * 是否使用 eventBus,默认为使用(true)，
     *
     * @return
     */
    protected boolean useEventBus() {
        return false;
    }

    /**
     * 此方法是让外部调用使 fragment 做一些操作的,比如说外部的 activity 想让 fragment 对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传 bundle,里面存一个 what 字段,来区分不同的方法,在 setData
     * 方法中就可以 switch 做不同的操作,这样就可以用统一的入口方法做不同的事,和 message 同理
     * <p>
     * 使用此方法时请注意调用时 fragment 的生命周期,如果调用此 setData 方法时 onActivityCreated
     * 还没执行,setData 里调用 presenter 的方法时,是会报空的,因为 dagger 注入是在 onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调 setData,在内部 onActivityCreated 中
     * 初始化就可以了
     *
     * @param data
     */
    public void setData(Object data) {

    }

    /**
     * 使用此方法时请注意调用时 fragment 的生命周期,如果调用此 setData 方法时 onActivityCreated
     * 还没执行,setData 里调用 presenter 的方法时,是会报空的,因为 dagger 注入是在 onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调 setData,在内部 onActivityCreated 中
     * 初始化就可以了
     */
    public void setData() {

    }
    /**
     * 添加返回按键的监听方法，在它所依附的activity中调用
     *
     * @param keyCode
     * @param event
     * @return false 表示down事件未处理，会继续传递，交给up处理，知道结束或true停止
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 系统返回键按下
     */
    public void onBackPressed() {

    }

}
