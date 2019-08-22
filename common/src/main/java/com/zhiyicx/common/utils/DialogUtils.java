package com.zhiyicx.common.utils;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/27
 * @Contact master.jungle68@gmail.com
 */

public class DialogUtils {

    public static AlertDialog.Builder getDialog(AlertDialog.Builder builder,
                                                DialogInterface.OnClickListener listener, String title, String message, String nagativeMessage, String positiveMessage) {
        builder.setTitle(title);
        builder.setMessage(message);
        if (!TextUtils.isEmpty(nagativeMessage))
            builder.setNegativeButton(nagativeMessage, null);
        builder.setPositiveButton(positiveMessage, listener);
        return builder;
    }

}
