package com.zhiyicx.baseproject.base;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.config.ApiConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/18
 * @Contact master.jungle68@gmail.com
 */

public class SystemConfigBean implements Serializable {

    private static final long serialVersionUID = -2767044631905981596L;

    @SerializedName("server:version")
    private String serverVersion;

    private SiteBean site;
    private RegisterSettingsBean registerSettings;

    @SerializedName("im:serve")
    private String im_serve;
    /**
     * 新版小助手
     */
    @SerializedName("im:helper-user")
    private String newImHelper;

    @SerializedName("ad")
    private ArrayList<Advert> mAdverts; // 广告

    @SerializedName("news")
    private NewsConfig mNewsContribute;
    @SerializedName("Q&A")
    private QuestionConfig mQuestionConfig;
    @SerializedName("plus-appversion")
    private Appversion mAppversion;
    @SerializedName("feed")
    private Feed mFeed;
    private CheckIn checkin;
    @SerializedName("pay-validate-user-password")
    private boolean usePayPassword;
    @SerializedName("group:create")
    private CircleGroup mCircleGroup;
    @SerializedName("group:reward")
    private GroupReward mGroupReward;

    private CurrencyConfig currency;

    private WalletConfigBean wallet;

    private int limit = 15;


    public WalletConfigBean getWallet() {

        if (wallet == null) {
            wallet = new WalletConfigBean();
        }
        return wallet;
    }

    public void setWallet(WalletConfigBean wallet) {
        this.wallet = wallet;
    }

    public CurrencyConfig getCurrency() {
        if (currency == null) {
            currency = new CurrencyConfig();
        }
        return currency;
    }

    public void setCurrency(CurrencyConfig currency) {
        this.currency = currency;
    }

    public QuestionConfig getQuestionConfig() {
        if (mQuestionConfig == null) {
            mQuestionConfig = new QuestionConfig();
        }
        return mQuestionConfig;
    }

