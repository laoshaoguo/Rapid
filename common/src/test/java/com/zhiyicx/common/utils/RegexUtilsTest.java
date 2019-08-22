package com.zhiyicx.common.utils;

import org.junit.Test;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/10
 * @Contact master.jungle68@gmail.com
 */
public class RegexUtilsTest {
    @Test
    public void isZh() throws Exception {

        String test = "你好123dfsf";
        System.out.println("has zh = " + RegexUtils.isZh(test));

    }

    @Test
    public void isContainChinese() throws Exception {

        String test = "15你好123dfsf";
        System.out.println("has zh = " + RegexUtils.isContainChinese(test));
        System.out.println("has zh = " + RegexUtils.getChineseCouns(test));

    }

    /**
     * 检查长度是否合格  ,
     * , 用户名至少为 4 个英文字符 ，用户名至少为 2 个中文字符
     *
     * @throws Exception
     */
    @Test
    public void isUsernameLength() throws Exception {

        String test = "15你";
        System.out.println("是否合格 = " + RegexUtils.isUsernameLength(test, 2, 8));
        String test1 = "1你";
        System.out.println("是否合格 = " + RegexUtils.isUsernameLength(test1, 2, 8));
        String test2 = "你";
        System.out.println("是否合格 = " + RegexUtils.isUsernameLength(test2, 2, 8));
        String test3 = "dsa";
        System.out.println("是否合格 = " + RegexUtils.isUsernameLength(test3, 2, 8));
        String test4 = "1234";
        System.out.println("是否合格 = " + RegexUtils.isUsernameLength(test4, 2, 8));

    }

}