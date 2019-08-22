package com.zhiyicx.baseproject.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.wcy.overscroll.OverScrollLayout;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.umeng.socialize.utils.DeviceConfig.context;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe H5 基类
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@SuppressLint("SetJavaScriptEnabled")
public abstract class TSWebFragment extends TSFragment {

    private static final int DEFALUT_SHOW_PROGRESS = 90;
    // 获取img标签正则
    private static final String IMAGE_URL_TAG = "<img.*src=(.*?)[^>]*?>";
    // 获取src路径的正则
    private static final String IMAGE_URL_CONTENT = "http:\"?(.*?)(\"|>|\\s+)";

    protected WebView mWebView;
    protected TextView mCloseView;
    private ProgressBar mProgressBar;
    private EmptyView mEmptyView;// 错误提示
    private OverScrollLayout mOverScrollLayout;

    private boolean mIsNeedProgress = true;// 是否需要进度条
    private List<String> mImageList = new ArrayList<>();// 网页内图片地址
    private String mLongClickUrl;// 长按图片的地址
    private boolean mIsLoadError;// 加载错误

    private Subscription subscription;
    private CompositeSubscription mCompositeSubscription;


    WebViewClient mWebViewClient = new WebViewClient() {
        /**
         * 多页面在同一个 WebView 中打开，就是不新建 activity 或者调用系统浏览器打开
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        /**
         * 网页开始加载
         * @param view
         * @param url
         * @param favicon
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mIsLoadError = false;
            mWebView.setVisibility(View.INVISIBLE);// 当加载网页的时候将网页进行隐藏
            mEmptyView.setErrorType(EmptyView.STATE_HIDE_LAYOUT);
        }

        /**
         *   网页加载结束
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mIsLoadError) {
                mEmptyView.setErrorType(EmptyView.STATE_NETWORK_ERROR);
            } else {
                mWebView.setVisibility(View.VISIBLE);
                // web 页面加载完成，添加监听图片的点击 js 函数
                setImageClickListner(view);
                //解析 HTML
                parseHTML(view);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            mIsLoadError = true;
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    };

    /**
     * js 通信接口处理图片单击，定义供 JavaScript 调用的交互接口
     */

    private class JavascriptInterfaceForImageClick {
        private Context context;

        public JavascriptInterfaceForImageClick(Context context) {
            this.context = context;
        }

        /**
         * 点击图片启动新的 ShowImageFromWebActivity，并传入点击图片对应的 url 和页面所有图片
         * 对应的 url
         *
         * @param url 点击图片对应的 url
         */
        @android.webkit.JavascriptInterface
        public void startShowImageActivity(String url) {
            onWebImageClick(url, mImageList);
        }
    }

    /**
     * 这个接口就是给 JavaScript 调用的,调用结果就是返回 HTML 文本，
     * 然后 getAllImageUrlFromHtml(HTML)
     * 从 HTML 文件中提取页面所有图片对应的地址对象
     **/
    private class JavaScriptForHandleHtml {
        /**
         * 获取 WebView 加载对应的 HTML 文本
         *
         * @param html WebView 加载对应的 HTML 文本
         */
        @android.webkit.JavascriptInterface
        public void showSource(String html) {
            // 从 HTML 文件中提取页面所有图片对应的地址对象
            getAllImageUrlFromHtml(html);
        }

    }

    WebChromeClient mWebChromeClient = new WebChromeClient() {

        /**
         * 多窗口的问题
         *
         * @param view
         * @param isDialog
         * @param isUserGesture
         * @param resultMsg
         * @return
         */
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(view);
            resultMsg.sendToTarget();
            return true;
        }

        /**
         * 进度条
         *
         * @param view
         * @param newProgress
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }

        private void setProgress(int newProgress) {
            if (!mIsNeedProgress) {
                return;
            }
            if (newProgress > DEFALUT_SHOW_PROGRESS && newProgress < getResources().getInteger(R.integer.progressbar_max)) {
                rxUnsub();
                if (View.GONE == mProgressBar.getVisibility()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                mProgressBar.setProgress(newProgress);
            } else if (newProgress == getResources().getInteger(R.integer.progressbar_max)) {
                rxUnsub();
                mProgressBar.setVisibility(View.GONE);
            }
        }

        /**
         * 当WebView加载之后，返回 HTML 页面的标题 Title
         *
         * @param view
         * @param title
         */
        @Override
        public void onReceivedTitle(WebView view, String title) {
            // 判断标题 title 中是否包含有“error”字段，如果包含“error”字段，则设置加载失败，显示加载失败的视图
            if (!TextUtils.isEmpty(title) && title.toLowerCase(Locale.getDefault()).contains("error")) {
                mIsLoadError = true;
            } else {
                mToolbarCenter.setVisibility(View.VISIBLE);
                mToolbarCenter.setText(title);
            }


        }
    };

