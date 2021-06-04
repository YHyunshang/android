package com.yh.baseui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.yh.baseui.utils.ImageLoaderUtils;
/**
 * @description 封装ImageView
 * @date: 5/28/21 1:55 PM
 * @author: 张致远
 */
public class YHImageView extends AppCompatImageView {

    public YHImageView(Context context) {
        super(context);
    }

    public YHImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public YHImageView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    String path;

    public void load(String path) {
        if (TextUtils.isEmpty(path) || !path.equals(this.path)) {
            this.path = path;
            ImageLoaderUtils.INSTANCE.load(getContext(), path, this);
        }
    }
}
