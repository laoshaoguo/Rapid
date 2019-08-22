package com.haohaishengshi.haohaimusic.data.source.repository;

import com.haohaishengshi.haohaimusic.data.beans.PurChasesBean;
import com.haohaishengshi.haohaimusic.data.beans.UserInfoBean;
import com.haohaishengshi.haohaimusic.data.source.remote.CommonClient;
import com.haohaishengshi.haohaimusic.data.source.remote.ServiceManager;
import com.haohaishengshi.haohaimusic.data.source.repository.i.ICommentRepository;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.net.UpLoadFile;

import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_QUESTIONS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_QUESTIONS_ANSWER;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/14
 * @Contact master.jungle68@gmail.com
 */

public class CommentRepository implements ICommentRepository {
    protected CommonClient mCommonClient;

//    @Inject
//    UserInfoRepository mUserInfoRepository;

    @Inject
    public CommentRepository(ServiceManager serviceManager) {
        mCommonClient = serviceManager.getCommonClient();
    }


    @Override
    public Observable<BaseJson<Object>> sendComment(String comment_content, long reply_to_user_id, long comment_mark, String path) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("body", comment_content);
        if (reply_to_user_id > 0) {
            params.put("reply_user", reply_to_user_id);
        }
        params.put("comment_mark", comment_mark);
        return mCommonClient.handleBackGroundTaskPost(path, UpLoadFile.upLoadFileAndParams(null, params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<Object>> sendCommentV2(String comment_content, long reply_to_user_id, long comment_mark, String path) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("body", comment_content);
        if (reply_to_user_id > 0) {
            params.put("reply_user", reply_to_user_id);
        }
        params.put("comment_mark", comment_mark);
        return mCommonClient.handleBackGroundTaskPostV3(path, UpLoadFile.upLoadFileAndParams(null, params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static String getCommentPath(long source_id, String component_type,long source_parent_id) {
        String path = null;
        if (component_type == null) {
            return path;
        }
        switch (component_type) {
            case ApiConfig.APP_LIKE_FEED:
                path = String.format(ApiConfig.APP_PATH_DYNAMIC_SEND_COMMENT_V2, source_id);

                break;
            case ApiConfig.APP_LIKE_GROUP_POST:
                path = String.format(Locale.getDefault(), ApiConfig.APP_PATH_COMMENT_GROUP_DYNAMIC_FORMAT,(int)source_id);

                break;
            case ApiConfig.APP_LIKE_MUSIC:
                path = String.format(ApiConfig.APP_PATH_MUSIC_COMMENT_FORMAT, source_id);
                break;
            case ApiConfig.APP_COMPONENT_SOURCE_TABLE_MUSIC_SPECIALS:
                path = String.format(ApiConfig.APP_PATH_MUSIC_ABLUM_COMMENT_FORMAT, source_id);
                break;
            case ApiConfig.APP_LIKE_NEWS:
                path = String.format(ApiConfig.APP_PATH_INFO_COMMENT_V2_S, source_id);
                break;
            case APP_QUESTIONS:
                path = String.format(ApiConfig.APP_PATH_SEND_QUESTION_COMMENT_S, source_id);
                break;
            case APP_QUESTIONS_ANSWER:
                path = String.format(Locale.getDefault(), ApiConfig.APP_PATH_COMMENT_QA_ANSWER_FORMAT, source_id);
                break;
            default:
                break;
        }
        return path;
    }

    @Override
    public Observable<PurChasesBean> checkNote(int note) {
        return null;
//        return mCommonClient.checkNote(note)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<String>> paykNote(int note, String psd) {
        return mCommonClient.payNote(note,psd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UserInfoBean> getCurrentLoginUserInfo() {
        return null;
    }
}
