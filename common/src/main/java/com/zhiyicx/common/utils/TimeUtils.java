package com.zhiyicx.common.utils;

import android.support.annotation.Nullable;

import com.zhiyicx.common.config.ConstantConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @Describe 时间格式化工具类 ,api接口里 的时间返回 统一用时间空间字符串格式 UTC+ 0 时区 例如; 2017-03-01 01:28:33
 * @Author Jungle68
 * @Date 2017/1/12
 * @Contact master.jungle68@gmail.com
 */

public class TimeUtils {

    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";


    /**
     * 将时间戳转为时间字符串，转换默认时区
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param millis 毫秒时间戳
     * @return 时间字符串
     */
    public static String millis2String(long millis) {
        return new SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault()).format(new Date(millis));
    }

    /**
     * 动态列表 \评论列表 时间戳格式转换
     * 一分钟内显示一分钟
     * 一小时内显示几分钟前
     * 一天内显示几小时前
     * 1天到2天显示昨天
     * 2天到9天显示几天前
     * 9天以上显示月日如（05-21）
     *
     * @param timestr 时间 例如; 2017-03-01 01:28:33
     * @return 友好时间字符串
     */
    public static String getTimeFriendlyNormal(String timestr) {
        String result = "1分钟内";
        long timesamp = utc2LocalLong(timestr);
        switch (getifferenceDays(timesamp)) {
            case 0:
                result = getFriendlyTimeForBeforHour(timesamp, result);
                break;
            case 1:
                result = "昨天";
                break;
            case 2:
                result = "2天前";
                break;
            case 3:
                result = "3天前";
                break;
            case 4:
                result = "4天前";
                break;
            case 5:
                result = "5天前";
                break;
            case 6:
                result = "6天前";
                break;
            case 7:
                result = "7天前";
                break;
            case 8:
                result = "8天前";
                break;
            case 9:
                result = "9天前";
                break;

            default:
                result = getStandardTimeWithMothAndDay(timesamp);
                break;
        }
        return result;
    }

    /**
     * 动态列表 \评论列表 时间戳格式转换
     * 一分钟内显示一分钟
     * 一小时内显示几分钟前
     * 一天内显示几小时前
     * 1天到2天显示昨天
     * 2天到9天显示几天前
     * 9天以上显示月日如（05-21）
     *
     * @param timesamp 时间 例如; 2017-03-01 01:28:33
     * @return 友好时间字符串
     */
    public static String getTimeFriendlyNormal(long timesamp) {
        String result = "1分钟内";
        switch (getifferenceDays(timesamp)) {
            case 0:
                result = getFriendlyTimeForBeforHour(timesamp, result);
                break;
            case 1:
                result = "昨天";
                break;
            case 2:
                result = "2天前";
                break;
            case 3:
                result = "3天前";
                break;
            case 4:
                result = "4天前";
                break;
            case 5:
                result = "5天前";
                break;
            case 6:
                result = "6天前";
                break;
            case 7:
                result = "7天前";
                break;
            case 8:
                result = "8天前";
                break;
            case 9:
                result = "9天前";
                break;

            default:
                result = getStandardTimeWithMothAndDay(timesamp);
                break;
        }
        return result;
    }


    /**
     * 聊天详情页 备注：聊天时间显示间隔6分钟
     * <p>
     * 一分钟内显示一分钟内
     * 一小时内显示几分钟前，
     * 一天内显示几小时前，
     * 1天到2天显示如（昨天 20:36），
     * 2天到9天显示如（5天前 20：34），
     * 9天以上显示如（02-28 19:15）
     *
     * @param timesamp 时间戳 单位 ms
     * @return 友好时间字符串
     */
    public static String getTimeFriendlyForChat(long timesamp) {
        return handleDetailTime(timesamp);
    }


    /**
     * 详情页(动态详情页、聊天详情页) 备注：聊天时间显示间隔6分钟
     * <p>
     * 一分钟内显示一分钟内
     * 一小时内显示几分钟前，
     * 一天内显示几小时前，
     * 1天到2天显示如（昨天 20:36），
     * 2天到9天显示如（5天前 20：34），
     * 9天以上显示如（02-28 19:15）
     *
     * @param timestr 时间 例如; 2017-03-01 01:28:33
     * @return 友好时间字符串
     */
    public static String getTimeFriendlyForDetail(String timestr) {
        long timesamp = utc2LocalLong(timestr);
        return handleDetailTime(timesamp);
    }

    /**
     * 个人主页
     * 1天内显示今天，
     * 1天到2天显示昨天，
     * 2天以上显示月日如（05-21）
     *
     * @param timestr 时间 例如; 2017-03-01 01:28:33
     * @return 友好的时间字符串
     */
    public static String getTimeFriendlyForUserHome(String timestr) {
        String result = "1分钟内";
        long timesamp = utc2LocalLong(timestr);
        switch (getifferenceDays(timesamp)) {
            case 0:
                result = "今,天";
                break;
            case 1:
                result = "昨,天";
                break;

            default:
                result = getStandardTimeWithMothAndDayOne(timesamp);
                break;
        }
        return result;
    }

    /**
     * 一分钟内显示一分钟内
     * 一小时内显示几分钟前，
     * 一天内显示几小时前，
     * 1天到2天显示如（昨天 20:36），
     * 2天到9天显示如（五天前 20：34），
     * 9天以上显示如（02-28 19:15）
     *
     * @param timesamp
     * @return
     */
    private static String handleDetailTime(long timesamp) {
        String result = "1分钟内";
        switch (getifferenceDays(timesamp)) {
            case 0:
                result = getFriendlyTimeForBeforHour(timesamp, result);
                break;
            case 1:
                result = "昨天 " + getStandardTimeWithHour(timesamp);
                break;
            case 2:
                result = "2天前 " + getStandardTimeWithHour(timesamp);
                break;
            case 3:
                result = "3天前 " + getStandardTimeWithHour(timesamp);
                break;
            case 4:
                result = "4天前 " + getStandardTimeWithHour(timesamp);
                break;
            case 5:
                result = "5天前 " + getStandardTimeWithHour(timesamp);
                break;
            case 6:
                result = "6天前 " + getStandardTimeWithHour(timesamp);
                break;
            case 7:
                result = "7天前 " + getStandardTimeWithHour(timesamp);
                break;
            case 8:
                result = "8天前 " + getStandardTimeWithHour(timesamp);
                break;
            case 9:
                result = "9天前 " + getStandardTimeWithHour(timesamp);
                break;

            default:
                result = getStandardTimeWithDate(timesamp);
                break;
        }
        return result;
    }

    /**
     * 获取当前 0 时区的时间戳
     *
     * @return
     */
    public static long getCurrenZeroTime() {
        //1、取得本地时间：
        java.util.Calendar cal = java.util.Calendar.getInstance();
        //2、取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        //3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        //4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        //之后，您再通过调用cal.get(int x)或cal.getTimeInMillis()方法所取得的时间即是UTC标准时间。
        return cal.getTimeInMillis();
    }

    /**
     * 获取当前 0 时区的时间戳
     *
     * @return
     */
    public static String getCurrenZeroTimeStr() {

        return millis2String(getCurrenZeroTime());
    }


    /**
     * 输入时间和当前时间间隔的天数
     *
     * @param timesamp 输入时间
     * @return 输入时间和当前时间间隔的天数
     */
    public static int getifferenceDays(long timesamp) {

        long timeMillisSpace = System.currentTimeMillis() - timesamp;

        double daySpaceDouble = (timeMillisSpace / (double) (1000 * 60 * 60 * 24));

        long daySpace = TimeUnit.DAYS.convert(timeMillisSpace, TimeUnit.MILLISECONDS);

        if (daySpaceDouble > daySpace && daySpaceDouble < 9) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd");
            Date today = new Date(System.currentTimeMillis());
            Date otherDay = new Date(timesamp);
            int now = Integer.parseInt(sdf.format(today));
            int other = Integer.parseInt(sdf.format(otherDay));
            return now - other;
        } else {
            return (int) daySpace;
        }
    }


    /**
     * 获取几分钟前、和几小时前
     *
     * @param timesamp     时间戳
     * @param defaltString 默认文字
     * @return
     */
    @Nullable
    private static String getFriendlyTimeForBeforHour(long timesamp, String defaltString) {
        long curTime = System.currentTimeMillis();
        long time = curTime - timesamp;
        if (time < ConstantConfig.MIN) {
            return defaltString;
        } else if (time < ConstantConfig.HOUR) {
            return time / ConstantConfig.MIN + "分钟前";
        } else if (time < ConstantConfig.DAY) {
            return time / ConstantConfig.HOUR + "小时前";
        }
        return defaltString;
    }

    /**
     * 通过时间戳获取 yyyy
     *
     * @param timestamp
     * @return
     */
    public static int getYear(long timestamp) {
        return Integer.parseInt(getTime(timestamp, "yyyy"));
    }

    /**
     * 通过时间戳获取 yyyy-MM-dd HH:mm
     *
     * @param timestamp
     * @return
     */
    public static String getStandardTimeWithYeay(long timestamp) {
        return getTime(timestamp, "yyyy-MM-dd HH:mm");
    }

    /**
     * 通过时间戳获取 yyyy-MM-dd
     *
     * @param timestamp
     * @return
     */
    public static String getYeayMonthDay(long timestamp) {
        return getTime(timestamp, "yyyy-MM-dd");
    }

    /**
     * 通过时间戳获取 format类型时间
     *
     * @param timestamp ms
     * @param format    格式类型
     * @return
     */
    public static String getTime(long timestamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    /**
     * 通过时间戳获取 MM-dd HH:mm
     *
     * @param timestamp
     * @return
     */
    public static String getStandardTimeWithDate(long timestamp) {
        return getTime(timestamp, "MM-dd HH:mm");
    }

    /**
     * 通过时间戳获取 HH:mm
     */
    public static String getStandardTimeWithHour(long timestamp) {
        return getTime(timestamp, "HH:mm");
    }

    /**
     * 通过时间戳获取 MM-dd
     */
    public static String getStandardTimeWithMothAndDay(long timestamp) {
        return getTime(timestamp, "MM-dd");
    }

    /**
     * 通过时间戳获取 MM.dd
     */
    public static String getStandardTimeWithMothAndDayWithDot(long timestamp) {
        return getTime(timestamp, "MM.dd");
    }

    /**
     * 通过时间戳获取 dd,MM月
     */
    public static String getStandardTimeWithMothAndDayOne(long timestamp) {
        return getTime(timestamp, "dd,M 月");
    }

    /**
     * 通过时间戳获取 mm:ss
     */
    public static String getStandardTimeWithMinAndSec(long timestamp) {
        return getTime(timestamp, "mm:ss");
    }

    /**
     * 将时间字符串转为时间戳
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 毫秒时间戳
     */
    public static long string2MillisDefaultLocal(String time) {
        return string2MillisDefaultLocal(time, DEFAULT_PATTERN);
    }

    /**
     * 将时间字符串转为时间戳
     * <p>time格式为pattern</p>
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return 毫秒时间戳
     */
    public static long string2MillisDefaultLocal(String time, String pattern) {
        return string2Millis(time, pattern, Locale.getDefault());
    }

    /**
     * 将时间字符串转为时间戳
     * <p>time格式为pattern</p>
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return 毫秒时间戳
     */
    public static long string2Millis(String time, String pattern, Locale locale) {
        return string2MillisDefaultLocal(time, pattern, locale);
    }

    /**
     * 将时间字符串转为时间戳
     * <p>time格式为pattern</p>
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @param locale  时间域
     * @return 毫秒时间戳
     */
    public static long string2MillisDefaultLocal(String time, String pattern, Locale locale) {
        try {
            return new SimpleDateFormat(pattern, locale).parse(time).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取时间 对应 星期几
     *
     * @param timeStr
     * @return
     */
    public static String string2_ToDya_Yesterday_Week(String timeStr) {
        long time = utc2LocalLong(timeStr);
        Date otherDay = new Date(time);
        int intervalDays = Math.abs(getifferenceDays(time));
        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六", "今天", "昨天"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(otherDay);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        if (intervalDays <= 1) {
            return weeks[7 + intervalDays] + "\n" + getStandardTimeWithMothAndDayWithDot(time);
        }
        return weeks[week_index] + "\n" + getStandardTimeWithMothAndDayWithDot(time);
    }

    public static String string2_Dya_Week_Time(String timeStr) {
        long time = utc2LocalLong(timeStr);
        String yearMothDay = getYeayMonthDay(time);
        String hourMin = getStandardTimeWithHour(time);
        Date otherDay = new Date(time);
        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六", "今天", "昨天"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(otherDay);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return yearMothDay + "\b" + weeks[week_index] + "\b" + hourMin;
    }

    /**
     * 将Server传送的UTC时间转换为指定时区的时间
     */
    public static String utc2LocalStr(String utcTime) {
        SimpleDateFormat utcFormater = new SimpleDateFormat(DEFAULT_PATTERN);
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return millis2String(System.currentTimeMillis());
        }
        SimpleDateFormat localFormater = new SimpleDateFormat(DEFAULT_PATTERN);
        localFormater.setTimeZone(TimeZone.getDefault());
        return localFormater.format(gpsUTCDate.getTime());
    }

    /**
     * 将Server传送的UTC时间转换为指定时区的时间
     */
    public static long utc2LocalLong(String utcTime) {
        SimpleDateFormat utcFormater = new SimpleDateFormat(DEFAULT_PATTERN);
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (Exception e) {
            return utcStringt2DefaultString(utcTime);
        }
        SimpleDateFormat localFormater = new SimpleDateFormat(DEFAULT_PATTERN);
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return string2MillisDefaultLocal(localTime);
    }

    public static long utcStringt2DefaultString(String utcString) {
        try {
            SimpleDateFormat utcFormat = new SimpleDateFormat(DEFAULT_UTC_PATTERN);
            utcFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            SimpleDateFormat defaultFormat = new SimpleDateFormat(DEFAULT_PATTERN);
            Date date = utcFormat.parse(utcString);
            String localTime = defaultFormat.format(date.getTime());
            return string2MillisDefaultLocal(localTime);
        } catch (ParseException pe) {
            pe.printStackTrace();
            return System.currentTimeMillis();
        }
    }
}
