package com.haohaishengshi.haohaimusic.data.source.repository.i;

import com.haohaishengshi.haohaimusic.data.beans.AuthBean;
import com.haohaishengshi.haohaimusic.data.beans.UserInfoBean;
import com.haohaishengshi.haohaimusic.data.beans.UserPermissions;
import com.haohaishengshi.haohaimusic.data.beans.UserTagBean;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * @Describe 用户相关
 * @Author Jungle68
 * @Date 2017/12/19
 * @Contact master.jungle68@gmail.com
 */

public interface IUserInfoRepository {

    /**
     * 注册
     *
     * @param phone       注册的手机号码
     * @param name        用户名
     * @param vertifyCode 手机验证码
     * @param password    用户密码
     * @return
     */
    Observable<AuthBean> registerByPhone(String phone, String name, String vertifyCode, String password);

    /**
     * @param email       注册的邮箱
     * @param name        用户名
     * @param vertifyCode 邮箱验证码
     * @param password    用户密码
     * @return
     */
    Observable<AuthBean> registerByEmail(String email, String name, String vertifyCode, String password);

    Observable<AuthBean> loginV2(final String account, final String password, String verifiable_code);

    /**
     * 从本地文件获取全国所有的省市
     */
//    Observable<ArrayList<AreaBean>> getAreaList();

    /**
     * 编辑用户信息
     *
     * @param userInfos 用户需要修改的信息，通过 hashMap 传递，key 表示请求字段，value 表示修改的值
     */
//    Observable<Object> changeUserInfo(UpdateUserInfoTaskParams userInfos);

    /**
     * 获取用户信息
     *
     * @param userIds 用户 id 数组
     * @return
     */
    Observable<List<UserInfoBean>> getUserInfo(List<Object> userIds);

    /**
     * 获取用户信息
     *
     * @param userIds           userIds 用户 id 数组
     * @param justUseLoacalData true 当数据库存在该用户时，不掉接口
     * @return
     */
    Observable<List<UserInfoBean>> getUserInfo(List<Object> userIds, boolean justUseLoacalData);


    /**
     * <p>获取当前登录用户信息<p>
     *
     * @return
     */
    Observable<UserInfoBean> getCurrentLoginUserInfo();

    /**
     * 获取指定用户信息  其中 following、follower 是可选参数，验证用户我是否关注以及是否关注我的用户 id ，默认为当前登陆用户。
     *
     * @param userId          the specified user id
     * @param followingUserId following user id
     * @param followerUserId  follow user id
     * @return
     */
    Observable<UserInfoBean> getSpecifiedUserInfo(long userId, Long followingUserId, Long followerUserId);

    /**
     * 批量获取指定用户的用户信息
     *
     * @param user_ids user 可以是一个值，或者多个值，多个值的时候用英文半角 , 分割。
     * @return
     */
    Observable<List<UserInfoBean>> getUserInfoByIds(String user_ids);

    Observable<List<UserInfoBean>> getUserInfoByNames(String user_names);

    Observable<List<UserInfoBean>> getUserInfoWithOutLocalByIds(String userIds);

    /**
     * 搜索用户的用户信息
     *
     * @param user_ids Get multiple designated users, multiple IDs using , split.
     * @param name     Used to retrieve users whose username contains name.
     * @param offset   The integer ID of the last User that you've seen.
     * @param order    Sorting. Enum: asc, desc
     * @param limit    List user limit, minimum 1 max 50.
     * @return
     */
    Observable<List<UserInfoBean>> searchUserInfo(String user_ids, String name, Integer offset, String order, Integer limit);

    /**
     * 获取用户信息,先从本地获取，本地没有再从网络 获取
     *
     * @param user_id 用户 id
     * @return
     */
    Observable<UserInfoBean> getLocalUserInfoBeforeNet(long user_id);

    /**
     * 关注操作
     *
     * @param followFansBean
     */
    void handleFollow(UserInfoBean followFansBean);

    /**
     * 获取用户收到的点赞
     *
     * @param max_id
     * @return
     */
//    Observable<List<DigedBean>> getMyDiggs(int max_id);


    /**
     * 获取用户收到的评论
     *
     * @param max_id
     * @return
     */
//    Observable<List<CommentedBean>> getMyComments(int max_id);

