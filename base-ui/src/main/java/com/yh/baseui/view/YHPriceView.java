package com.yh.baseui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class YHPriceView extends AppCompatTextView {

    public YHPriceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public YHPriceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YHPriceView(Context context) {
        super(context);
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/YHYCXinHT-Regular.ttf"));
    }
}
