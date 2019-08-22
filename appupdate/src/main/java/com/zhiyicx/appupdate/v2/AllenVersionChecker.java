package com.zhiyicx.appupdate.v2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhiyicx.appupdate.core.http.AllenHttp;
import com.zhiyicx.appupdate.v2.builder.DownloadBuilder;
import com.zhiyicx.appupdate.v2.builder.RequestVersionBuilder;
import com.zhiyicx.appupdate.v2.builder.UIData;
import com.zhiyicx.appupdate.v2.ui.VersionService;

/**
 * Created by allenliu on 2018/1/12.
 */

public class AllenVersionChecker {

//    public AllenVersionChecker() {
//        throw new RuntimeException("AllenVersionChecker can not be instantiated from outside");
//    }
    private AllenVersionChecker(){

    }

    public static AllenVersionChecker getInstance() {
        return AllenVersionCheckerHolder.allenVersionChecker;
    }

    private static class AllenVersionCheckerHolder {
        public static final AllenVersionChecker allenVersionChecker = new AllenVersionChecker();
    }

    public void cancelAllMission(Context context) {
        Intent intent = new Intent(context, VersionService.class);
        context.stopService(intent);
    }

    /**
     * @param versionBundle developer should return version bundle ,to use when showing UI page,could be null
     * @return download builder for download setting
     */
    public DownloadBuilder downloadOnly(@Nullable UIData versionBundle) {
        return new DownloadBuilder(null, versionBundle);
    }

    /**
     * use request version function
     * @return requestVersionBuilder
     */
    public RequestVersionBuilder requestVersion() {
        return new RequestVersionBuilder();
    }

}
