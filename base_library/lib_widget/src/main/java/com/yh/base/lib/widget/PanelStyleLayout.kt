package com.yh.base.lib.widget

import android.content.Context
import android.content.res.TypedArray
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat

/**
 * @description 特定样式的模板，方便在xml设置一些属性；
 * @date: 2021/4/23 8:41 PM
 * @author: zengbobo
 */
class PanelStyleLayout : FrameLayout {
    var lineNumber: Int = -1
    var maxLength: Int = 0
    var isNoNull: Boolean = true
    var isShowArrow: Boolean = true
    var isShowSwitch: Boolean = true
    var textTitle: String = ""
    var textTitleColor: Int = 0
    var textTitleSize: Float = 0f
    var nullTitleSize: Float = 0f
    var hintEditText: String = ""
    var hintEditTextColor: Int = 0
    var hintEditTextSize: Float = 0f
    var editTextColor: Int = 0
    var editTextSize: Float = 0f
    var imgSwitchSrc: Int = 0
    var idTvTitle: Int = 0
    var idEditText: Int = 0
    var idImageArrow: Int = 0
    var idImageSwitch: Int = 0
    var idViewNoNull: Int = 0
    var viewNoNull: TextView? = null
    var imgArrow: ImageView? = null
    var imgSwitch: ImageView? = null
    var tvTitle: TextView? = null
    var editText: TextView? = null

    @LayoutRes
    var layoutId: Int = 0

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray: TypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.PanelStyleLayout, defStyleAttr, 0)
        lineNumber = typedArray.getInt(R.styleable.PanelStyleLayout_lineNumber, -1)
        maxLength = typedArray.getInt(R.styleable.PanelStyleLayout_maxLength, 100)
        layoutId = typedArray.getResourceId(R.styleable.PanelStyleLayout_layoutId, 0)
        isNoNull = typedArray.getBoolean(R.styleable.PanelStyleLayout_isNoNull, true)
        nullTitleSize = typedArray.getDimension(R.styleable.PanelStyleLayout_nullTitleSize, resources.getDimension(R.dimen.textSize_big))
        isShowArrow = typedArray.getBoolean(R.styleable.PanelStyleLayout_isShowArrow, true)
        isShowSwitch = typedArray.getBoolean(R.styleable.PanelStyleLayout_isShowSwitch, false)
        imgSwitchSrc = typedArray.getResourceId(R.styleable.PanelStyleLayout_imgSwitchSrc, 0)
        textTitle = typedArray.getString(R.styleable.PanelStyleLayout_textTitle) ?: ""
        textTitleColor = typedArray.getColor(R.styleable.PanelStyleLayout_textTitleColor, ContextCompat.getColor(context, R.color.textColor_4d4d4d))
        textTitleSize = typedArray.getDimension(R.styleable.PanelStyleLayout_textTitleSize, resources.getDimension(R.dimen.textSize_big))

        hintEditText = typedArray.getString(R.styleable.PanelStyleLayout_hintEditText) ?: ""
        hintEditTextColor = typedArray.getColor(R.styleable.PanelStyleLayout_hintEditTextColor, ContextCompat.getColor(context, R.color.textColor_cccccc))
        hintEditTextSize = typedArray.getDimension(R.styleable.PanelStyleLayout_hintEditTextSize, resources.getDimension(R.dimen.textSize_big))
        editTextColor = typedArray.getColor(R.styleable.PanelStyleLayout_editTextColor, ContextCompat.getColor(context, R.color.textColor_grey_black))
        editTextSize = typedArray.getDimension(R.styleable.PanelStyleLayout_editTextSize, resources.getDimension(R.dimen.textSize_big))

        idTvTitle = typedArray.getResourceId(R.styleable.PanelStyleLayout_idTvTitle, R.id.tv_title)
        idEditText = typedArray.getResourceId(R.styleable.PanelStyleLayout_idEditText, R.id.editText)
        idImageArrow = typedArray.getResourceId(R.styleable.PanelStyleLayout_idImageArrow, R.id.img_arrow)
        idViewNoNull = typedArray.getResourceId(R.styleable.PanelStyleLayout_idViewNoNull, R.id.viewNoNull)
        idImageSwitch = typedArray.getResourceId(R.styleable.PanelStyleLayout_idImageSwitch, R.id.img_switch)
        typedArray.recycle()
        if (layoutId == 0) {
            return
        }

        val view = LayoutInflater.from(context).inflate(layoutId, this, true)
        viewNoNull = view.findViewById(idViewNoNull)
        tvTitle = view.findViewById(idTvTitle)
        imgArrow = view.findViewById(idImageArrow)
        editText = view.findViewById(idEditText)
        imgSwitch = view.findViewById(idImageSwitch)
        if (lineNumber > 0) {
            if (lineNumber == 1) {
                editText?.maxLines = 1
                editText?.ellipsize = TextUtils.TruncateAt.END
            } else {
                editText?.setLines(lineNumber)
            }
        }
        if (maxLength > 0) {
            editText?.filters = arrayOf<InputFilter>(LengthFilter(maxLength))
        }
        viewNoNull?.visibility = if (isNoNull) View.VISIBLE else View.GONE
        imgArrow?.visibility = if (isShowArrow) View.VISIBLE else View.GONE
        imgSwitch?.visibility = if (isShowSwitch) View.VISIBLE else View.GONE
        viewNoNull?.apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, nullTitleSize)
        }
        tvTitle?.apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textTitleSize)
            setTextColor(textTitleColor)
            text = textTitle
        }
        editText?.apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize)
            setTextColor(editTextColor)
            hint = hintEditText
            setHintTextColor(hintEditTextColor)
        }
        imgSwitch?.setImageResource(imgSwitchSrc)
    }

    fun setIsNoNull(isNoNull: Boolean) {
        this.isNoNull = isNoNull
        viewNoNull?.visibility = if (isNoNull) View.VISIBLE else View.GONE
    }

    fun setHintEditTextView(hintEditText: String) {
        this.hintEditText = hintEditText
        editText?.apply {
            hint = hintEditText
        }
    }
}