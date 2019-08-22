package com.zhiyicx.common.mvp.i;

import com.trycatch.mysnackbar.Prompt;

/**
 * @Describe view 公用接口
 * @Author Jungle68
 * @Date 2016/12/14
 * @Contact 335891510@qq.com
 */

public interface IBaseView<VP> {
    void setPresenter(VP presenter);

    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示信息
     */
    void showMessage(String message);

    /**
     * 从顶部显示信息
     */
    void showSnackMessage(String message, Prompt prompt);

    /**
     * 从顶部显示成功信息
     */
    void showSnackSuccessMessage(String message);

    /**
     * 从顶部显示失败信息
     */
    void showSnackErrorMessage(String message);

    /**
     * 从顶部显示警告信息
     */
    void showSnackWarningMessage(String message);

    /**
     * 从顶部显示持续 加载信息，直到下一个出现，或者手动停止
     *
     * @param message
     */
    void showSnackLoadingMessage(String message);

    /**
     * 隐藏 SnackBar
     */
    void dismissSnackBar();

    /**
     * 显示登录提示框
     */
    void showLoginPop();

    /**
     * 屏幕中间带文字的 loading 窗口
     */
    void showCenterLoading(String msg);

    void hideCenterLoading();

    void closeLoadingView();

    /**
     * 去充值
     */
    void goTargetActivity(Class<?> cls);

    void paySuccess();
    void payFailed(String msg);

}
