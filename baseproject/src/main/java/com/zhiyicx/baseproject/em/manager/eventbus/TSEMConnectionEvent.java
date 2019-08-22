package com.zhiyicx.baseproject.em.manager.eventbus;

/**
 * @author Jliuer
 * @Date 18/02/06 11:41
 * @Email Jliuer@aliyun.com
 * @Description 自定义连接监听 EventBus post 事件，传递链接变化状态
 */
public class TSEMConnectionEvent {

    /**
     * 链接状态
     */
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TSEMConnectionEvent() {

    }
}
