package com.zhiyicx.baseproject.base;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.widget.TabSelectView;
import com.zhiyicx.common.mvp.i.IBasePresenter;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;


/**
 * @Describe viewpager 类的抽象基类
 * @Author Jungle68
 * @Date 2017/2/22
 * @Contact master.jungle68@gmail.com
 */
public abstract class TSViewPagerFragment<P extends IBasePresenter> extends TSFragment<P> {
    private static final int DEFAULT_OFFSET_PAGE = 3;
    protected TabSelectView mTsvToolbar;
    protected ViewPager mVpFragment;
    protected TSViewPagerAdapter tsViewPagerAdapter;
    protected List<Fragment> mFragmentList;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_follow_fans_viewpager;
    }

    @Override
    protected void initView(View rootView) {
        initViewPager(rootView);
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    protected abstract List<String> initTitles();

    protected abstract List<Fragment> initFragments();

    protected void initViewPager(View rootView) {
        mTsvToolbar = rootView.findViewById(R.id.tsv_toolbar);
        mVpFragment = rootView.findViewById(R.id.vp_fragment);
        tsViewPagerAdapter = new TSViewPagerAdapter(getChildFragmentManager());
        tsViewPagerAdapter.bindData(initFragments());
        mVpFragment.setAdapter(tsViewPagerAdapter);
        mTsvToolbar.setAdjustMode(isAdjustMode());
        mTsvToolbar.setTabSpacing(tabSpacing());
        mTsvToolbar.setIndicatorMode(setIndicatorMode());
        mTsvToolbar.initTabView(mVpFragment, initTitles());
        mTsvToolbar.setLeftClickListener(this, new TabSelectView.TabLeftRightClickListener() {
            @Override
            public void buttonClick() {
                setLeftClick();
            }
        });
        mVpFragment.setOffscreenPageLimit(getOffsetPage());
    }

    protected int tabSpacing() {
        return getResources().getDimensionPixelOffset(R.dimen.spacing_large);
    }

    protected boolean isAdjustMode() {
        return false;
    }
    protected int setIndicatorMode() {
        return LinePagerIndicator.MODE_WRAP_CONTENT;
    }

    protected int getOffsetPage() {
        return DEFAULT_OFFSET_PAGE;
    }

}
