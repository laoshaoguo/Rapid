package com.zhiyicx.baseproject.widget.popwindow;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;

/**
 * @author LiuChao
 * @describe 用于权限验证的提示弹框：提示用户去设置页面允许权限通过
 * @date 2017/4/1
 * @contact email:450127106@qq.com
 */

public class PermissionPopupWindow extends ActionPopupWindow {

    private String mPermissionName;

    public PermissionPopupWindow(PermissionBuilder builder) {
        super(builder);
        this.mPermissionName = builder.permissionName;
        initView();
    }

    @Override
    protected boolean canInitView() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ppw_for_permission;
    }

    public static PermissionPopupWindow.PermissionBuilder builder() {
        return new PermissionPopupWindow.PermissionBuilder();
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        // 添加权限文字标题
        if (!TextUtils.isEmpty(mPermissionName)) {
            TextView permissionName = (TextView) mContentView.findViewById(R.id.tv_permission_name);
            permissionName.setVisibility(View.VISIBLE);
            permissionName.setText(mPermissionName);
        }
    }

    public static class PermissionBuilder extends Builder {
        private String permissionName;// 一个扩展字段，用于子类添加额外信息

        private PermissionBuilder() {
            super();
        }

        public PermissionPopupWindow.PermissionBuilder permissionName(String permissionName) {
            this.permissionName = permissionName;
            return this;
        }

        public ActionPopupWindow build() {
            return new PermissionPopupWindow(this);
        }

    }
}