    /**
     * 获取评论，参数巨多
     *
     * @param limit            条数限制
     * @param index            max
     * @param direction        可选，数据排序方向，以 id 进行排序，支持 asc 或 desc，默认 desc。
     * @param author           可选，需要筛选的评论作者，传递用户 ID。
     * @param forUser          可选，需要获取关于某用户的评论，传递用户 ID。
     * @param forType          可选，获取关于某用户评论的方式，默认 all，支持 all: 全部、target: 评论我的和 reply: 回复我的。
     * @param resourceableId   可选，需要以资源 ID 为条件查询的评论，多个以 , 进行分割。
     * @param resourceableType 可选，如果 resourceable_id 存在则必须 ，资源类型标识。
     * @param id               可选，多个评论 ID 使用 , 进行分割；如果存在本参数，除了 direction 外，其他参数均失效。
     * @return
     * @ url https://slimkit.github.io/docs/api-v2-core-comment.html
     */
//    Observable<List<AtMeaasgeBean>> getAllComments(Integer limit, int index, String direction,
//                                                   Long author, Long forUser,
//                                                   String forType, String resourceableId,
//                                                   String resourceableType, String id);


    /**
     * 更新认证用户的手机号码和邮箱
     *
     * @param phone
     * @param email
     * @param verifiable_code
     * @return
     */
    Observable<Object> updatePhoneOrEmail(String phone, String email, String verifiable_code);

    /**
     * 解除用户 Phone 绑定:
     *
     * @param password
     * @param verify_code
     * @return
     */
    Observable<Object> deletePhone(String password, String verify_code);

    /**
     * 解除用户 E-Mail 绑定:
     *
     * @param password
     * @param verify_code
     * @return
     */
    Observable<Object> deleteEmail(String password, String verify_code);


    /*******************************************  标签  *********************************************/

    /**
     * 获取一个用户的标签
     *
     * @param user_id
     * @return
     */
    Observable<List<UserTagBean>> getUserTags(long user_id);

    /**
     * 获取当前认证用户的标签
     *
     * @return
     */
    Observable<List<UserTagBean>> getCurrentUserTags();

    /**
     * 当前认证用户附加一个标签
     *
     * @param tag_id
     * @return
     */
    Observable<Object> addTag(long tag_id);

    /**
     * 当前认证用户分离一个标签
     *
     * @param tag_id
     * @return
     */
    Observable<Object> deleteTag(long tag_id);

    /*******************************************  找人  *********************************************/


    /**
     * 热门用户
     *
     * @param limit  每页数量
     * @param offset 偏移量, 注: 此参数为之前获取数量的总和
     * @return
     */
    Observable<List<UserInfoBean>> getHotUsers(Integer limit, Integer offset);

    /**
     * 最新用户
     *
     * @param limit  每页数量
     * @param offset 偏移量, 注: 此参数为之前获取数量的总和
     * @return
     */
    Observable<List<UserInfoBean>> getNewUsers(Integer limit, Integer offset);

    /**
     * tag 推荐用户
     *
     * @param limit  每页数量
     * @param offset 偏移量, 注: 此参数为之前获取数量的总和
     * @return
     */
    Observable<List<UserInfoBean>> getUsersRecommentByTag(Integer limit, Integer offset);

    /**
     * @return 后台推荐用户
     */
    Observable<List<UserInfoBean>> getRecommendUserInfo();

    /**
     * phone 推荐用户
     * <p>
     * { "phones": [ 18877778888, 18999998888, 17700001111 ] }
     *
     * @return
     */
    Observable<List<UserInfoBean>> getUsersByPhone(ArrayList<String> phones);


    /**
     * 更新位置数据
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return
     */
    Observable<Object> updateUserLocation(double longitude, double latitude);

    /**
     * 根据经纬度查询周围最多 50KM 内的 TS+ 用户
     *
     * @param longitude 当前用户所在位置的纬度
     * @param latitude  当前用户所在位置的经度
     * @param radius    搜索范围，米为单位 [0 - 50000], 默认3000
     * @param limit     默认20， 最大100
     * @param page      分页参数， 默认1，当返回数据小于limit， page达到最大值
     * @return
     */
//    Observable<List<NearbyBean>> getNearbyData(double longitude, double latitude, Integer radius, Integer limit, Integer page);


