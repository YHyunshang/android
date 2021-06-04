package com.yh.base.ui.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.yh.base.utils.DisplayUtil;
import com.yh.base.utils.Util;

public class PopProgress {
    Activity activity;
    LinearLayout mPopView;
    ViewGroup parentView;

    boolean usePopWindow = false;

    public void setUsePopWindow(boolean usePopWindow) {
        this.usePopWindow = usePopWindow;
    }

    Handler handler;

    public PopProgress(Activity activity) {
        this(activity, null);
    }

    public PopProgress(Activity activity, View parentView) {
        this.activity = activity;
        handler = new Handler(Looper.getMainLooper());
        this.parentView = (ViewGroup) parentView;
    }

    int refCounter;
    View focusView;

    boolean isKeyboardEnable = true;

    public void setKeyboardEnabled(boolean enabled) {
        isKeyboardEnable = enabled;
    }

    boolean isTouchable = false;

    public void setTouchEnabled(boolean enabled) {
        isTouchable = enabled;
    }

    PopupWindow mPopWindow;

    void removeView() {
        if (parentView != null) {
            parentView.removeView(mPopView);
        } else {
            FrameLayout content = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            content.removeView(mPopView);
        }
        mPopWindow.dismiss();
    }

    void addView() {
        handler.postDelayed(() -> {
            if (mPopView == null)
                return;
            try {
                FrameLayout content = activity.getWindow().getDecorView().findViewById(android.R.id.content);
                if (usePopWindow) {
                    LinearLayout contentView = new LinearLayout(activity);
                    mPopWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
                    mPopWindow.setClippingEnabled(false);
                    mPopWindow.setBackgroundDrawable(new ColorDrawable(0x3F000000));
                    mPopWindow.setTouchable(isTouchable);
//                    mPopWindow.setFocusable(false);
//                    mPopWindow.setOutsideTouchable(true);
                    if (!isKeyboardEnable) {
                        contentView.setFocusable(true);
                        contentView.setFocusableInTouchMode(true);
                        contentView.requestFocus();
                        contentView.setOnKeyListener((v, keyCode, event) -> true);
                    }
//                    FrameLayout content = activity.getWindow().getDecorView().findViewById(android.R.id.content);
                    mPopWindow.showAtLocation(content, Gravity.LEFT, 0, 0);
                }
                if (parentView != null) {
                    parentView.addView(mPopView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                } else {
                    content.addView(mPopView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 800);
    }

    Runnable showRunnable = () -> {
        Log.v(PopProgress.class.getName(), "show " + activity.getClass().getName() + " " + refCounter);
        try {
            if (++refCounter <= 0) {
                return;
            }
            if (mPopView != null) {
                return;
            }
            mPopView = new LinearLayout(activity);
            mPopView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            mPopView.setOrientation(LinearLayout.VERTICAL);

            mPopView.setGravity(Gravity.CENTER);
//            mPopView.setBackgroundColor(0x2F000000);

            if (isTouchable) {
                mPopView.setEnabled(false);
                mPopView.setFocusableInTouchMode(false);
            } else {
                mPopView.setEnabled(true);
                mPopView.setClickable(true);
                mPopView.setFocusableInTouchMode(true);
                mPopView.setOnTouchListener((v, event) -> true);
            }
            if (isKeyboardEnable) {
                focusView = null;
            } else {
                focusView = activity.getCurrentFocus();
                mPopView.setFocusable(true);
                mPopView.requestFocus();
                mPopView.setOnKeyListener((v, keyCode, event) -> true);
            }

//            mPopView.setBackgroundColor(0x1F000000);

            ProgressWheel progressBar = new ProgressWheel(activity);
//            progressBar.setBarColor(0xFF00B501);
            progressBar.setBarColor(0xcFFF4D4F);
            progressBar.setBarWidth((int) DisplayUtil.dp2px(2));
            progressBar.setCircleRadius((int) DisplayUtil.dp2px(15));
            progressBar.setSpinSpeed(1.0f);
            progressBar.spin();

            mPopView.addView(progressBar, new LinearLayout.LayoutParams(DisplayUtil.dp2px(100), DisplayUtil.dp2px(100)));

            addView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    Runnable hideRunnable = () -> {
        Log.v(PopProgress.class.getName(), "hide " + activity.getClass().getName() + " " + refCounter);
        if (--refCounter <= 0) {
            if (mPopView != null) {
                try {
                    removeView();
                    focusView.requestFocus();
                } catch (Exception e) {
                }
                mPopView = null;
            }
        }
    };

    public void show() {
        handler.post(showRunnable);
    }

    public void hide() {
        handler.post(hideRunnable);
    }

    public void close() {
        handler.removeCallbacks(showRunnable);
        handler.removeCallbacks(hideRunnable);
        handler.post(() -> {
            Log.v(PopProgress.class.getName(), "close " + activity.getClass().getName() + " " + refCounter);
            if (mPopView != null) {
                try {
                    removeView();
                    focusView.requestFocus();
                } catch (Exception e) {
                }
                mPopView = null;
            }
        });
    }
}
