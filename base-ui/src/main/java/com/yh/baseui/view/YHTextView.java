package com.yh.baseui.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * @description 封装ImageView
 * @date: 5/28/21 1:55 PM
 * @author: 张致远
 */
public class YHTextView extends AppCompatTextView {

    public YHTextView(Context context) {
        super(context);
    }

    public YHTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public YHTextView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    String text;

    public void setText(String text) {
        if (text != null && !text.equals(this.text)) {
            this.text = text;
            super.setText(text);
        }
    }
}
