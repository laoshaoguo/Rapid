package com.zhiyicx.common.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import com.zhiyicx.common.config.MarkdownConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zhiyicx.common.config.ConstantConfig.REGEX_DATE;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_EMAIL;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_ID_CARD15;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_ID_CARD18;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_IP;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_MOBILE_EXACT;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_MOBILE_SIMPLE;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_NOT_NUMBER_START;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_TEL;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_URL;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_USERNAME;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_ZH;
import static com.zhiyicx.common.config.ConstantConfig.REGEX_ZH_;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/2
 *     desc  : 正则相关工具类
 * </pre>
 */
public class RegexUtils {

    private RegexUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * If u want more please visit http://toutiao.com/i6231678548520731137/
     */

    /**
     * 验证手机号（简单）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileSimple(CharSequence input) {
        return isMatch(REGEX_MOBILE_SIMPLE, input);
    }

    /**
     * 验证手机号（精确）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileExact(CharSequence input) {
        return isMatch(REGEX_MOBILE_EXACT, input);
    }

    /**
     * 验证电话号码
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isTel(CharSequence input) {
        return isMatch(REGEX_TEL, input);
    }

    /**
     * 验证身份证号码15位
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard15(CharSequence input) {
        return isMatch(REGEX_ID_CARD15, input);
    }

    /**
     * 验证身份证号码18位
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard18(CharSequence input) {
        return isMatch(REGEX_ID_CARD18, input);
    }

    /**
     * 验证邮箱
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isEmail(CharSequence input) {
        return isMatch(REGEX_EMAIL, input);
    }

    /**
     * 验证URL
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isURL(CharSequence input) {
        return isMatch(REGEX_URL, input);
    }

    /**
     * 验证汉字
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isZh(CharSequence input) {
        return isMatch(REGEX_ZH, input);
    }

    /**
     * 验证是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile(REGEX_ZH_);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static int getChineseCouns(String str) {
        String regEx = REGEX_ZH_;
        String term = str.replaceAll(regEx, "aa");
        return term.length() - str.length();
    }

    /**
     * 验证用户名
     * <p>不能以数字开通</p>
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isUsernameNoNumberStart(CharSequence input) {
        return isMatch(REGEX_NOT_NUMBER_START, input);
    }

    /**
     * 中文 1 ，除外 0.5
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isUsernameLength(CharSequence input, int minLength, int maxLength) {
        int chineseCount = getChineseCouns(input.toString());
        int totalCount = input.toString().length();

        float len = 2 * chineseCount + (totalCount - chineseCount);
        return len >= minLength && len <= maxLength;
    }

    /**
     * 验证用户名
     * <p>不能以数字开通</p>
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isUsername(CharSequence input) {
        return isMatch(REGEX_USERNAME, input);
    }

    /**
     * 验证yyyy-MM-dd格式的日期校验，已考虑平闰年
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isDate(CharSequence input) {
        return isMatch(REGEX_DATE, input);
    }

    /**
     * 验证IP地址
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIP(CharSequence input) {
        return isMatch(REGEX_IP, input);
    }

    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMatch(String regex, CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    /**
     * 获取正则匹配的部分
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return 正则匹配的部分
     */
    public static List<String> getMatches(String regex, CharSequence input) {
        if (input == null) {
            return null;
        }
        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    /**
     * 获取正则匹配分组
     *
     * @param input 要分组的字符串
     * @param regex 正则表达式
     * @return 正则匹配分组
     */
    public static String[] getSplits(String input, String regex) {
        if (input == null) {
            return null;
        }
        return input.split(regex);
    }

    /**
     * 替换正则匹配的第一部分
     *
     * @param input       要替换的字符串
     * @param regex       正则表达式
     * @param replacement 代替者
     * @return 替换正则匹配的第一部分
     */
    public static String getReplaceFirst(String input, String regex, String replacement) {
        if (input == null) {
            return null;
        }
        return Pattern.compile(regex).matcher(input).replaceFirst(replacement);
    }

    /**
     * 替换所有正则匹配的部分
     *
     * @param input       要替换的字符串
     * @param regex       正则表达式
     * @param replacement 代替者
     * @return 替换所有正则匹配的部分
     */
    public static String getReplaceAll(String input, String regex, String replacement) {
        if (input == null) {
            return null;
        }
        return Pattern.compile(regex).matcher(input).replaceAll(replacement);
    }

    public static String replaceImageId(String regex, String input) {
        try {
            Matcher matcher = Pattern.compile(regex).matcher(input);
            return matcher.replaceAll(MarkdownConfig.IMAGE_REPLACE);
        } catch (Exception e) {
            return input == null ? "" : input;
        }
    }

    public static String replaceAllLines(String input) {
        if (TextUtils.isEmpty(input)) {
            return input;
        }
        return input.replaceAll("\r|\n*", "");
    }

    public static String replaceImageIdAndNeedSpaceString(String regex, String input) {
        try {
            Matcher matcher = Pattern.compile(regex).matcher(input);
            return matcher.replaceAll(MarkdownConfig.IMAGE_REPLACE);
        } catch (Exception e) {
            return TextUtils.isEmpty(input) ? " " : input;
        }
    }

    /**
     * 内容分段
     *
     * @param targetStr
     * @return
     */
    public static List<String> cutStringByImgTag(String targetStr) {
        List<String> splitTextList = new ArrayList<>();
        Pattern pattern = Pattern.compile("@!\\[.*?]\\((\\d+)\\)");
        Matcher matcher1 = pattern.matcher(targetStr);
        int lastIndex = 0;
        while (matcher1.find()) {
            if (matcher1.start() > lastIndex) {
                // 文字
                String result1 = targetStr.substring(lastIndex, matcher1.start());
                splitTextList.add(result1);
            }
            // 图片
            String result2 = targetStr.substring(matcher1.start(), matcher1.end());
            splitTextList.add(result2);

            lastIndex = matcher1.end();
        }
        // 没有匹配
        if (lastIndex != targetStr.length()) {
            splitTextList.add(targetStr.substring(lastIndex, targetStr.length()));
        }
        // 最后添加标识符
        if (splitTextList.size() > 0) {
            String last = splitTextList.get(splitTextList.size() - 1);
            splitTextList.set(splitTextList.size() - 1, last + "tym_last");
        }
        return splitTextList;
    }

    public static List<String> cutStringByNetSite(String targetStr) {
        List<String> splitTextList = new ArrayList<>();
        Pattern pattern = Pattern.compile("((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2," +
                "6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*" +
                "(/[a-zA-Z0-9\\&%_\\./-~-]*)?");
        Matcher matcher1 = pattern.matcher(targetStr);
        int lastIndex = 0;
        while (matcher1.find()) {
            // 图片
            String result = targetStr.substring(matcher1.start(), matcher1.end());
            splitTextList.add(result);
        }
        return splitTextList;
    }

    /**
     * 提取第一个 图片 id
     *
     * @param input
     * @return 请使用 getImageIdFromMarkDown(String regex, String input)；
     */
    @Deprecated
    public static int getImageId(String input) {
        try {
            String reg = "@!\\[.*?]\\((\\d+)\\)";
            Matcher matcher = Pattern.compile(reg).matcher(input);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 提取第一个 图片 id
     *
     * @param regex
     * @param input
     * @return
     */
    public static int getImageIdFromMarkDown(String regex, String input) {

        if (regex == null || input == null) {
            return -1;
        }
        try {
            Matcher matcher = Pattern.compile(regex).matcher(input);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    public static List<Integer> getImageIdsFromMarkDown(String regex, String input) {
        String reg = "@!\\[.*?]\\((\\d+)\\)";
        if (regex == null || input == null || !reg.equals(regex)) {
            return null;
        }
        List<Integer> result = new ArrayList<>();
        try {
            Matcher matcher = Pattern.compile(regex).matcher(input);
            while (matcher.find()) {
                result.add(Integer.parseInt(matcher.group(1)));
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static InputFilter getEmojiFilter() {
        return new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int
                    dstart, int dend) {
                Matcher emojiMatcher = emoji.matcher(source);
                if (emojiMatcher.find()) {
                    return "";
                }
                return null;
            }

            // 页屏蔽了 \n(换行输入) 2018-7-5 14:09:38，by tym
            String reg = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|\n";
            Pattern emoji = Pattern.compile(reg, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        };


    }

    // 感觉这里应该用dom解析，正则限制太多了 2018-5-17 10:39:59 by tym
    public static String getMarkdownWords(String source) {
        Pattern pattern = Pattern.compile("<div [^>]*class=\"block\"[^>]*>(<div[^>]*>(<div[^>]*>((<div[^>]*>[\\s\\S]*?</div>|[\\s\\S])*?</div>)|[\\s\\S])*?</div>|[\\s\\S])*?</div>");
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            String need = matcher.group(3);
            if (TextUtils.isEmpty(need)) {
                return null;
            }
            Matcher img = Pattern.compile("@!\\[.*?]\\((\\d+)\\)").matcher(need);
            if (img.find()) {
                // 这儿每个标签前面要加空格，不知道为什么
                String imgTag = " " + img.group(0);
                if (TextUtils.isEmpty(imgTag)) {
                    return source;
                }
                source = matcher.replaceFirst(imgTag);
            } else {
                // 图片失败
                source = source.replace(need, "");
                source = pattern.matcher(source).replaceFirst("");
            }
            matcher = pattern.matcher(source);
        }
        source = source.replaceAll("div", "p");
        source = source.replaceAll("<p>", " <p>");
        source = source.replaceAll("\n", "");
        source = source.replaceFirst("<p>", "");
        source = source.replaceFirst("</p>", "");

        String result = source.replaceAll("<br>| |<p>|</p>|", "");

        return TextUtils.isEmpty(result) ? result : source;
    }

    public static String dealAtUserData(String src) {
        String rege = "(?<!\u00ad)@[^./\\s\u00ad@]+";
        String atrege = "\\u00ad(?:@[^/]+?)\\u00ad";
        String holderrege = "☆zhiyi☆";
        Matcher at = Pattern.compile(atrege).matcher(src);
        List<String> atList = new ArrayList<>();
        boolean isFind = false;
        while (at.find()) {
            atList.add(at.group(0));
            isFind = true;
        }
        if (isFind) {
            src = at.replaceAll(holderrege);
        }
        Matcher prefix = Pattern.compile(rege).matcher(src);
        while (prefix.find()) {
            //匹配结果可能包含)特殊字符
            String rlt = null;
            if (prefix.group(0).contains(")")){
                rlt = prefix.group(0).replace(")","");
            }
            src = src.replaceFirst(rlt, MarkdownConfig.AT_MARK + rlt + MarkdownConfig.AT_SUFFIX);
        }
        Matcher holder = Pattern.compile(holderrege).matcher(src);
        int i = 0;
        while (holder.find()) {
            if (i < atList.size()) {
                src = src.replaceFirst(holder.group(0), atList.get(i));
            }
            i++;
        }
        return src;
    }

    public static String replaceAllAt(String src) {
        return src.replaceAll(MarkdownConfig.AT, "");
    }

    /**
     * 获取随机用户名
     *
     * @param length
     * @param needNumAndLetter 是否必须包含字母和数字
     * @return
     */
    public static String getRandomUserName(int length, boolean needNumAndLetter) {
        String reg = "(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{" + length + ",20}";
        String userName = getLetterNumber(length);
        while (needNumAndLetter) {
            userName = getLetterNumber(length);
            if (userName.matches(reg)) {
                return userName;
            }
        }
        return userName;
    }

    /**
     * 获取随机字符串 0-9,a-z,A-Z
     *
     * @param length 长度
     * @return
     */
    public static String getLetterNumber(int length) {
        String str = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            sb.append(str.charAt(random.nextInt(46)));
        }
        return sb.toString();
    }
}