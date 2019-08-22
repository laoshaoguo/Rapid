package com.zhiyicx.baseproject.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.widget.badgeview.DisplayUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author LiuChao
 * @describe 和viewpager关联的tab选项卡，集成在toolbar中；
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public class TabSelectView extends FrameLayout {
    // 定义默认样式值
    private static final int DEFAULT_TAB_UNSELECTED_TEXTCOLOR = R.color.normal_for_assist_text;// 缺省的tab未选择文字
    private static final int DEFAULT_TAB_SELECTED_TEXTCOLOR = R.color.important_for_content;// 缺省的tab被选择文字
    private static final int DEFAULT_TAB_TEXTSIZE = R.integer.tab_text_size;// 缺省的tab文字大小
    private static final int DEFAULT_TAB_TEXTSIZE_BIG = R.integer.tab_text_size_big;// 缺省的tab文字大小
    private static final int DEFAULT_TAB_LINE_COLOR = R.color.themeColor;// 缺省的tab的线的颜色
    private int mTabMargin = R.integer.tab_margin;// 缺省的tab左padding
    private int mTabMargin1 = R.integer.tab_margin;// 缺省的tab右padding

    private int mTabPadding = R.integer.tab_padding;// 缺省的tab的线和文字的边缘距离
    private int mLineHeight = R.integer.line_height;// 缺省的tab的线的高度
    private int mLineWidth = R.integer.line_width;// 缺省的tab的线的宽度
    private int mXOffset;// 缺省的tab的线的宽度
    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private View divider;
    private TextView tvToolbarLeft, tvToolbarRight,tvToolbarLeftRight;
    private List<String> mStringList;// tab列表的文字
    private Context mContext;
    private CommonNavigator mCommonNavigator;
    private boolean mIsAdjustMode;
    private int mLinePagerIndicator = LinePagerIndicator.MODE_WRAP_CONTENT;
    private int mTabSpacing;

    private boolean mIsNeedChooseItemToBig = false;

    public TabSelectView(Context context) {
        super(context);
        initView(context);
    }

    public TabSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TabSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.toolbar_for_viewpager, this);
        mMagicIndicator = (MagicIndicator) findViewById(R.id.mg_indicator);
        divider = findViewById(R.id.divider);
        tvToolbarLeft = (TextView) findViewById(R.id.tv_toolbar_left);
        tvToolbarRight = (TextView) findViewById(R.id.tv_toolbar_right);
        tvToolbarLeftRight = (TextView) findViewById(R.id.tv_toolbar_right_left);
        mContext = context;
        mTabSpacing = getResources().getDimensionPixelOffset(R.dimen.spacing_large);
        showDivider(true);// 默认展示分割线
        setLeftImg(R.mipmap.topbar_back);// 默认左边为箭头
    }

    public void initTabView(ViewPager viewPager, List<String> stringList) {
        this.mViewPager = viewPager;
        this.mStringList = stringList;
        if (mStringList == null) {
            mStringList = new ArrayList<>();
        }
        initMagicIndicator();
    }

    public void initTabView(ViewPager viewPager, List<String> stringList, CommonNavigatorAdapter customAdapter) {
        this.mViewPager = viewPager;
        this.mStringList = stringList;
        if (mStringList == null) {
            mStringList = new ArrayList<>();
        }
        initMagicIndicator(customAdapter);
    }

    public void initTabView(ViewPager viewPager, List<String> stringList, int resId, CommonNavigatorAdapter customAdapter) {
        this.mViewPager = viewPager;
        this.mStringList = stringList;
        if (mStringList == null) {
            mStringList = new ArrayList<>();
        }
        initMagicIndicator(resId, customAdapter);
    }

    /**
     * 是否需要展示toolbar分割线
     */
    public void showDivider(boolean showDivider) {
        divider.setVisibility(showDivider ? VISIBLE : GONE);
    }

    public void showDivider(boolean showDivider, boolean useTabSpacing) {
        divider.setVisibility(showDivider ? VISIBLE : GONE);
        if (useTabSpacing) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) divider.getLayoutParams();
            params.leftMargin = mTabSpacing;
            params.rightMargin = mTabSpacing;
        }
    }

    public void setLeftText(String text) {
        tvToolbarLeft.setText(text);
        tvToolbarLeft.setVisibility(View.VISIBLE);
    }

    public void setLeftImg(int imgRes) {
        if (imgRes == 0 && TextUtils.isEmpty(tvToolbarLeft.getText())) {
            tvToolbarLeft.setVisibility(INVISIBLE);
        } else {
            tvToolbarLeft.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), imgRes), null, null, null);
            tvToolbarLeft.setVisibility(View.VISIBLE);
        }
    }

    public void setRightText(String text) {
        tvToolbarRight.setText(text);
    }

    public void setLeftRightText(String text) {
        tvToolbarLeftRight.setText(text);
    }

    public void setRightImg(int imgRes, @ColorRes int backgroud) {
        if (imgRes == 0 && TextUtils.isEmpty(tvToolbarRight.getText())) {
            tvToolbarRight.setVisibility(INVISIBLE);
        } else {
            tvToolbarRight.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), imgRes), null, null, null);
            tvToolbarRight.setVisibility(View.VISIBLE);
            tvToolbarRight.setBackgroundResource(backgroud);
        }
    }

    public void setLeftRightImg(int imgRes, @ColorRes int backgroud) {
        if (imgRes == 0 && TextUtils.isEmpty(tvToolbarLeftRight.getText())) {
            tvToolbarLeftRight.setVisibility(INVISIBLE);
        } else {
            tvToolbarLeftRight.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), imgRes), null, null, null);
            tvToolbarLeftRight.setVisibility(View.VISIBLE);
            tvToolbarLeftRight.setBackgroundResource(backgroud);
        }
    }

    /**
     * 获取右边 view
     *
     * @return
     */
    public TextView getRightTextView() {
        return tvToolbarRight;
    }

    /**
     * 设置文字颜色
     *
     * @param leftColor 文字的颜色值，不是颜色resID
     */
    public void setTextColor(int leftColor, int rightColor) {
        tvToolbarLeft.setTextColor(leftColor);
        tvToolbarLeft.setTextColor(rightColor);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(int leftSize, int rightSize) {
        tvToolbarLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP, leftSize);
        tvToolbarRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, rightSize);
    }

    public void setLeftClickListener(final TSFragment fragment, final TabLeftRightClickListener tabLeftClickListener) {
        RxView.clicks(tvToolbarLeft)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(fragment.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (tabLeftClickListener != null) {
                            tabLeftClickListener.buttonClick();
                        }
                    }
                });
    }

    public void setRightClickListener(TSFragment fragment, final TabLeftRightClickListener tabRightClickListener) {
        RxView.clicks(tvToolbarRight)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(fragment.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (tabRightClickListener != null) {
                            tabRightClickListener.buttonClick();
                        }
                    }
                });
    }

    public void setLeftRightClickListener(TSFragment fragment, final TabLeftRightClickListener tabRightClickListener) {
        RxView.clicks(tvToolbarLeftRight)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(fragment.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (tabRightClickListener != null) {
                            tabRightClickListener.buttonClick();
                        }
                    }
                });
    }

    public void setNeedChooseItemToBig(boolean needChooseItemToBig) {
        mIsNeedChooseItemToBig = needChooseItemToBig;
    }

    private void initMagicIndicator() {
        mMagicIndicator.setBackgroundColor(Color.TRANSPARENT);
        mCommonNavigator = new CommonNavigator(mContext);
        mCommonNavigator.setAdjustMode(mIsAdjustMode);
        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {


            @Override
            public int getCount() {
                return mStringList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(context, DEFAULT_TAB_UNSELECTED_TEXTCOLOR));
                simplePagerTitleView.setPadding(mTabSpacing, 0, mTabSpacing, 0);
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, DEFAULT_TAB_SELECTED_TEXTCOLOR));
                simplePagerTitleView.setText(mStringList.get(index));
                if (mIsNeedChooseItemToBig && mViewPager.getCurrentItem() == index) {
                    simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getInteger(DEFAULT_TAB_TEXTSIZE_BIG));
                } else {
                    simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getInteger(DEFAULT_TAB_TEXTSIZE));
                }
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(mLinePagerIndicator);
                try {
                    linePagerIndicator.setXOffset(mXOffset == 0 ? -UIUtil.dip2px(context, context.getResources().getInteger(mTabPadding)) : mXOffset);
                    linePagerIndicator.setLineHeight(UIUtil.dip2px(context, context.getResources().getInteger(mLineHeight)));

                    if (LinePagerIndicator.MODE_EXACTLY == mLinePagerIndicator) {
                        linePagerIndicator.setLineWidth(UIUtil.dip2px(context, context.getResources().getInteger(mLineWidth)));
                    }
                } catch (Exception ignored) {
                }
                linePagerIndicator.setColors(ContextCompat.getColor(context, DEFAULT_TAB_LINE_COLOR));
                return linePagerIndicator;
            }
        });
        mMagicIndicator.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
        if (mIsNeedChooseItemToBig) {
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mCommonNavigator.notifyDataSetChanged();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private void initMagicIndicator(CommonNavigatorAdapter customAdapter) {
        mMagicIndicator.setBackgroundColor(Color.TRANSPARENT);
        mCommonNavigator = new CommonNavigator(mContext);
        mCommonNavigator.setAdapter(customAdapter);
        mCommonNavigator.setAdjustMode(mIsAdjustMode);
        mMagicIndicator.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    /**
     * 可以设置背景的
     */
    private void initMagicIndicator(int resId, CommonNavigatorAdapter customAdapter) {
        mMagicIndicator.setBackgroundResource(resId);
        mCommonNavigator = new CommonNavigator(mContext);
        mCommonNavigator.setAdapter(customAdapter);
        mCommonNavigator.setAdjustMode(mIsAdjustMode);
        mMagicIndicator.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    public void setAdjustMode(boolean adjustMode) {
        mIsAdjustMode = adjustMode;
    }

    public void setIndicatorMode(int indicatorMode) {
        mLinePagerIndicator = indicatorMode;
    }

    public void setIndicatorMatchWidth(boolean matchWidth) {
        if (matchWidth) {
            ViewGroup.LayoutParams params = mMagicIndicator.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mMagicIndicator.setPadding(0, 0, DisplayUtil.dp2px(mContext, 38), 0);
            mMagicIndicator.setLayoutParams(params);
        }
    }

    public void setDefaultTabLinehegiht(int height) {
        mLineHeight = height;
    }

    public void setTabSpacing(int spacing) {
        mTabSpacing = spacing;
    }

    public void setXOffset(int XOffset) {
        mXOffset = XOffset;
    }

    public void setDefaultTabLeftMargin(int defaultTabLeftMargin) {
        mTabMargin = defaultTabLeftMargin;
    }

    public void setDefaultTabRightMargin(int defaultTabRightMargin) {
        mTabMargin1 = defaultTabRightMargin;
    }


    public interface TabLeftRightClickListener {
        void buttonClick();
    }

    public void notifyDataSetChanged(List<String> stringList) {
        mStringList = stringList;
        mCommonNavigator.notifyDataSetChanged();
    }
}
