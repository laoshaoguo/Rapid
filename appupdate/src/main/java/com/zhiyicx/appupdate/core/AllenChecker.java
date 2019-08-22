package com.zhiyicx.appupdate.core;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.zhiyicx.appupdate.core.http.AllenHttp;

/**
 * Created by allenliu on 2017/8/15.
 *{@link com.zhiyicx.appupdate.v2.AllenVersionChecker} use AllenVersionChecker of v2
 */
@Deprecated
public class AllenChecker {
    private static boolean isDebug = true;
    private static Context globalContext;
    private static VersionParams params;

    public static void startVersionCheck(Application context, VersionParams versionParams) {
        globalContext = context;
        params = versionParams;
        Intent intent = new Intent(context, versionParams.getService());
        intent.putExtra(AVersionService.VERSION_PARAMS_KEY, versionParams);
        context.stopService(intent);
        context.startService(intent);
    }

    public static void init(boolean debug) {
        isDebug = debug;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    /**
     * cancel all the https request
     */
    public static void cancelMission() {
        AllenHttp.getHttpClient().dispatcher().cancelAll();
        if (globalContext != null && params != null) {
            Intent intent = new Intent(globalContext, params.getService());
            globalContext.stopService(intent);
        }
        if (VersionDialogActivity.instance != null) {
            VersionDialogActivity.instance.finish();
        }
        globalContext = null;
        params = null;

    }

    public static Context getGlobalContext() {
        return globalContext;
    }

}