    public void setQuestionConfig(QuestionConfig questionConfig) {
        mQuestionConfig = questionConfig;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isUsePayPassword() {
        return usePayPassword;
    }

    public void setUsePayPassword(boolean usePayPassword) {
        this.usePayPassword = usePayPassword;
    }

    public NewsConfig getNewsContribute() {
        if (mNewsContribute == null) {
            return new NewsConfig();
        }
        return mNewsContribute;
    }

    public void setNewsContribute(NewsConfig newsContribute) {
        mNewsContribute = newsContribute;
    }

    public Appversion getAppversion() {
        if (mAppversion == null) {
            mAppversion = new Appversion();
        }
        return mAppversion;
    }

    public void setAppversion(Appversion appversion) {
        mAppversion = appversion;
    }

    public Feed getFeed() {
        if (mFeed == null) {
            return new Feed();
        }
        return mFeed;
    }

    public void setFeed(Feed feed) {
        mFeed = feed;
    }

    public CircleGroup getCircleGroup() {
        if (mCircleGroup == null) {
            return new CircleGroup();
        }
        return mCircleGroup;
    }

    public GroupReward getGroupReward() {
        if (mGroupReward == null) {
            return new GroupReward();
        }
        return mGroupReward;
    }

    public String getIm_serve() {
        return im_serve;
    }

    public void setIm_serve(String im_serve) {
        this.im_serve = im_serve;
    }

    /**
     * 新版的小助手数据放入原有的数据模型
     *
     * @return
     */
    public ArrayList<ImHelperBean> getIm_helper() {
        ArrayList<ImHelperBean> imHelperBeans = new ArrayList<>();
        if (!TextUtils.isEmpty(newImHelper)) {
            ImHelperBean imHelperBean = new ImHelperBean();
            imHelperBean.setUid(newImHelper);
            imHelperBean.setUrl(ApiConfig.APP_DOMAIN + ApiConfig.URL_ABOUT_US);
            imHelperBeans.add(imHelperBean);
        }
        return imHelperBeans;
    }


    public String getNewImHelper() {
        return newImHelper;
    }

    public void setNewImHelper(String newImHelper) {
        this.newImHelper = newImHelper;
    }

    public ArrayList<Advert> getAdverts() {
        return mAdverts;
    }

    public void setAdverts(ArrayList<Advert> adverts) {
        mAdverts = adverts;
    }

    public CheckIn getCheckin() {
        if (checkin == null) {
            checkin = new CheckIn();
        }
        return checkin;
    }

    public void setCheckin(CheckIn checkin) {
        this.checkin = checkin;
    }

    public RegisterSettingsBean getRegisterSettings() {
        if (registerSettings == null) {
            registerSettings = new RegisterSettingsBean();
        }
        return registerSettings;
    }

    public void setRegisterSettings(RegisterSettingsBean registerSettings) {
        this.registerSettings = registerSettings;
    }

    public SiteBean getSite() {
        if (site == null) {
            return new SiteBean();
        }
        return site;
    }

    public void setSite(SiteBean site) {
        this.site = site;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    /**
     * uid : 1
     * url : https://plus.io/users/1
     */

    public static class ImHelperBean implements Serializable {
        private static final long serialVersionUID = 2932201693891980990L;
        private String uid;
        private String url;
        private boolean isDelete;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isDelete() {
            return isDelete;
        }

        public void setDelete(boolean delete) {
            isDelete = delete;
        }

        @Override
        public String toString() {
            return "ImHelperBean{" +
                    "uid='" + uid + '\'' +
                    ", url='" + url + '\'' +
                    ", isDelete=" + isDelete +
                    '}';
        }
    }

    /**
     * {
     * "id":1,
     * "title":"广告1",
     * "type":"image",
     * "data":{
     * "image":"https://avatars0.githubusercontent.com/u/5564821?v=3&s=460",
     * "link":"https://github.com/zhiyicx/thinksns-plus"
     * }
     */
    public static class Advert implements Serializable {
        private static final long serialVersionUID = -261781358771084800L;
        private int id;
        private String title;
        private String type;
        private Object data;
        private ImageAdvert mImageAdvert;

        public ImageAdvert getImageAdvert() {
            return mImageAdvert;
        }

        public void setImageAdvert(ImageAdvert imageAdvert) {
            mImageAdvert = imageAdvert;
        }

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }


    public static class RegisterSettingsBean implements Serializable {
        private static final long serialVersionUID = 9161708076383134140L;
        /**
         * showTerms : false
         * registerMode : all
         * completeData : true
         * accountType : all
         * content : # 服务条款及隐私政策
         */

        private boolean showTerms;
        private String method;// 注册类型
        private String type;// 注册方式
        private String rules;
        private String fixed;// 是否完善资料
        private boolean completeData;
        private String content;// 用户协议

        public String getFixed() {
            return fixed;
        }

        public void setFixed(String fixed) {
            this.fixed = fixed;
        }

        public String getRules() {
            return rules;
        }

        public void setRules(String rules) {
            this.rules = rules;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean hasShowTerms() {
            return showTerms;
        }

        public void setShowTerms(boolean showTerms) {
            this.showTerms = showTerms;
        }

        public boolean isCompleteData() {
            return completeData;
        }

        public void setCompleteData(boolean completeData) {
            this.completeData = completeData;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class SiteBean implements Serializable {
        private static final long serialVersionUID = -5372367518165433839L;
        /**
         * status : true
         * off_reason : 站点维护中请稍后再访问
         * app : {"status":true}
         * h5 : {"status":true}
         * reserved_nickname : root,admin
         * about_url : 关于我们
         * client_email : admin@123.com
         * gold : {"status":true}
         * reward : {"status":true}
         * anonymous : 回答匿名规则
         * user_invite_template : 我发现了一个全平台社交系统ThinkSNS+，快来加入吧：http://t.cn/RpFfbbi
         * gold_name : {"name":"金币","unit":"枚"}
         */
        private String reserved_nickname = "root,admin"; // 保留的用户名
        private String client_email; // 保留的邮箱
        private String user_invite_template; // 邀请注册的短信模板
        private boolean status;
        private String off_reason;
        private String about_url;
        private AppBean app;
        private H5Bean h5;
        private GoldBean gold;
        private RewardBean reward;
        @SerializedName("currency_name")
        private GoldNameBean gold_name;
        private Anonymous anonymous;

        public Anonymous getAnonymous() {
            if (anonymous == null) {
                return new Anonymous();
            }
            return anonymous;
        }

        public void setAnonymous(Anonymous anonymous) {
            this.anonymous = anonymous;
        }

        public String getAbout_url() {
            return about_url;
        }

        public void setAbout_url(String about_url) {
            this.about_url = about_url;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getOff_reason() {
            return off_reason;
        }

        public void setOff_reason(String off_reason) {
            this.off_reason = off_reason;
        }

        public AppBean getApp() {
            if (app == null) {
                return new AppBean();
            }
            return app;
        }

        public void setApp(AppBean app) {
            this.app = app;
        }

        public H5Bean getH5() {
            if (h5 == null) {
                return new H5Bean();
            }
            return h5;
        }

        public void setH5(H5Bean h5) {
            this.h5 = h5;
        }

        public String getReserved_nickname() {
            if (reserved_nickname == null) {
                return "";
            }
            return reserved_nickname;
        }

        public void setReserved_nickname(String reserved_nickname) {
            this.reserved_nickname = reserved_nickname;
        }

        public String getClient_email() {
            if (client_email == null) {
                return "";
            }
            return client_email;
        }

        public void setClient_email(String client_email) {
            this.client_email = client_email;
        }

        public GoldBean getGold() {
            if (gold == null) {
                return new GoldBean();
            }
            return gold;
        }

        public void setGold(GoldBean gold) {
            this.gold = gold;
        }

        public RewardBean getReward() {
            if (reward == null) {
                return new RewardBean();
            }
            return reward;
        }

        public void setReward(RewardBean reward) {
            this.reward = reward;
        }

        public String getUser_invite_template() {
            return user_invite_template;
        }

        public void setUser_invite_template(String user_invite_template) {
            this.user_invite_template = user_invite_template;
        }

        public GoldNameBean getGold_name() {
            if (gold_name == null) {
                return new GoldNameBean();
            }
            return gold_name;
        }

        public void setGold_name(GoldNameBean gold_name) {
            this.gold_name = gold_name;
        }

        public static class AppBean implements Serializable {
            private static final long serialVersionUID = -2995455975855810530L;
            /**
             * status : true
             */

            private boolean status;

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }

            @Override
            public String toString() {
                return "AppBean{" +
                        "status=" + status +
                        '}';
            }
        }

        public static class H5Bean implements Serializable {
            private static final long serialVersionUID = -4543706356197674563L;
            /**
             * status : true
             */

            private boolean status;

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }

            @Override
            public String toString() {
                return "H5Bean{" +
                        "status=" + status +
                        '}';
            }
        }

        public static class GoldBean implements Serializable {
            private static final long serialVersionUID = 175809906769541494L;
            /**
             * status : true
             */
            private String name;
            private boolean status;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }

            @Override
            public String toString() {
                return "GoldBean{" +
                        "status=" + status +
                        '}';
            }
        }

        public static class RewardBean implements Serializable {
            private static final long serialVersionUID = 8123957693947760523L;
            /**
             * status : true
             */

            private boolean status = true;

            private String amounts = "100,500,1000";

            public boolean hasStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }

            public String getAmounts() {
                return amounts;
            }

            public void setAmounts(String amounts) {
                this.amounts = amounts;
            }

            @Override
            public String toString() {
                return "RewardBean{" +
                        "status=" + status +
                        ", amounts='" + amounts + '\'' +
                        '}';
            }
        }

        public static class GoldNameBean implements Serializable {
            private static final long serialVersionUID = 1993148057604147543L;
            /**
             * name : 金币
             * unit : 枚
             */

            private String name = "积分";
            private String unit = "个";

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            @Override
            public String toString() {
                return super.toString();
            }
        }

        @Override
        public String toString() {
            return "SiteBean{" +
                    "reserved_nickname='" + reserved_nickname + '\'' +
                    ", client_email='" + client_email + '\'' +
                    ", user_invite_template='" + user_invite_template + '\'' +
                    ", status=" + status +
                    ", off_reason='" + off_reason + '\'' +
                    ", app=" + app +
                    ", h5=" + h5 +
                    ", gold=" + gold +
                    ", reward=" + reward +
                    ", gold_name=" + gold_name +
                    '}';
        }
    }

    public static class NewsConfig implements Serializable {

        private static final long serialVersionUID = 7843543063937840121L;

        /**
         * 开关
         */
        private NewsConfigContribute contribute;
        /**
         * 资讯投稿金额
         */
        private int pay_contribute = 1;

        public NewsConfigContribute getContribute() {
            return contribute;
        }

        public void setContribute(NewsConfigContribute contribute) {
            this.contribute = contribute;
        }

        public int getPay_contribute() {
            return pay_contribute;
        }

        public void setPay_contribute(int pay_contribute) {
            this.pay_contribute = pay_contribute;
        }

        public static class NewsConfigContribute implements Serializable {

            private static final long serialVersionUID = 7843543063937840122L;
            /**
             * 投稿是否需要认证
             */
            private boolean verified = true;
            /**
             * 投稿是否需要付费
             */
            private boolean pay = true;

            public boolean hasVerified() {
                return verified;
            }

            public void setVerified(boolean verified) {
                this.verified = verified;
            }

            public boolean hasPay() {
                return pay;
            }

            public void setPay(boolean pay) {
                this.pay = pay;
            }

        }
    }

    public static class Appversion implements Serializable {
        private static final long serialVersionUID = 8919908013556136434L;
        private boolean open;

        public boolean hasOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }
    }

    public static class Feed implements Serializable {

        private static final long serialVersionUID = 2393545893640479534L;
        private boolean reward = true;
        private boolean paycontrol = false;
        private String[] items;
        private int limit = 50;

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public String[] getItems() {
            return items;
        }

        public void setItems(String[] items) {
            this.items = items;
        }

        public boolean hasReward() {
            return reward;
        }

        public void setReward(boolean reward) {
            this.reward = reward;
        }

        public boolean hasPaycontrol() {
            return paycontrol;
        }

        public void setPaycontrol(boolean paycontrol) {
            this.paycontrol = paycontrol;
        }

        @Override
        public String toString() {
            return "Feed{" +
                    "reward=" + reward +
                    ", paycontrol=" + paycontrol +
                    '}';
        }
    }

    public static class CircleGroup implements Serializable {

        private static final long serialVersionUID = 2393545893640479535L;
        private boolean need_verified;

        public boolean isNeed_verified() {
            return need_verified;
        }

        public void setNeed_verified(boolean need_verified) {
            this.need_verified = need_verified;
        }

        @Override
        public String toString() {
            return "CircleGroup{" +
                    "need_verified=" + need_verified +
                    '}';
        }
    }
    public static class GroupReward implements Serializable {

        private static final long serialVersionUID = 239354589361047635L;
        @SerializedName("status")
        private boolean open;

        public boolean isOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }
    }
    public static class CurrencyConfig implements Serializable {

