package com.zhiyicx.appupdate.v2.callback;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhiyicx.appupdate.core.VersionParams;
import com.zhiyicx.appupdate.v2.builder.UIData;

/**
 * Created by allenliu on 2018/1/12.
 */

public interface RequestVersionListener {
    /**
     *
     * @param result the result string of request
     * @return developer should return version bundle ,to use when showing UI page,could be null
     */
    @Nullable
    UIData onRequestVersionSuccess(String result);
    void onRequestVersionFailure(String message);

}
