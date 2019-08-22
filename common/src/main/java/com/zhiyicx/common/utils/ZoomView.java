package com.zhiyicx.common.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author LiuChao
 * @describe 可以缩放的View, 和可滑动的列表绑定，摸摸大
 * @date 2017/3/8
 * @contact email:450127106@qq.com
 */

public class ZoomView {
    // 控件移动的最大放缩距离，单位像素
    private static final int MAX_DEFAULT_DISTANCE = 300;
    // 最小可刷新的距离：控件累计位移如果小于它，就无法刷新：为MAX_DEFAULT_DISTANCE的一半
    public static final int CAN_REFRESH_DISTANCE = 150;
    // 滑动系数，用来处理手指滑动和布局放大的系数比：布局放大尺寸=手指滑动距离*DEFAULT_MOVE_RATIO
    public static final float DEFAULT_MOVE_RATIO = 0.6f;
    // 是否正在放大
    private Boolean mScaling = false;
    // 记录首次按下位置
    private float mFirstPosition = 0;
    // 需要进行放缩的控件
    private View zoomView;
    // 可滑动的列表，当前使用的RecyclerView，你也可以自己添加更多类型的列表
    private RecyclerView mRecyclerView;
    // RecyclerView的布局管理器，用来判断第一个item的位置，如果你把RecyclerView改成其他的ListView，ScrollView，修改判断逻辑
    private LinearLayoutManager mLinearLayoutManager;
    private Activity mActivity;
    // 缩放view的初始宽高,单位像素
    private int originWidth, originHeight;
    // 这个值可以有构造方法传入，先放这儿，留给有缘人
    private int max_distance = MAX_DEFAULT_DISTANCE;
    private int can_refresh_distance = CAN_REFRESH_DISTANCE;
    //监听刷新
    private ZoomTouchListenerForRefresh mZoomTouchListenerForRefresh;

    public ZoomView(View zoomView, Activity activity, RecyclerView recyclerView, int originWidth, int originHeight) {
        this.zoomView = zoomView;
        mActivity = activity;
        mRecyclerView = recyclerView;
        mLinearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        this.originHeight = originHeight;
        this.originWidth = originWidth;

        int halfScreenHeight = DeviceUtils.getScreenHeight(activity) / 2;
        // 下边缘可以放大到屏幕高度一半
        // 如果控件原始高度太矮，导致无法放大到屏幕一半的位置，就增大max_distance的距离，否则就用默认的max_distance
        if (originHeight + max_distance <= halfScreenHeight) {
            max_distance = halfScreenHeight - originHeight;
        }
        //LogUtils.i("ZoomViewConstructor" + "   max_distance" + max_distance + "  originHeight" + originHeight);
        can_refresh_distance = max_distance / 2;//可刷新距离，为最大移动距离的一半

    }

    /**
     * 初始化view的缩放
     */
    public void initZoom() {
        initzoomView();
    }

    /**
     * view缩放的处理
     */
    private void initzoomView() {
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressWarnings("AlibabaSwitchStatement")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup.LayoutParams lp = zoomView.getLayoutParams();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mScaling = false;
                        replyImage();
                        int upCanAcessDistance = (int) (event.getY() - mFirstPosition);// 手指移动距离
                        int upScaleDistance = (int) (upCanAcessDistance * DEFAULT_MOVE_RATIO);// 添加移动参数，增大移动距离和放大尺寸的比例
                        int upDistance = upScaleDistance * originHeight / originWidth;//计算释放时控件垂直方向总的位移
                        if (mZoomTouchListenerForRefresh != null) {
                            // 监听控件移动距离
                            mZoomTouchListenerForRefresh.moving(upDistance);
                        }
                        if (upDistance >= can_refresh_distance) {
                            // 可刷新区域
                            if (mZoomTouchListenerForRefresh != null) {
                                mZoomTouchListenerForRefresh.refreshStart(upDistance);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //LogUtils.i("zoomView-->" + zoomView.getTop());
                        if (!mScaling) {
                            //当图片也就是第一个item完全可见的时候，记录触摸屏幕的位置
                            if (mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                                mFirstPosition = event.getY();
                            } else {
                                break;
                            }
                        }
                        // 实际监听到的滑动距离：第一个item完全可见的时候
                        int canAcessDistance = (int) (event.getY() - mFirstPosition);
                        int scaleDistance = (int) (canAcessDistance * DEFAULT_MOVE_RATIO); // 滚动距离乘以一个系数
                        int distance = scaleDistance * originHeight / originWidth;//计算释放时控件垂直方向总的位移
                        if (mZoomTouchListenerForRefresh != null) {
                            // 监听控件移动距离
                            mZoomTouchListenerForRefresh.moving(distance);
                        }
                        // 控制最大的下拉距离
                        if (distance < 0 || distance > max_distance) {
                            break;
                        }

                        if (mZoomTouchListenerForRefresh != null) {
                            // 是否处于可刷新区域
                            mZoomTouchListenerForRefresh.canRefresh(distance, distance >= can_refresh_distance);
                        }
                        // 处理放大，需要注意的是，被放缩的控件一定要在父布局的水平方向居中显示，这样放大，才会往两边扩展
                        mScaling = true;
                        lp.width = originWidth + scaleDistance;
                        lp.height = originHeight + distance;
                        zoomView.setLayoutParams(lp);
                        return true; // 返回true表示已经完成触摸事件，不再处理
                }
                return false;
            }
        });

    }

    /**
     * 松开手指，view复原动画
     */
    private void replyImage() {
        final ViewGroup.LayoutParams lp = zoomView.getLayoutParams();
        final float w = zoomView.getLayoutParams().width;// 图片当前宽度
        final float h = zoomView.getLayoutParams().height;// 图片当前高度
        final float newW = originWidth;// 图片原宽度
        final float newH = originHeight;// 图片原高度

        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(200);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                lp.width = (int) (w - (w - newW) * cVal);
                lp.height = (int) (h - (h - newH) * cVal);
                zoomView.setLayoutParams(lp);
            }
        });
        anim.start();

    }

    public void setZoomTouchListenerForRefresh(ZoomTouchListenerForRefresh zoomTouchListenerForRefresh) {
        this.mZoomTouchListenerForRefresh = zoomTouchListenerForRefresh;
    }

    /**
     * 监听zoomviewTouch事件:用于刷新等操作
     */
    public interface ZoomTouchListenerForRefresh {

        /**
         * 手指移动，监听滑动距离和方向
         *
         * @param moveDistance 控件移动距离
         */
        void moving(int moveDistance);

        /**
         * 刷新开始
         *
         * @param moveDistance 控件移动距离
         */
        void refreshStart(int moveDistance);

        /**
         * 刷新完成:该接口方法需要用户手动调用
         */
        void refreshEnd();

        /**
         * 监听可刷新状态
         *
         * @param canRefresh   控件到该处，如果释放，是否可以刷新
         * @param moveDistance 控件移动距离
         */
        void canRefresh(int moveDistance, boolean canRefresh);

    }

}