        private static final long serialVersionUID = 3393545893640479536L;
        private OpenConfig cash;
        private OpenConfig recharge;
        private String rule;
        private IntegrationConfigBean settings;

        public OpenConfig getCash() {
            if (cash == null) {
                cash = new OpenConfig();
            }
            return cash;
        }

        public void setCash(OpenConfig cash) {
            this.cash = cash;
        }

        public OpenConfig getRecharge() {
            if (recharge == null) {
                recharge = new OpenConfig();
            }
            return recharge;
        }

        public void setRecharge(OpenConfig recharge) {
            this.recharge = recharge;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        public IntegrationConfigBean getSettings() {
            if (settings == null) {
                settings = new IntegrationConfigBean();
            }
            return settings;
        }

        public void setSettings(IntegrationConfigBean settings) {
            this.settings = settings;
        }
    }

    public static class IntegrationConfigBean implements Serializable {

        private static final long serialVersionUID = -1293476228114357409L;
        /**
         * recharge-ratio	int	兑换比例，人民币一分钱可兑换的积分数量
         * recharge-options	string	充值选项，人民币分单位
         * recharge-max	int	单笔最高充值额度 v
         * recharge-min	int	单笔最小充值额度
         * rule	string	积分规则
         * cash-min
         * cash-max
         */

