package com.haohaishengshi.haohaimusic.data.source.remote;

import com.haohaishengshi.haohaimusic.data.beans.AuthBean;
import com.zhiyicx.appupdate.AppVersionBean;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_CHECK_NOTE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_ADVERT_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_ALL_TAGS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_APP_NEW_VERSION;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_APP_VERSION;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_All_ADVERT_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_BOOTSTRAPERS_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_CHECK_NOTE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_MEMBER_VERTIFYCODE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_NON_MEMBER_VERTIFYCODE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_SINGLE_ADVERT_INFO;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_GET_SYSTEM_CONVERSATIONS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_HANDLE_BACKGROUND_TASK;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_REFRESH_TOKEN;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_REPORT_COMMON_COMMENT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_SEARDCH_LOCATION;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_SGET_HOT_CITY;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_STORAGE_HASH;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_STORAGE_UPLAOD;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_STORAGE_UPLAOD_FILE;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_SYSTEM_FEEDBACK;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_TOKEN_EXPIERD;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public interface CommonClient {


    /*******************************************  系统相关  *********************************************/

    /**
     * 获取客户端更新列表
     *
     * @param after 查询标识，传入当前客户端的版本id
     * @param type  客户端版本类型，暂定参数有 android、ios
     * @param limit 返回数据条数，默认15条
     * @return
     */
    @GET(APP_PATH_GET_APP_VERSION)
    Observable<Object> getAppVersions(@Query("after") Integer after, @Query("type") String type, @Query("limit") Integer limit);

    /**
     * 获取会员验证码 ：使用场景如登陆、找回密码，其他用户行为验证等。
     *
     * @param phone 需要被发送验证码的手机号 Required without email, Send the verification code in sms mode.
     * @param email 需要被发送验证码的邮箱 Required without phone, Send the verification code in mail mode.
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_GET_MEMBER_VERTIFYCODE)
    Observable<Object> getMemberVertifyCode(@Field("phone") String phone, @Field("email") String email);

    /**
     * 获取非会员验证码 ：用于发送不存在于系统中的用户短信，使用场景如注册等。
     *
     * @param phone 需要被发送验证码的手机号 Required without email, Send the verification code in sms mode.
     * @param email 需要被发送验证码的邮箱 Required without phone, Send the verification code in mail mode.
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_GET_NON_MEMBER_VERTIFYCODE)
    Observable<Object> getNonMemberVertifyCode(@Field("phone") String phone, @Field("email") String email);


    /**
     * 刷新 token
     *
     * @return 成功后自动调用 auth 接口，返回信息和 login 一样
     */
    @PATCH(APP_PATH_REFRESH_TOKEN)
    Observable<AuthBean> refreshToken();

    /**
     * 同步刷新 token
     *
     * @return 成功后自动调用 auth 接口，返回信息和 login 一样
     */
    @PATCH(APP_PATH_REFRESH_TOKEN)
    Call<AuthBean> refreshTokenSyn();

    /**
     * 启动信息
     *
     * @return
     */
    @GET(APP_PATH_GET_BOOTSTRAPERS_INFO)
    Observable<SystemConfigBean> getBootstrappersInfo();

    /**
     * 意见反馈
     *
     * @param content
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_SYSTEM_FEEDBACK)
    Observable<Object> systemFeedback(@Field("content") String content, @Field("system_mark") long system_mark);


    /**
     * 版本更新
     *
     * @param version_code 当前版本号
     * @param type         类型：android \ios
     * @return
     */
    @GET(APP_PATH_GET_APP_NEW_VERSION)
    Observable<List<AppVersionBean>> getAppNewVersion(@Query("version_code") Integer version_code, @Query("type") String type);


    /*******************************************  文件上传  *********************************************/

    /**
     * 储存任务创建
     * <p>
     * hash            待上传文件hash值，hash方式md5
     * origin_filename 原始文件名称
     * mime_type       文件mimeType
     * width           图片宽度
     * height          图片高度
     * <p>
     * 校验文件hash V2 api
     *
     * @param hash 文件 MD5 值
     * @return
     */
    @GET(APP_PATH_STORAGE_HASH)
    Observable<BaseJsonV2> checkStorageHash(@Path("hash") String hash);


    /**
     * 付费节点支付
     *
     * @param note
     * @return
     */
    @FormUrlEncoded
    @POST(APP_PATH_CHECK_NOTE)
    Observable<BaseJsonV2<String>> payNote(@Path("note") int note, @Field("password") String psd);

    /**
     * 通过Post方法上传文件
     */
    @Multipart
    @POST
    Observable<String> upLoadFileByPost(@Url String url, @HeaderMap HashMap<String, String> headers, @Part List<MultipartBody.Part> params);

    /**
     * 通过Put方法上传文件
     */
    @Multipart
    @PUT
    Observable<String> upLoadFileByPut(@Url String url, @HeaderMap HashMap<String, String> headers, @Part List<MultipartBody.Part> params);


    /**
     * 通过Post方法上传文件 V2
     */
    @Multipart
    @POST(APP_PATH_STORAGE_UPLAOD_FILE)
    Observable<BaseJsonV2> upLoadFileByPostV2(@Part List<MultipartBody.Part> params);



    /**
     * 热门城市
     *
     * @return
     */
    @GET(APP_PATH_SGET_HOT_CITY)
    Observable<List<String>> getHoCity();

    /*******************************************  后台任务处理  *********************************************/

    /**
     * 后台任务处理
     */
    @Multipart
    @POST(APP_PATH_HANDLE_BACKGROUND_TASK)
    Observable<BaseJson<Object>> handleBackGroundTaskPost(@Path(value = "path", encoded = true) String path, @Part List<MultipartBody.Part> partList);

    @Multipart
    @POST(APP_PATH_HANDLE_BACKGROUND_TASK)
    Observable<Object> handleBackGroundTaskPostV2(@Path(value = "path", encoded = true) String path, @Part List<MultipartBody.Part> partList);

    @Multipart
    @POST(APP_PATH_HANDLE_BACKGROUND_TASK)
    Observable<BaseJsonV2<Object>> handleBackGroundTaskPostV3(@Path(value = "path", encoded = true) String path, @Part List<MultipartBody.Part> partList);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @HTTP(method = "DELETE", path = APP_PATH_HANDLE_BACKGROUND_TASK, hasBody = true)
    Observable<Object> handleBackGroudTaskDelete(@Path(value = "path", encoded = true) String path, @Body RequestBody requestBody);

    @PUT(APP_PATH_HANDLE_BACKGROUND_TASK)
    Observable<Object> handleBackGroundTaskPut(@Path(value = "path", encoded = true) String path);

    /**
     * rap接口，用来测试token过期,当前返回token过期
     *
     * @return
     */
    @POST(APP_PATH_TOKEN_EXPIERD)
    Observable<BaseJson> testTokenExpierd(@Query("requestState") String requestState);

    @Multipart
    @PATCH(APP_PATH_HANDLE_BACKGROUND_TASK)
    Observable<BaseJsonV2<Object>> handleBackGroundTaskPatch(@Path(value = "path", encoded = true) String path, @Part List<MultipartBody.Part> partList);


}