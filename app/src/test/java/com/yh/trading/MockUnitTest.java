package com.yh.trading;

import android.content.Context;

import com.yh.baseui.utils.NumUtil;
import com.yh.tobtrading.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @description $
 * @date: 2021/6/2 4:34 PM
 * @author: zengbobo
 */
@RunWith(MockitoJUnitRunner.class)
public class MockUnitTest {
    private static final String FAKE_STRING = "AndroidUnitTest";

    @Mock
    Context mMockContext;

    @Test
    public void readStringFromContext_LocalizedString() {
        //模拟方法调用的返回值，隔离对Android系统的依赖
        when(mMockContext.getString(R.string.app_name)).thenReturn(FAKE_STRING);
        assertThat(mMockContext.getString(R.string.app_name), is(FAKE_STRING));

        when(mMockContext.getPackageName()).thenReturn("com.jdqm.androidunittest");
        System.out.println(mMockContext.getPackageName());
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
        assertThat(NumUtil.INSTANCE.changeStock("0.01"),is("0.01"));
        assertThat(NumUtil.INSTANCE.changeStock( "0.0"),is("0"));
        assertThat(NumUtil.INSTANCE.changeStock("0"),is("0"));
        assertThat(NumUtil.INSTANCE.changeStock("123"),is("123"));
        assertThat(NumUtil.INSTANCE.changeStock("123.00100"),is("123.001"));
        assertThat(NumUtil.INSTANCE.changeStock("123.001"),is("123.001"));
        assertThat(NumUtil.INSTANCE.changeStock("123.00"),is("123"));
        assertThat(NumUtil.INSTANCE.changeStock("123.11"),is("123.11"));
    }
}