    @Override
    protected int getToolBarLayoutId() {
        return R.layout.toolbar_for_web;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragme_ts_web;
    }

    @Override
    protected void initDefaultToolBar(View toolBarContainer) {
        super.initDefaultToolBar(toolBarContainer);
        mCloseView = (TextView) toolBarContainer.findViewById(R.id.tv_toolbar_left_right);
        mCloseView.setVisibility(View.INVISIBLE);
        mCloseView.setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_content));
        mCloseView.setText(getString(R.string.close));
        RxView.clicks(mCloseView)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        getActivity().finish();
                    }
                });
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void setLeftClick() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            mCloseView.setVisibility(View.VISIBLE);
        } else {
            super.setLeftClick();
        }
    }

    @Override
    protected void initView(View rootView) {
        mWebView = (WebView) rootView.findViewById(R.id.wv_about_us);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_bar);
        mEmptyView = (EmptyView) rootView.findViewById(R.id.emptyview);
        mEmptyView.setNeedTextTip(false);
        mEmptyView.setNeedClickLoadState(false);
        RxView.clicks(mEmptyView)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   // 两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mWebView.clearCache(true);
                        mWebView.reload();
                    }
                });
        mOverScrollLayout = (OverScrollLayout) rootView.findViewById(R.id.overscroll);
