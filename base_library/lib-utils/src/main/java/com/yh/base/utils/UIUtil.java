package com.yh.base.utils;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class UIUtil {

    public static final ViewGroup.LayoutParams layoutParamsMatchParent() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public static final ViewGroup.MarginLayoutParams layoutParamsMatchParent(int topMargin) {
        ViewGroup.MarginLayoutParams  params= new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.topMargin = topMargin;
        return params;
    }


    public static void setImageViewFitWidth(ImageView mv, int newWidth) {
        mv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int width = mv.getMeasuredWidth();
                int height = mv.getMeasuredHeight();
                mv.getLayoutParams().width = newWidth;
                mv.getLayoutParams().height = height * newWidth / width;
                mv.setLayoutParams(mv.getLayoutParams());
                mv.setScaleType(ImageView.ScaleType.FIT_XY);
                mv.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    public static void setListViewHeightBasedOnChildren(ListView listView, boolean wrapWidth) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int maxWidth = 0;
        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
            int width = listItem.getMeasuredWidth();
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        if (wrapWidth) {
            params.width = maxWidth;
        }
        listView.setLayoutParams(params);
    }

    public static void clipView(View view, float radiusDp) {
        view.setClipToOutline(true);
        view.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), DisplayUtil.dp2px(radiusDp));
            }

        });
    }

    public static void setViewHeightByWidth(final View mv) {
        mv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                mv.getLayoutParams().height = mv.getMeasuredWidth();
                mv.setLayoutParams(mv.getLayoutParams());
                mv.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    public static void setViewWidthByHeight(final View mv) {
        mv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                mv.getLayoutParams().width = mv.getMeasuredHeight();
                mv.setLayoutParams(mv.getLayoutParams());
                mv.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }
}