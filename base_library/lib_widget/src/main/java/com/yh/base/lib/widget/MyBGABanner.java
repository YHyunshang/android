package com.yh.base.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cn.bingoogolapple.bgabanner.BGABanner;

public class MyBGABanner extends BGABanner {

    public MyBGABanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyBGABanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