//        mOverScrollLayout.setTopOverScrollEnable(false);
    }


    @Override
    protected void initData() {
        initWebViewData();
    }

    private void initWebViewData() {
        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);

        mWebSettings.setDefaultTextEncodingName("utf-8");
        // 支持自动加载图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebSettings.setLoadsImagesAutomatically(true);
        } else {
            mWebSettings.setLoadsImagesAutomatically(false);
        }
        mWebSettings.setAllowFileAccess(true);

        //调用 JS 方法.安卓版本大于 17,加上注解 @JavascriptInterface
        mWebSettings.setJavaScriptEnabled(true);

        // 载入 js
        mWebView.addJavascriptInterface(new JavascriptInterfaceForImageClick(context), "imageListener");
        // 获取 html
        mWebView.addJavascriptInterface(new JavaScriptForHandleHtml(), "handleHtml");

        saveData(mWebSettings);
        newWin(mWebSettings);
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setWebImageLongClickListener(v);
                return false;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {  // chorme 调试
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    /***
     * 加载网页
     *
     * @param url 网页地址
     */
    public void loadUrl(String url, HashMap<String, String> headers) {
        mWebView.loadUrl(url, headers);
        if (!mIsNeedProgress) {
            return;
        }
        //参数一：延迟时间  参数二：间隔时间  参数三：时间颗粒度  模拟 {@link DEFALUT_SHOW_PROGRESS} 的进度
        mCompositeSubscription = new CompositeSubscription();
        subscription = Observable.interval(0, 20, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureLatest()
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long newProgress) {
                        mProgressBar.setProgress(newProgress.intValue());
                        if (newProgress == DEFALUT_SHOW_PROGRESS) {
                            rxUnsub();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
        mCompositeSubscription.add(subscription);


    }

    /**
     * 是否需要进度条
     *
     * @param needProgress
     */
    public void setNeedProgress(boolean needProgress) {
        mIsNeedProgress = needProgress;
    }

    /**
     * @return
     */
    public boolean isNeedProgress() {
        return mIsNeedProgress;
    }

    /**
     * 获取当前进度
     *
     * @return
     */
    public int getCurrentProgress() {
        return mProgressBar.getProgress();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebView.resumeTimers();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers(); //小心这个！！！暂停整个 WebView 所有布局、解析、JS。
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.loadUrl("about:blank");
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
        rxUnsub();
    }

    private void rxUnsub() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    /**
     * 覆盖系统的回退键
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            mCloseView.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }


    /**
     * 网页图片单击
     *
     * @param clickUrl 单击的当前图片地址
     * @param images   页面中所有的图片地址
     */
    protected abstract void onWebImageClick(String clickUrl, List<String> images);

    /**
     * 网页图片长按
     *
     * @param longClickUrl 长按当前图片地址
     */
    protected abstract void onWebImageLongClick(String longClickUrl);

    /**
     * 设置网页错误提示图片
     *
     * @param resId
     */
    public void setTipImage(@DrawableRes int resId) {
        mEmptyView.setErrorImag(resId);
    }

    /***
     * 获取页面所有图片对应的地址对象，
     * 例如 <mIvError src="http://sc1.hao123img.com/data/f44d0aab7bc35b8767de3c48706d429e" />
     *
     * @param html WebView 加载的 html 文本
     * @return
     */
    public List<String> getAllImageUrlFromHtml(String html) {
        Matcher matcher = Pattern.compile(IMAGE_URL_TAG).matcher(html);
        List<String> listImgUrl = new ArrayList<>();
        while (matcher.find()) {
            listImgUrl.add(matcher.group());
        }
        //从图片对应的地址对象中解析出 src 标签对应的内容
        return getAllImageUrlFormSrcObject(listImgUrl);
    }

    /***
     * 从图片对应的地址对象中解析出 src 标签对应的内容,即 url
     * 例如 "http://sc1.hao123img.com/data/f44d0aab7bc35b8767de3c48706d429e"
     *
     * @param listImageUrl 图片地址对象，
     *                     例如 <mIvError src="http://sc1.hao123img.com/data/f44d0aab7bc35b8767de3c48706d429e" />
     */
    private List<String> getAllImageUrlFormSrcObject(List<String> listImageUrl) {
        for (String image : listImageUrl) {
            Matcher matcher = Pattern.compile(IMAGE_URL_CONTENT).matcher(image);
            while (matcher.find()) {
                mImageList.add(matcher.group().substring(0, matcher.group().length() - 1));
            }
        }
        return mImageList;
    }

    /**
     * 解析 HTML 该方法在 setWebViewClient 的 onPageFinished 方法中进行调用
     *
     * @param view
     */
    private void parseHTML(WebView view) {
        view.loadUrl("javascript:window.handleHtml.showSource('<head>'+"
                + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
    }

    /**
     * 注入 js 函数监听，这段 js 函数的功能就是，遍历所有的图片，并添加 onclick 函数，实现点击事件，
     * 函数的功能是在图片点击的时候调用本地 java 接口并传递 url 过去
     */
    private void setImageClickListner(WebView view) {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        view.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"mIvError\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imageListener.startShowImageActivity(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    /**
     * 多窗口的问题
     */
    private void newWin(WebSettings mWebSettings) {
        //html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下
        //然后 复写 WebChromeClient的onCreateWindow方法
        mWebSettings.setSupportMultipleWindows(false);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    /**
     * HTML5 数据存储
     */
    private void saveData(WebSettings mWebSettings) {
        //有时候网页需要自己保存一些关键数据,Android WebView 需要自己设置
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        if (NetUtils.netIsConnected(getContext())) {
            mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//网络不可用时只使用缓存
        } else {
            mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//网络不可用时只使用缓存
        }
        String appCachePath = FileUtils.getCacheFilePath(getContext());
        mWebSettings.setAppCachePath(appCachePath);
    }

    /**
     * 响应长按点击事件
     *
     * @param v
     */
    private void setWebImageLongClickListener(View v) {
        if (v instanceof WebView) {
            WebView.HitTestResult result = ((WebView) v).getHitTestResult();
            if (result != null) {
                int type = result.getType();
                if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    mLongClickUrl = result.getExtra();
                    onWebImageLongClick(mLongClickUrl);
                }
            }
        }
    }
}
