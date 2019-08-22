package com.zhiyicx.common.base;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/06/20/18:09
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BaseJsonV2<T> {

    @SerializedName(value = "status",alternate = {"need_review"})
    protected boolean status;
    protected int code;
    protected int id = -1;
    /**
     * 数据类型
     * answer         --  问题回答
     * question       --  问题
     * comment        --  评论
     * count          --  圈子总数
     * post           --  圈子帖子
     * group          --  圈子信息
     * protocol       --  圈子协议
     */
    @SerializedName(value = "data",alternate = {"answer","question","comment","count","post","group","protocol"})
    private T data;
    private Object message;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getMessage() {
        List<String> resultMsg;
        if (message instanceof List) {
            resultMsg = (List<String>) message;
        } else {
            resultMsg = new ArrayList<>();
            resultMsg.add((String) message);
        }
        return resultMsg;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