        @SerializedName("recharge-ratio")
        private int rechargeratio = 1;
        @SerializedName("recharge-options")
        private String rechargeoptions;
        @SerializedName("recharge-max")
        private long rechargemax;
        @SerializedName("recharge-min")
        private int rechargemin;
        @SerializedName("cash-max")
        private long cashmax;
        @SerializedName("cash-min")
        private int cashmin;


        public int getRechargeratio() {
            return rechargeratio;
        }

        public void setRechargeratio(int rechargeratio) {
            this.rechargeratio = rechargeratio;
        }

        public String getRechargeoptions() {
            return rechargeoptions;
        }

        public void setRechargeoptions(String rechargeoptions) {
            this.rechargeoptions = rechargeoptions;
        }

        public long getRechargemax() {
            return rechargemax;
        }

        public void setRechargemax(long rechargemax) {
            this.rechargemax = rechargemax;
        }

        public int getRechargemin() {
            return rechargemin;
        }

        public void setRechargemin(int rechargemin) {
            this.rechargemin = rechargemin;
        }


        public long getCashmax() {
            return cashmax;
        }

        public void setCashmax(long cashmax) {
            this.cashmax = cashmax;
        }

        public int getCashmin() {
            return cashmin;
        }

        public void setCashmin(int cashmin) {
            this.cashmin = cashmin;
        }
    }

