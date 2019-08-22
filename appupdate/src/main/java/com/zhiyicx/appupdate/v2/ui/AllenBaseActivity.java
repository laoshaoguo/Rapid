package com.zhiyicx.appupdate.v2.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.zhiyicx.appupdate.v2.builder.DownloadBuilder;
import com.zhiyicx.appupdate.v2.eventbus.CommonEvent;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


/**
 * Created by allenliu on 2018/1/18.
 */

public abstract class AllenBaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        setTransparent(this);
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置根布局参数
     */
    private void setRootView(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }

    public void setTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(activity);
        setRootView(activity);
    }
    protected void throwWrongIdsException() {
        throw new RuntimeException("customize dialog must use the specify id that lib gives");
    }
    protected DownloadBuilder getVersionBuilder(){
        return VersionService.builder;
    }
    protected void checkForceUpdate() {
        if (getVersionBuilder()!=null&&getVersionBuilder().getForceUpdateListener() != null) {
            getVersionBuilder().getForceUpdateListener().onShouldForceUpdate();
            finish();
        }
    }
    protected  void cancelHandler(){
        if (getVersionBuilder()!=null&&getVersionBuilder().getOnCancelListener() != null) {
            getVersionBuilder().getOnCancelListener().onCancel();

        }
    }
    @Subscriber(mode = ThreadMode.MAIN)
    public void receiveEvent(CommonEvent commonEvent) {
    }

    public abstract void showDefaultDialog();
    public abstract void showCustomDialog();
}
