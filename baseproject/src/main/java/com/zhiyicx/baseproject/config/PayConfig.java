package com.zhiyicx.baseproject.config;

import java.text.NumberFormat;

/**
 * @Describe the config for pay
 * @Author Jungle68
 * @Date 2017/5/15
 * @Contact master.jungle68@gmail.com
 */
public class PayConfig {

    /**
     * 和用户余额相关的信息，余额单位都是按照 CNY 的「分」作为单位。
     * 接口不给现实数字，而是给出以 CNY 的「分」单位的数字，并在「启动者」中提供了显示转换比例，单位为百分比（%）
     */

    /**
     * 1.钱包余额充值
     * 用户充值金额，单位为真实货币「分」单位
     * 2.提现
     * 用户输入的余额(游戏币)值按照「转换比例」转换后再转化为「分」单位 分，按照比例转换单位后乘100以保证没有小数出现
     */

    public static final String WX_APP_ID = "wx324324324";

    /**
     * 人民币以分为单位，* 100  后做计算防止浮点数丢失，处理完再除以 100
     */
    public static final double MONEY_UNIT = 100;

    /**
     * 转换率单位 %，这个是针对 元 单位的
     */
    public static final double RATIO_UNIT = 100;

    /**
     * @param d     游戏币
     * @param ratio 转换率，是个百分数：500  -> 500%
     * @return 真实货币元单位 再乘以 MONEY_UNIT 得到 分单位
     */
    public static double gameCurrency2RealCurrency(double d, int ratio) {
        return (RATIO_UNIT * d / ratio) * MONEY_UNIT;
    }

    /**
     * @param d     真实货币 分单位
     * @param ratio 转换率，是个百分数：500  -> 500%
     * @return 与真实货币 分单位对应的 游戏币
     */
    public static double realCurrency2GameCurrency(double d, int ratio) {
        // 转换比例是针对  元 的，所有要先把分 转成 元
        return realCurrency2GameCurrency(d, ratio, false);
    }

    public static String realCurrency2GameCurrencyStr(double d, int ratio) {
        // 转换比例是针对  元 的，所有要先把分 转成 元
        double result = realCurrency2GameCurrency(d, ratio, false);
        String value = null;
        NumberFormat nf = NumberFormat.getInstance();
        // 设置此格式中不使用分组
        nf.setGroupingUsed(false);
        // 设置数的小数部分所允许的最大位数。
        nf.setMaximumFractionDigits(0);
        value = nf.format(result);
        return value;
    }

    /**
     * @param d            真实货币 分单位
     * @param ratio        转换率，是个百分数：500  -> 500%
     * @param isNeedChange 是否转换
     * @return 与真实货币 分单位对应的 游戏币
     */
    public static double realCurrency2GameCurrency(double d, int ratio, boolean isNeedChange) {
        // 转换比例是针对  元 的，所有要先把分 转成 元
        if (isNeedChange) {
            return (realCurrencyFen2Yuan(d) * ratio / RATIO_UNIT);
        } else {
            return d;
        }
    }

    /**
     * @param d 真实货币 元单位
     * @return 真实货币 分单位
     */
    public static double realCurrencyYuan2Fen(double d) {
        return d * MONEY_UNIT;
    }

    /**
     * @param d 真实货币 分单位
     * @return 真实货币 元单位
     */
    public static double realCurrencyFen2Yuan(double d) {
        return d / MONEY_UNIT;
    }

}
