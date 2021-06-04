package com.yh.trading;

import com.yh.baseui.utils.NumUtil;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @description $
 * @date: 2021/6/2 4:25 PM
 * @author: zengbobo
 */
public class JUnitTest {
    @Test
    public void changeStock() {
//        String str8 = "0.01";
//        System.out.println(str8 + ":" + NumUtil.INSTANCE.changeStock(str8));
//        String str7 = "0.0";
//        System.out.println(str7 + ":" + NumUtil.INSTANCE.changeStock(str7));
//        String str6 = "0";
//        System.out.println(str6 + ":" + NumUtil.INSTANCE.changeStock(str6));
//        String str5 = "123";
//        System.out.println(str5 + ":" + NumUtil.INSTANCE.changeStock(str5));
//        String str4 = "123.00100";
//        System.out.println(str4 + ":" + NumUtil.INSTANCE.changeStock(str4));
//        String str3 = "123.001";
//        System.out.println(str3 + ":" + NumUtil.INSTANCE.changeStock(str3));
//        String str2 = "123.00";
//        System.out.println(str2 + ":" + NumUtil.INSTANCE.changeStock(str2));
//        String str1 = "123.11";
//        System.out.println(str1 + ":" + NumUtil.INSTANCE.changeStock(str1));
        assertThat(NumUtil.INSTANCE.changeStock("0.01"), is("0.01"));
        assertThat(NumUtil.INSTANCE.changeStock("0.0"), is("0"));
        assertThat(NumUtil.INSTANCE.changeStock("0"), is("0"));
        assertThat(NumUtil.INSTANCE.changeStock("123"), is("123"));
        assertThat(NumUtil.INSTANCE.changeStock("123.00100"), is("123.001"));
        assertThat(NumUtil.INSTANCE.changeStock("123.001"), is("123.001"));
        assertThat(NumUtil.INSTANCE.changeStock("123.00"), is("123"));
        assertThat(NumUtil.INSTANCE.changeStock("123.11"), is("123.11"));
    }
}
