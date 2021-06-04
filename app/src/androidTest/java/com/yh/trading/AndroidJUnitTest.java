package com.yh.trading;


import androidx.test.runner.AndroidJUnit4;

import com.yh.base.constant.Config;
import com.yh.baseui.utils.NumUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @description $
 * @date: 2021/6/2 4:58 PM
 * @author: zengbobo
 */
@RunWith(AndroidJUnit4.class)
public class AndroidJUnitTest {


    @Before
    public void setUp() {
        Config.isDebug = true;
    }

    @Test
    public void changeStock() {
//        String str8 = "0.01";
//        LogUtils.d(str8 + ":" + NumUtil.INSTANCE.changeStock(str8));
//        String str7 = "0.0";
//        LogUtils.d(str7 + ":" + NumUtil.INSTANCE.changeStock(str7));
//        String str6 = "0";
//        LogUtils.d(str6 + ":" + NumUtil.INSTANCE.changeStock(str6));
//        String str5 = "123";
//        LogUtils.d(str5 + ":" + NumUtil.INSTANCE.changeStock(str5));
//        String str4 = "123.00100";
//        LogUtils.d(str4 + ":" + NumUtil.INSTANCE.changeStock(str4));
//        String str3 = "123.001";
//        LogUtils.d(str3 + ":" + NumUtil.INSTANCE.changeStock(str3));
//        String str2 = "123.00";
//        LogUtils.d(str2 + ":" + NumUtil.INSTANCE.changeStock(str2));
//        String str1 = "123.11";
//        LogUtils.d(str1 + ":" + NumUtil.INSTANCE.changeStock(str1));

        Assert.assertEquals(NumUtil.INSTANCE.changeStock(null), "");
        Assert.assertEquals(NumUtil.INSTANCE.changeStock("0.01"), "0.01");
        Assert.assertEquals(NumUtil.INSTANCE.changeStock("0.0"), "0");
        Assert.assertEquals(NumUtil.INSTANCE.changeStock("0"), "0");
        Assert.assertEquals(NumUtil.INSTANCE.changeStock("123"), "123");
        Assert.assertEquals(NumUtil.INSTANCE.changeStock("123.00100"), "123.001");
        Assert.assertEquals(NumUtil.INSTANCE.changeStock("123.001"), "123.001");
        Assert.assertEquals(NumUtil.INSTANCE.changeStock("123.00"), "123");
        Assert.assertEquals(NumUtil.INSTANCE.changeStock("123.11"), "123.11");
    }
}
