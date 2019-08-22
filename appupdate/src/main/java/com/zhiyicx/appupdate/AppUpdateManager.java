package com.zhiyicx.appupdate;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.appupdate.callback.OnCancelListener;
import com.zhiyicx.appupdate.v2.AllenVersionChecker;
import com.zhiyicx.appupdate.v2.builder.DownloadBuilder;
import com.zhiyicx.appupdate.v2.builder.UIData;
import com.zhiyicx.appupdate.v2.callback.CustomVersionDialogListener;
import com.zhiyicx.appupdate.v2.callback.ForceUpdateListener;
import com.zhiyicx.appupdate.v2.callback.RequestVersionListener;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.InternalStyleSheet;
import br.tiagohm.markdownview.css.styles.Github;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/9/13
 * @Contact master.jungle68@gmail.com
 */
public class AppUpdateManager {
    private volatile static AppUpdateManager ourInstance;
    private final DownloadBuilder builder;
    private Context mContext;

    private static final String BUNDLE_VERSIONDATA = "versionData";
    public static final String SHAREPREFERENCE_TAG_ABORD_VERION = "abord_vertion";

    public static AppUpdateManager getInstance(Context context, String requstUrl) {
        if (ourInstance == null) {
            synchronized (AppUpdateManager.class) {
                if (ourInstance == null) {
                    ourInstance = new AppUpdateManager(context.getApplicationContext(), requstUrl);
                }
            }
        }
        return ourInstance;
    }

    private AppUpdateManager(Context context, String requstUrl) {
        mContext = context;
        builder = AllenVersionChecker
                .getInstance()
                .requestVersion()
                .setRequestUrl(requstUrl)
                .request(new RequestVersionListener() {
                    @Nullable
                    @Override
                    public UIData onRequestVersionSuccess(String result) {
                        return crateUIData(result);
                    }

                    @Override
                    public void onRequestVersionFailure(String message) {

                    }
                });
        builder.setForceRedownload(true);
        builder.setCustomVersionDialogListener(createCustomDialog());
        builder.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel() {
                if (builder.getForceUpdateListener() != null) {
                    ActivityHandler.getInstance().finishAllActivity();
                }
            }
        });
    }

    private UIData crateUIData(String response) {
        List<AppVersionBean> appVersionBean = null;
        try {
            appVersionBean = new Gson().fromJson(response, new TypeToken<List<AppVersionBean>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (appVersionBean != null
                && !appVersionBean.isEmpty()
                && !checkIsAbord(mContext, appVersionBean.get(0).getVersion_code())
                && AppUtils.getVersionCode(mContext) < appVersionBean.get(0).getVersion_code()) {
            boolean forceUpdate = appVersionBean.get(0).getIs_forced() > 0;
            UIData uiData = UIData.create();
            uiData.getVersionBundle().putParcelable(BUNDLE_VERSIONDATA, appVersionBean.get(0));
            builder.setForceUpdateListener(forceUpdate ? new ForceUpdateListener() {
                @Override
                public void onShouldForceUpdate() {
                    ActivityHandler.getInstance().finishAllActivity();
                }
            } : null);
            uiData.setTitle("检测到新版本");
            uiData.setDownloadUrl(appVersionBean.get(0).getLink());
            uiData.setContent(appVersionBean.get(0).getDescription());
            return uiData;
        }
        return null;
    }

    private CustomVersionDialogListener createCustomDialog() {
        return new CustomVersionDialogListener() {
            @Override
            public Dialog getCustomVersionDialog(final Context context, final UIData uiData) {
                final BaseDialog versionDialog = new BaseDialog(context, R.style.BaseDialog, R.layout.custom_dialog_two_layout);
                final TextView tvTitle = versionDialog.findViewById(R.id.tv_title);
                final ImageView tvAbord = versionDialog.findViewById(R.id.iv_ignore);
                boolean isForceUpdate = builder.getForceUpdateListener() != null;
                versionDialog.setCanceledOnTouchOutside(!isForceUpdate);
                versionDialog.setCancelable(!isForceUpdate);
                if (!isForceUpdate) {
                    tvAbord.setVisibility(View.VISIBLE);
                    tvAbord.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            versionDialog.dismiss();
                            AppVersionBean versionBean = uiData.getVersionBundle().getParcelable(BUNDLE_VERSIONDATA);
                            if (versionBean != null) {
                                ArrayList<Integer> abordVersions = SharePreferenceUtils.getObject(context, SHAREPREFERENCE_TAG_ABORD_VERION);
                                if (abordVersions == null) {
                                    abordVersions = new ArrayList<>();
                                }
                                abordVersions.add(versionBean.getVersion_code());
                                SharePreferenceUtils.saveObject(context, SHAREPREFERENCE_TAG_ABORD_VERION, abordVersions);
                            }
                            Activity activity = AppUtils.scanForActivity(view.getContext());
                            if (activity != null) {
                                activity.finish();
                            }
                        }
                    });
                } else {
                    versionDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                }
                final MarkdownView mdMsg = versionDialog.findViewById(R.id.md_msg);
                //可以使用之前从service传过来的一些参数比如：title。msg，downloadurl，parambundle
                tvTitle.setText(uiData.getTitle());
                InternalStyleSheet css = new Github();
                css.addRule(".container", "padding-right:0", ";padding-left:0", "margin-right: 20px","text-align:justify", "text-align-last:left", "letter-spacing: 0.3px");
                css.addRule("body", "line-height: 1.59", "padding: 0px", "font-size: 13px", "color: #999999");
                css.addRule("h1", "color: #333333", "margin-left: 20px","size: 25px", "text-align: left");
                css.addRule("h2", "color: #333333", "margin-left: 20px","size: 23px", "text-align: left");
                css.addRule("h3", "color: #333333", "margin-left: 20px","size: 21px", "text-align: left");
                css.addRule("h4", "color: #333333", "margin-left: 20px","size: 19px", "text-align: left");
                css.addRule("img", "margin-top: 20px", "margin-bottom: 20px", "align:center", "margin: 0 auto", "max-width: 100%", "display: block");
                /*设置 a 标签文字颜色，不知道为什么，要这样混合才能有效*/
                css.addMedia("color: #59b6d7; a:link {color: #59b6d7}");
                css.endMedia();
                css.addRule("a", "font-weight: bold");
                mdMsg.addStyleSheet(css);
                mdMsg.loadMarkdown(uiData.getContent());
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

                    }

                    /**
                     *   网页加载结束
                     */
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        mdMsg.scrollTo(0, 0);
                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        System.out.println("errorCode = " + errorCode);
                        super.onReceivedError(view, errorCode, description, failingUrl);
                    }
                };
                mdMsg.setWebViewClient(mWebViewClient);
                return versionDialog;
            }
        };
    }

    /**
     * 检查版本是否被忽略
     *
     * @return
     */
    public boolean checkIsAbord(Context context, int verstionCode) {
        ArrayList<Integer> abordVersions = SharePreferenceUtils.getObject(context, SHAREPREFERENCE_TAG_ABORD_VERION);
        return abordVersions != null && abordVersions.contains(verstionCode);
    }

    public void startVersionCheck() {
        builder.excuteMission(mContext);
    }
}
