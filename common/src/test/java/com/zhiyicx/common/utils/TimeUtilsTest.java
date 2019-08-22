package com.zhiyicx.common.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/12
 * @Contact master.jungle68@gmail.com
 */
public class TimeUtilsTest {
    /**
     * 通用
     * 一分钟内显示1分钟
     * 一小时内显示几分钟前，
     * 一天内显示几小时前，
     * 1天到2天显示昨天，
     * 2天到9天显示几天前，
     * 9天以上显示月日如（05-21）
     *
     * @throws Exception
     */
    @Test
    public void testGetTimeFriendlyNormal() throws Exception {
        long now = System.currentTimeMillis()/1000;//转换s
        System.out.println("分钟前 ："+TimeUtils.getTimeFriendlyNormal(TimeUtils.millis2String(now)));
        Assert.assertTrue(TimeUtils.getTimeFriendlyNormal(TimeUtils.millis2String(now)).contains("分钟前"));
        System.out.println("分钟前 ："+TimeUtils.getTimeFriendlyNormal(TimeUtils.millis2String(now-3590)));
        Assert.assertTrue(TimeUtils.getTimeFriendlyNormal(TimeUtils.millis2String(now-3590)).contains("分钟前"));
        System.out.println("小时前 ："+TimeUtils.getTimeFriendlyNormal(TimeUtils.millis2String(now-3610)));
        Assert.assertTrue(TimeUtils.getTimeFriendlyNormal(TimeUtils.millis2String(now-3610)).contains("小时前"));
        System.out.println("昨天 ："+TimeUtils.getTimeFriendlyNormal(TimeUtils.millis2String(now-(3600*23))));
        Assert.assertTrue(TimeUtils.getTimeFriendlyNormal(TimeUtils.millis2String(now-(3600*23))).contains("昨天"));
        System.out.println("天前 ："+TimeUtils.getTimeFriendlyNormal(TimeUtils.millis2String(now-(3600*24*6))));
        Assert.assertTrue(TimeUtils.getTimeFriendlyNormal(TimeUtils.millis2String(now-3600*24*6)).contains("天前"));
        System.out.println("月日 ："+TimeUtils.getTimeFriendlyNormal(TimeUtils.millis2String(now-(3600*24*10))));
        Assert.assertTrue(TimeUtils.getTimeFriendlyNormal(TimeUtils.millis2String(now-(3600*24*10))).contains("-"));
    }

    /**
     *  详情页
     *  一分钟内显示1分钟
     *  一小时内显示几分钟前，
     *  一天内显示几小时前，
     *  1天到2天显示如（昨天 20:36），
     *  2天到9天显示如（五天前 20：34），
     *  9天以上显示如（02-28 19:15）
     *
     * @throws Exception
     */
    @Test
    public void testGetTimeFriendlyForDetail() throws Exception {
        long now = System.currentTimeMillis()/1000;//转换s
        System.out.println("分钟前 ："+TimeUtils.getTimeFriendlyForDetail(TimeUtils.millis2String(now)));
        Assert.assertTrue(TimeUtils.getTimeFriendlyForDetail(TimeUtils.millis2String(now)).contains("分钟前"));
        System.out.println("分钟前 ："+TimeUtils.getTimeFriendlyForDetail(TimeUtils.millis2String(now-3590)));
        Assert.assertTrue(TimeUtils.getTimeFriendlyForDetail(TimeUtils.millis2String(now-3590)).contains("分钟前"));
        System.out.println("小时前 ："+TimeUtils.getTimeFriendlyForDetail(TimeUtils.millis2String(now-36100)));
        Assert.assertTrue(TimeUtils.getTimeFriendlyForDetail(TimeUtils.millis2String(now-3610)).contains("小时前"));
        System.out.println("昨天 ："+TimeUtils.getTimeFriendlyForDetail(TimeUtils.millis2String(now-(3600*23))));
        Assert.assertTrue(TimeUtils.getTimeFriendlyForDetail(TimeUtils.millis2String(now-(3600*23))).contains("昨天 "));
        System.out.println("天前 ："+TimeUtils.getTimeFriendlyForDetail(TimeUtils.millis2String(now-(3600*24*6))));
        Assert.assertTrue(TimeUtils.getTimeFriendlyForDetail(TimeUtils.millis2String(now-3600*24*6)).contains("天前 "));
        System.out.println("月日 ："+TimeUtils.getTimeFriendlyForDetail(TimeUtils.millis2String(now-(3600*24*10))));
        Assert.assertTrue(TimeUtils.getTimeFriendlyForDetail(TimeUtils.millis2String(now-(3600*24*10))).contains(":"));
    }

    /**
     *  个人主页
     *  1天内显示今天，
     *  1天到2天显示昨天，
     *  2天以上显示月日如（05-21）
     *
     * @throws Exception
     */
    @Test
    public void testGetTimeFriendlyForUserHome() throws Exception {
        long now = System.currentTimeMillis()/1000;//转换s
        System.out.println("分钟前 ："+TimeUtils.getTimeFriendlyForUserHome(TimeUtils.millis2String(now)));
        Assert.assertTrue(TimeUtils.getTimeFriendlyForUserHome(TimeUtils.millis2String(now)).equals("今天"));
        System.out.println("分钟前 ："+TimeUtils.getTimeFriendlyForUserHome(TimeUtils.millis2String(now-3590)));
        Assert.assertTrue(TimeUtils.getTimeFriendlyForUserHome(TimeUtils.millis2String(now-3590)).equals("今天"));
        System.out.println("小时前 ："+TimeUtils.getTimeFriendlyForUserHome(TimeUtils.millis2String(now-3610)));
        Assert.assertTrue(TimeUtils.getTimeFriendlyForUserHome(TimeUtils.millis2String(now-3610)).equals("今天"));
        System.out.println("昨天 ："+TimeUtils.getTimeFriendlyForUserHome(TimeUtils.millis2String(now-(3600*23))));
        Assert.assertTrue(TimeUtils.getTimeFriendlyForUserHome(TimeUtils.millis2String(now-(3600*23))).contains("昨天"));
        System.out.println("天前 ："+TimeUtils.getTimeFriendlyForUserHome(TimeUtils.millis2String(now-(3600*24*6))));
        Assert.assertTrue(TimeUtils.getTimeFriendlyForUserHome(TimeUtils.millis2String(now-3600*24*6)).contains("-"));
        System.out.println("月日 ："+TimeUtils.getTimeFriendlyForUserHome(TimeUtils.millis2String(now-(3600*24*10))));
        Assert.assertTrue(TimeUtils.getTimeFriendlyForUserHome(TimeUtils.millis2String(now-(3600*24*10))).contains("-"));
    }
}