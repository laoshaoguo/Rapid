package com.zhiyicx.appupdate.v2.callback;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.zhiyicx.appupdate.v2.builder.UIData;

/**
 * Created by allenliu on 2018/1/18.
 */

public interface CustomVersionDialogListener {
    Dialog getCustomVersionDialog(Context context,UIData versionBundle);
}
