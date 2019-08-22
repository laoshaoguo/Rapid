package com.zhiyicx.baseproject;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    /**
     * summary 获取app可用的最大内存
     * steps
     * expected
     */
    @Test
    public void getMaxMemory() throws Exception {
        System.out.print("app可使用最大内存----》"+Runtime.getRuntime().maxMemory()/1024/1024+"MB");
    }
}