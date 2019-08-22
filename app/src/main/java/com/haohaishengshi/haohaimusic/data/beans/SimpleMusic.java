package com.haohaishengshi.haohaimusic.data.beans;

import android.annotation.SuppressLint;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * ThinkSNS Plus
 * Copyright (c) 2019 Chengdu ZhiYiChuangXiang Technology Co., Ltd.
 *
 * @Author icepring
 * @Date 2019/03/01/17:24
 * @Email Jliuer@aliyun.com
 * @Description
 */
@SuppressLint("ParcelCreator")
public class SimpleMusic extends BaseListBean {

    /**
     * id : 1
     * title : 兰花指
     * singer : 1
     * storage : {"id":2}
     * last_time : 234
     * lyric : 哈哈哈
     * taste_count : 0
     * share_count : 0
     * comment_count : 0
     * sort : 0
     * created_at : 2019-02-28 03:46:56
     * updated_at : 2019-02-28 03:46:56
     * deleted_at : null
     * has_like : false
     */

    private int id;
    private String title;
    private int singer;
    private StorageBean storage;
    private int last_time;
    private String lyric;
    private int taste_count;
    private int share_count;
    private int comment_count;
    private int sort;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private boolean has_like;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSinger() {
        return singer;
    }

    public void setSinger(int singer) {
        this.singer = singer;
    }

    public StorageBean getStorage() {
        return storage;
    }

    public void setStorage(StorageBean storage) {
        this.storage = storage;
    }

    public int getLast_time() {
        return last_time;
    }

    public void setLast_time(int last_time) {
        this.last_time = last_time;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public int getTaste_count() {
        return taste_count;
    }

    public void setTaste_count(int taste_count) {
        this.taste_count = taste_count;
    }

    public int getShare_count() {
        return share_count;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public boolean isHas_like() {
        return has_like;
    }

    public void setHas_like(boolean has_like) {
        this.has_like = has_like;
    }

    public static class StorageBean {
        /**
         * id : 2
         */

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