    /*******************************************  签到  *********************************************/

    /**
     * 获取签到信息
     *
     * @return
     */
//    Observable<CheckInBean> getCheckInInfo();

    /**
     * 签到
     *
     * @return
     */
    Observable<Object> checkIn();

    /**
     * 连续签到排行榜
     *
     * @param offset 数据偏移数，默认为 0。
     * @return
     */
    Observable<List<UserInfoBean>> getCheckInRanks(Integer offset);


    /*******************************************  三方登录  *********************************************/

    /**
     * 获取已经绑定的三方
     * qq	    腾讯 QQ 。
     * weibo	新浪 Weibo 。
     * wechat	腾讯微信 。
     *
     * @return 请求成功后，将返回用户已绑定第三方的 provider 名称，不存在列表中的代表用户并为绑定。
     */
    Observable<List<String>> getBindThirds();

    /**
     * 检查绑定并获取用户授权
     *
     * @param access_token thrid token
     * @return 返回的数据参考 「用户／授权」接口，如果返回 404 则表示没有改账号没有注册，进入第三方登录注册流程。
     */
    Observable<AuthBean> checkThridIsRegitser(String provider, String access_token);

    /**
     * 检查注册信息或者注册用户
     *
     * @param provider     type qq\weibo\wechat
     * @param access_token 获取的 Provider Access Token。
     * @param name         用户名。
     * @param check        如果是 null 、 false 或 0 则不会进入检查，如果 存在任何转为 bool 为 真 的值，则表示检查注册信息。
     * @return
     */
    Observable<AuthBean> checkUserOrRegisterUser(String provider, String access_token, String name, Boolean check);

    /**
     * 已登录账号绑定
     *
     * @param provider
     * @param access_token
     * @return
     */
    Observable<Object> bindWithLogin(String provider, String access_token);

    /**
     * 输入账号密码绑定
     *
     * @param provider     type qq\weibo\wechat
     * @param access_token 获取的 Provider Access Token。
     * @param login        用户登录名，手机，邮箱
     * @param password     用户密码。
     * @return
     */
    Observable<AuthBean> bindWithInput(String provider, String access_token, String login, String password);

    /**
     * 取消绑定
     *
     * @param provider type qq\weibo\wechat
     * @return
     */
    Observable<Object> cancelBind(String provider);

    /**
     * 获取认证信息
     *
     * @return
     */
//    Observable<UserCertificationInfo> getCertificationInfo();

    /**
     * 提交认证信息
     *
     * @param bean
     * @return
     */
//    Observable<BaseJsonV2<Object>> sendCertification(SendCertificationBean bean);

    /**
     * @param userId
     * @param maxId
     * @return 获取关注列表
     */
    Observable<List<UserInfoBean>> getFollowListFromNet(long userId, int maxId);

    /**
     * 获取用户好友列表
     *
     * @param maxId   offset
     * @param keyword 搜索用的关键字
     * @return Observable
     */
    Observable<List<UserInfoBean>> getUserFriendsList(long maxId, String keyword);

    /**
     * @param userId
     * @param maxId
     * @return 获取粉丝列表
     */
    Observable<List<UserInfoBean>> getFansListFromNet(long userId, int maxId);


    /**
     * User Append Follower Count
     *
     * @return 新增关注数
     */
//    Observable<UserFollowerCountBean> getUserAppendFollowerCount();

    Observable<Object> clearUserMessageCount(String type);

    Observable<Object> clearUserMessageCount(

    );

    /**
     * @param offset 偏移量，当前请求数据条数
     * @return 当前登录用户的黑名单列表
     */
    Observable<List<UserInfoBean>> getUserBlackList(Long offset);

    /**
     * @param userId 用户 id
     * @return 把用户加入黑名单
     */
    Observable<Object> addUserToBlackList(Long userId);

    /**
     * @param userId 用户 id
     * @return 把用户移除黑名单
     */
    Observable<Object> removeUserFromBlackList(Long userId);


    /**
     * 获取当前登录用户权限信息
     *
     * @return 权限列表信息
     */
    Observable<List<UserPermissions>> getCurrentLoginUserPermissions();

}
