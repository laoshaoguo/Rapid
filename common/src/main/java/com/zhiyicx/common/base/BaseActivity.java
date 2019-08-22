package com.zhiyicx.common.base;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.zhiyicx.common.base.i.IBaseActivity;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.ActivityHandler;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import skin.support.app.SkinCompatActivity;

/**
 * @Describe Activity 基类
 * @Author Jungle68
 * @Date 2016/12/14
 * @Contact 335891510@qq.com
 */

public abstract class BaseActivity<P extends BasePresenter> extends SkinCompatActivity implements
        IBaseActivity {
    protected final String TAG = this.getClass().getSimpleName();

    protected BaseApplication mApplication;
    @Inject
    protected P mPresenter;
    private Unbinder mUnbinder;
    protected LayoutInflater mLayoutInflater;
    /**
     * 用于应用是否处于前台还是后台的判断；
     */
    public boolean mIsForeground;
    protected FragmentDispatchTouchEventListener mFragmentDispatchTouchEventListener;

    @SuppressLint("ActivityLayoutNameNotPrefixed")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            restoreData(savedInstanceState);
        }
        mApplication = (BaseApplication) getApplication();
        ActivityHandler.getInstance().addActivity(this);
        mLayoutInflater = LayoutInflater.from(this);
        // 如果要使用 eventbus 请将此方法返回 true
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        setContentView(getLayoutId());
        // 绑定到 butterknife
        mUnbinder = ButterKnife.bind(this);
        initView(savedInstanceState);
        componentInject();// 依赖注入，必须放在initview后
        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsForeground = false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mFragmentDispatchTouchEventListener != null && mFragmentDispatchTouchEventListener.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    /**
     * 子类获取contentView
     *
     * @return
     */
    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityHandler.getInstance().removeActivityNotFinish(this);
        if (mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        if (useEventBus())// 如果要使用 eventbus 请将此方法返回 true
        {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 是否使用 eventBus,默认为使用(true)，
     *
     * @return
     */
    protected boolean useEventBus() {
        return false;
    }

    /**
     * 依赖注入的入口
     */
    protected abstract void componentInject();

    /**
     * view 初始化
     *
     * @param savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 数据初始化
     */
    protected abstract void initData();

    /**
     * 读取关闭时保存的数据
     *
     * @param savedInstanceState
     */
    protected void restoreData(Bundle savedInstanceState) {

    }

    /**
     * 关闭时保存数据
     *
     * @param savedInstanceState
     * @return
     */
    protected Bundle saveData(Bundle savedInstanceState) {
        return savedInstanceState;
    }


    public interface FragmentDispatchTouchEventListener {
        boolean dispatchTouchEvent(MotionEvent ev);
    }

    public void registerFragmentDispatchTouchEventListener(FragmentDispatchTouchEventListener listener) {
        mFragmentDispatchTouchEventListener = listener;
    }

    protected void applyWindowFullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void applyViewFullScreen(boolean hasFocus) {
        if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
