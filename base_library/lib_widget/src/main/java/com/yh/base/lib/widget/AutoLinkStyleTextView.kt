package com.yh.base.lib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

/**
 *
 * @Link https://github.com/wangshaolei/AutoSpannableTextView
 * Created by Shaolei on 2016/10/1.
 * textview 添加链接、高亮、点击不同文本内容对应不同响应
 */
class AutoLinkStyleTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {
    private var defaultTextValue: String? = null
    private lateinit var textValueSplit: String
    private var hasUnderLine = true
    private var startImageDes = 0
    private var mClickCallBack: ClickCallBack? = null

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoLinkStyleTextView, defStyleAttr, 0)
        styleType = typedArray.getInt(R.styleable.AutoLinkStyleTextView_AutoLinkStyleTextView_type, TYPE_CONTENT_TEXT)
        defaultTextValue = typedArray.getString(R.styleable.AutoLinkStyleTextView_AutoLinkStyleTextView_text_value)
        textValueSplit = typedArray.getString(R.styleable.AutoLinkStyleTextView_AutoLinkStyleTextView_text_value_split)
                ?: "#"
        defaultColor = typedArray.getColor(R.styleable.AutoLinkStyleTextView_AutoLinkStyleTextView_default_color, defaultColor)
        hasUnderLine = typedArray.getBoolean(R.styleable.AutoLinkStyleTextView_AutoLinkStyleTextView_has_under_line, hasUnderLine)
        startImageDes = typedArray.getResourceId(R.styleable.AutoLinkStyleTextView_AutoLinkStyleTextView_start_image, 0)
        addStyle()
        typedArray.recycle()
    }

    /**
     * 部分文字链接的通过xml设置静态渲染
     */
    private fun addStyle() {
        if (!TextUtils.isEmpty(defaultTextValue) && defaultTextValue!!.contains(textValueSplit)) {
            val values = defaultTextValue!!.split(textValueSplit).toTypedArray()
            val spannableString = SpannableString(text.toString().trim { it <= ' ' })
            for (i in values.indices) {
                if (!spannableString.contains(values[i])) {
                    continue
                }
                spannableString.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        if (mClickCallBack != null) {
                            mClickCallBack!!.onClick(i)
                        }
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.color = defaultColor
                        ds.isUnderlineText = hasUnderLine
                    }
                }, text.toString().trim { it <= ' ' }.indexOf(values[i]),
                        text.toString().trim { it <= ' ' }.indexOf(values[i]) + values[i].length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            text = spannableString
            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    /**
     * 以image开头风格的需要动态调用这个方法
     * 图片和文字已经过居中适配
     *
     * @param text
     */
    fun setStartImageText(text: CharSequence) {
        if (styleType == TYPE_START_IMAGE && !TextUtils.isEmpty(text) && startImageDes != 0) {
            val spannableString = SpannableString("   $text")
            val span = CenteredImageSpan(context, startImageDes)
            spannableString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setText(spannableString)
        }
    }

    private inner class CenteredImageSpan(context: Context, drawableRes: Int) : ImageSpan(context, drawableRes) {
        override fun draw(canvas: Canvas, text: CharSequence,
                          start: Int, end: Int, x: Float,
                          top: Int, y: Int, bottom: Int, paint: Paint) {
            // image to draw
            val b = drawable
            // font metrics of text to be replaced
            val fm = paint.fontMetricsInt
            val transY = (y + fm.descent + y + fm.ascent) / 2 - b.bounds.bottom / 2
            canvas.save()
            canvas.translate(x, transY.toFloat())
            b.draw(canvas)
            canvas.restore()
        }
    }

    fun setOnClickCallBack(clickCallBack: ClickCallBack?) {
        mClickCallBack = clickCallBack
    }

    interface ClickCallBack {
        fun onClick(position: Int)
    }

    companion object {
        private const val TYPE_START_IMAGE = 0
        private const val TYPE_CONTENT_TEXT = 1
        private var styleType = TYPE_CONTENT_TEXT
        private var defaultColor = Color.parseColor("#f23218")
    }

    init {
        init(context, attrs, defStyleAttr)
    }
}