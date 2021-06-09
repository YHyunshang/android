package com.yh.trading;

import com.yh.demo.activity.MainActivity;
import com.yh.base.BaseApplication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

/**
 * @description $
 * @date: 2021/6/2 5:15 PM
 * @author: zengbobo
 */
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    @Before
    public void  setUp() {
        BaseApplication app = (BaseApplication) RuntimeEnvironment.application;

    }
    @Test
    public void bottomNavigationView() {
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
//        BottomNavigationView navigationView =  activity.mViewBinding.bottomNavigationView;
//        navigationView.setSelectedItemId(R.id.CategoryFragment);
    }
}
