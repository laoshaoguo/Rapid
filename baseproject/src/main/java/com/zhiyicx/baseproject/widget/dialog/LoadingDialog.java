package com.zhiyicx.baseproject.widget.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;


/**
 * @author LiuChao
 * @describe 提示信息弹框，包括发送失败，发送成功，正在发送
 * @date 2017/2/5
 * @contact email:450127106@qq.com
 */

public class LoadingDialog {
    private static final int SUCCESS_ERROR_STATE_TIME = 1500;// 成功或者失败的停留时间
    private final int HANDLE_DELAY = 0;
    private final float DEFAULT_ALPHA = 0.8f;
    private AlertDialog sLoadingDialog;
    private AnimationDrawable mAnimationDrawable;
    private View layoutView;
    private ImageView iv_hint_img;
    private TextView tv_hint_text;

    private Activity mActivity;


    public LoadingDialog(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 显示错误的状态
     *
     * @param text 错误或失败状态的提示消息
     */
    public void showStateError(String text) {
        initDialog(R.mipmap.msg_box_remind, text, false);
        sendHideMessage();
    }

    /**
     * 显示成功的状态
     *
     * @param text 正确或成功状态的提示消息
     */
    public void showStateSuccess(String text) {
        initDialog(R.mipmap.msg_box_succeed, text, false);
        sendHideMessage();
    }

    /**
     * 显示进行中的状态
     *
     * @param text 进行中的提示消息
     */
    public void showStateIng(String text) {
        initDialog(R.drawable.frame_loading_grey, text, false);
        handleAnimation(true);
    }

    /**
     * 进行中的状态变为结束
     **/
    public void showStateEnd() {
        handleAnimation(false);
        hideDialog();
    }

    public void onDestroy() {
        if (sLoadingDialog != null) {
            sLoadingDialog.dismiss();
        }
    }

    /**
     * 处理动画
     *
     * @param status true 开启动画，false 关闭动画
     */
    private void handleAnimation(boolean status) {
        mAnimationDrawable = (AnimationDrawable) iv_hint_img.getDrawable();
        if (mAnimationDrawable == null)
            throw new IllegalArgumentException("load animation not be null");
        if (status) {
            if (!mAnimationDrawable.isRunning()) {
                mAnimationDrawable.start();
            }
        } else {
            if (mAnimationDrawable.isRunning()) {
                mAnimationDrawable.stop();
            }
        }
    }


    private void initDialog(Integer imgRsId, String hintContent, boolean outsideCancel) {
        if (sLoadingDialog == null) {
            layoutView = LayoutInflater.from(mActivity).inflate(com.zhiyicx.baseproject.R.layout.view_hint_info1, null);
            iv_hint_img = (ImageView) layoutView.findViewById(com.zhiyicx.baseproject.R.id.iv_hint_img);
            tv_hint_text = (TextView) layoutView.findViewById(com.zhiyicx.baseproject.R.id.tv_hint_text);
            sLoadingDialog = new AlertDialog.Builder(mActivity, R.style.loadingDialogStyle)
                    .setCancelable(outsideCancel)
                    .create();
            sLoadingDialog.setCanceledOnTouchOutside(outsideCancel);
            sLoadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    setWindowAlpha(1.0f);
                }
            });
        }
        tv_hint_text.setText(hintContent);
        iv_hint_img.setImageResource(imgRsId);
        showDialog();
        sLoadingDialog.setContentView(layoutView);// 必须放在show方法后面
    }

    /**
     * 发送关闭窗口的延迟消息
     */
    private void sendHideMessage() {
        Message message = Message.obtain();
        message.what = HANDLE_DELAY;
        mHandler.sendMessageDelayed(message, SUCCESS_ERROR_STATE_TIME);
    }

    private  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLE_DELAY && sLoadingDialog != null) {
                hideDialog();
            }
        }
    };

    // Dialog有可能在activity销毁后，调用，这样会发生dialog找不到窗口的错误，所以需要先判断是否有activity
    private void showDialog() {
        setWindowAlpha(DEFAULT_ALPHA);
        if (mActivity != null && isValidActivity(mActivity)) {
            sLoadingDialog.show();
        }
    }

    private void hideDialog() {
        if (mActivity != null && isValidActivity(mActivity)) {
            sLoadingDialog.dismiss();
        }
    }

    /**
     * 判断一个界面是否还存在
     * 使用场景：比如  一个activity被销毁后，它的dialog还要执行某些操作，比如dismiss和show这样是不可以的
     * 因为 dialog是属于activity的
     *
     * @param c
     * @return
     */
    @TargetApi(17)
    private boolean isValidActivity(Activity c) {
        if (c == null) {
            return false;
        }

        if (c.isDestroyed() || c.isFinishing()) {
            return false;
        } else {
            return true;
        }
    }

    private void setWindowAlpha(float alpha) {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = alpha;
        params.verticalMargin = 100;
        mActivity.getWindow().setAttributes(params);
    }
}
