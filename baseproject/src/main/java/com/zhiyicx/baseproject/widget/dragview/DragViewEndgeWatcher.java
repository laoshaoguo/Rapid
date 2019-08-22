package com.zhiyicx.baseproject.widget.dragview;

/**
 * @author LiuChao
 * @describe 处理ViewDragHelper和子控件的滑动冲突，主要是判断在还没有滑动到子控件边缘时，应该由
 * 子控件内部处理分发事件，到达边缘才能由ViewDragHelper接手分发事件
 * @date 2017/3/25
 * @contact email:450127106@qq.com
 */

public interface DragViewEndgeWatcher {
    /**
     * 是否滑动到底部，针对于上下滑动
     *
     * @return
     */
    boolean isScrollToBottom();

    /**
     * 是否滑动到顶部，针对于上下滑动
     *
     * @return
     */
    boolean isScrollToTop();

    /**
     * 是否滑动到左边缘，针对于左右滑动
     *
     * @return
     */

    boolean isScrollToLeftEdge();

    /**
     * 是否滑动到右边缘，针对于左右滑动
     *
     * @return
     */
    boolean isScrollToRightEdge();
}
