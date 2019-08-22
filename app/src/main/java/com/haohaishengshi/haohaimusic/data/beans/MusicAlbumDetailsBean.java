package com.haohaishengshi.haohaimusic.data.beans;

import com.haohaishengshi.haohaimusic.data.source.local.data_convert.BaseConvert;
import com.haohaishengshi.haohaimusic.data.source.local.data_convert.PaidNoteConverter;
import com.haohaishengshi.haohaimusic.data.source.local.data_convert.StorageConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/03/02
 * @Email Jliuer@aliyun.com
 * @Description 专辑详情
 */
@Entity
public class MusicAlbumDetailsBean implements Serializable  {
    private static final long serialVersionUID = 1L;


    /**
     * id : 2
     * created_at : 2017-03-15 17:04:31
     * updated_at : 2017-06-27 18:40:56
     * title : 少女情怀总是诗
     * intro : 耶嘿 杀乌鸡
     * storage : {"id":108,"size":"3024x3024"}
     * taste_count : 845
     * share_count : 21
     * comment_count : 97
     * collect_count : 9
     * paid_node : {"paid":true,"node":1,"amount":200}
     * has_collect : true
     * musics : [{"id":7,"created_at":"2017-04-17 15:27:59","updated_at":"2017-07-06 03:53:04","deleted_at":null,"title":"umbrella","singer":{"id":2,"created_at":"2017-03-16 17:22:18","updated_at":"2017-03-16 17:22:20","name":"佚名","cover":null},"storage":{"id":113},"last_time":300,"lyric":null,"taste_count":0,"share_count":0,"comment_count":0,"has_like":true},{"id":3,"created_at":"2017-03-16 17:21:09","updated_at":"2017-07-06 08:01:18","deleted_at":null,"title":"别碰我的人","singer":{"id":1,"created_at":"2017-03-16 17:22:04","updated_at":"2017-03-16 17:22:08","name":"群星","cover":null},"storage":{"id":109},"last_time":200,"lyric":null,"taste_count":297,"share_count":0,"comment_count":23,"has_like":true}]
     */
    @Id
    private Long id;
    private String created_at;
    private String updated_at;
    private String title;
    private String intro;
    @Convert(converter = StorageConvert.class, columnType = String.class)
    private StorageBean storage;
    private int taste_count;
    private int share_count;
    private int comment_count;
    private int collect_count;
    @Convert(converter = PaidNoteConverter.class, columnType = String.class)
    private PaidNote paid_node;
    private boolean has_collect;
    @Convert(converter = MusicsBeanConvert.class, columnType = String.class)
    private List<MusicDetaisBean> musics;


    @Generated(hash = 1130780264)
    public MusicAlbumDetailsBean(Long id, String created_at, String updated_at, String title, String intro, StorageBean storage, int taste_count, int share_count, int comment_count, int collect_count, PaidNote paid_node, boolean has_collect, List<MusicDetaisBean> musics) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.title = title;
        this.intro = intro;
        this.storage = storage;
        this.taste_count = taste_count;
        this.share_count = share_count;
        this.comment_count = comment_count;
        this.collect_count = collect_count;
        this.paid_node = paid_node;
        this.has_collect = has_collect;
        this.musics = musics;
    }


    @Generated(hash = 1417869479)
    public MusicAlbumDetailsBean() {
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getCreated_at() {
        return this.created_at;
    }


    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


    public String getUpdated_at() {
        return this.updated_at;
    }


    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


    public String getTitle() {
        return this.title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getIntro() {
        return this.intro;
    }


    public void setIntro(String intro) {
        this.intro = intro;
    }


    public StorageBean getStorage() {
        return this.storage;
    }


    public void setStorage(StorageBean storage) {
        this.storage = storage;
    }


    public int getTaste_count() {
        return this.taste_count;
    }


    public void setTaste_count(int taste_count) {
        this.taste_count = taste_count;
    }


    public int getShare_count() {
        return this.share_count;
    }


    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }


    public int getComment_count() {
        return this.comment_count;
    }


    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }


    public int getCollect_count() {
        return this.collect_count;
    }


    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }


    public PaidNote getPaid_node() {
        return this.paid_node;
    }


    public void setPaid_node(PaidNote paid_node) {
        this.paid_node = paid_node;
    }


    public boolean getHas_collect() {
        return this.has_collect;
    }


    public void setHas_collect(boolean has_collect) {
        this.has_collect = has_collect;
    }


    public List<MusicDetaisBean> getMusics() {
        return this.musics;
    }


    public void setMusics(List<MusicDetaisBean> musics) {
        this.musics = musics;
    }

    public static class MusicsBeanConvert extends BaseConvert<List<MusicDetaisBean>> {
    }

}