    public static class OpenConfig implements Serializable {

        private static final long serialVersionUID = 3393545893640479534L;
        @SerializedName(value = "open", alternate = {"status"})
        private boolean open = true;
        private String rule = "";

        public boolean isOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        @Override
        public String toString() {
            return "Open{" +
                    "open=" + open +
                    '}';
        }
    }

    public static class Anonymous implements Serializable {
        private static final long serialVersionUID = 339354589340479534L;
        private boolean status = true;
        private String rule;

        public boolean getStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }
    }

    public static class CheckIn implements Serializable {
        private static final long serialVersionUID = 339354589340479532L;
        /**
         * 签到是否开启
         */
        @SerializedName("switch")
        private boolean status = false;
        /**
         * 签到获取的金币
         */
        private int balance = 1;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }
    }

    public static class QuestionConfig implements Serializable {
        private static final long serialVersionUID = -6819416425568712274L;
        /**
         * switch 接口开关
         * apply_amount : 200 对应配置信息的    "question:apply_amount": 200,  //  申请精选所需支付金额
         * onlookers_amount : 100 对应配置信息的    "question:onlookers_amount": 100  //  围观答案所需支付金额
         * anonymity_rule : 匿名规则12321
         * reward_rule 悬赏规则
         */
        @SerializedName("switch")
        private boolean status = true;
        private int apply_amount = 1;
        private int onlookers_amount = 1;
        private String anonymity_rule = "";
        private String reward_rule = "";

        public int getApply_amount() {
            return apply_amount;
        }

        public void setApply_amount(int apply_amount) {
            this.apply_amount = apply_amount;
        }

        public int getOnlookers_amount() {
            return onlookers_amount;
        }

        public void setOnlookers_amount(int onlookers_amount) {
            this.onlookers_amount = onlookers_amount;
        }

        public String getAnonymity_rule() {
            return anonymity_rule;
        }

        public void setAnonymity_rule(String anonymity_rule) {
            this.anonymity_rule = anonymity_rule;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getReward_rule() {
            return reward_rule;
        }

        public void setReward_rule(String reward_rule) {
            this.reward_rule = reward_rule;
        }
    }

    public static class WalletConfigBean implements Serializable, Parcelable {

        public static final String TYPE_ALIPAY = "alipay";
        public static final String TYPE_WECHAT = "wechat";
        private static final long serialVersionUID = 2408871831852484954L;
        /**
         * labels : [550,2000,9900]
         * ratio : 200
         * rule : 我是积分规则纯文本.
         * case_min_amount : 1, // 真实金额分单位，用户最低提现金额。
         * alipay : {"open":false}
         * apple : {"open":false}
         * wechat : {"open":false}
         * cash : {"types":["alipay"]}
         */
        private List<Float> labels = new ArrayList<>();
        private int ratio = 100;
        private String rule;
        @SerializedName("transform-currency")
        private boolean walletTransform = true; // 余额转积分
        private WalletCashOrRechargeConfigBean cash;
        private WalletCashOrRechargeConfigBean recharge;

        public List<Float> getLabels() {
            return labels;
        }

        public void setLabels(List<Float> labels) {
            this.labels = labels;
        }

        public int getRatio() {
            return ratio;
        }

        public void setRatio(int ratio) {
            this.ratio = ratio;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        public boolean isWalletTransform() {
            return walletTransform;
        }

        public void setWalletTransform(boolean walletTransform) {
            this.walletTransform = walletTransform;
        }

        public WalletCashOrRechargeConfigBean getCash() {
            if (cash == null) {
                cash = new WalletCashOrRechargeConfigBean();
            }
            return cash;
        }

        public void setCash(WalletCashOrRechargeConfigBean cash) {
            this.cash = cash;
        }

        public WalletCashOrRechargeConfigBean getRecharge() {
            if (recharge == null) {
                recharge = new WalletCashOrRechargeConfigBean();
            }
            return recharge;
        }

        public void setRecharge(WalletCashOrRechargeConfigBean recharge) {
            this.recharge = recharge;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(this.labels);
            dest.writeInt(this.ratio);
            dest.writeString(this.rule);
            dest.writeByte(this.walletTransform ? (byte) 1 : (byte) 0);
            dest.writeSerializable(this.cash);
            dest.writeSerializable(this.recharge);
        }

        public WalletConfigBean() {
        }

        protected WalletConfigBean(Parcel in) {
            this.labels = new ArrayList<Float>();
            in.readList(this.labels, Float.class.getClassLoader());
            this.ratio = in.readInt();
            this.rule = in.readString();
            this.walletTransform = in.readByte() != 0;
            this.cash = (WalletCashOrRechargeConfigBean) in.readSerializable();
            this.recharge = (WalletCashOrRechargeConfigBean) in.readSerializable();
        }

        public static final Parcelable.Creator<WalletConfigBean> CREATOR = new Parcelable.Creator<WalletConfigBean>() {
            @Override
            public WalletConfigBean createFromParcel(Parcel source) {
                return new WalletConfigBean(source);
            }

            @Override
            public WalletConfigBean[] newArray(int size) {
                return new WalletConfigBean[size];
            }
        };
    }

    public static class WalletCashOrRechargeConfigBean implements Serializable {
        private static final long serialVersionUID = 2408871831852484924L;
        private String[] types;
        @SerializedName("min-amount")
        private int minAmount = 1;

        public String[] getTypes() {
            return types;
        }

        public void setTypes(String[] types) {
            this.types = types;
        }

        public int getMinAmount() {
            return minAmount;
        }

        public void setMinAmount(int minAmount) {
            this.minAmount = minAmount;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        private boolean status;


    }
}

