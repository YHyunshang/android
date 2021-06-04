package com.yh.base.lib.widget

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

/**
 * @author zengbobo
 * @description 代替系统的ActionBar、ToolBar
 * @time 2021/4/9 4:47 PM
 */
class ActionBarLayout : FrameLayout {
    private lateinit var view: View
    private var middleLayout: LinearLayout? = null
    private var customLayout: FrameLayout? = null
    private var leftLayout: FrameLayout? = null
    private var rightLayout: FrameLayout? = null
    private var rightLayoutAlign: FrameLayout? = null
    private var title: TextView? = null
    private var subTitle: TextView? = null
    private var leftTv: TextView? = null
    private var rightTv: TextView? = null
    private var rightTvAlign: TextView? = null
    private var leftImg: ImageView? = null
    private var rightImg: ImageView? = null
    private var rightImgAlign: ImageView? = null
    private var actionbarLayout: FrameLayout? = null
    private var backgroundView: View? = null
    private var actionBarBackground: Drawable? = null
    private var actionBarTitleTextColor = 0
    private var actionBarHeight = 0
    private var statusBarHeight = 0
    private var isShowStatusBarHeight = false
    private var fractionAlpha = 1f

    @LayoutRes
    private var layoutId = 0

    constructor(context: Context?) : super(context!!) {
        initAttrs(context, null, 0, 0)
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        initAttrs(context, attrs, 0, 0)
        init(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {
        initAttrs(context, attrs, defStyle, 0)
        init(context)
    }

    private fun initAttrs(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (context == null) {
            actionBarBackground = ContextCompat.getDrawable(context!!, R.color.color_status_action_bar)
            isShowStatusBarHeight = false
            fractionAlpha = 1.0f
            return
        }
        val a: TypedArray
        a = if (attrs == null) {
            context.obtainStyledAttributes(intArrayOf(R.styleable.ActionBarLayout_actionBarBackground,
                    R.styleable.ActionBarLayout_isShowStatusBarHeight))
        } else {
            context.obtainStyledAttributes(attrs, R.styleable.ActionBarLayout
                    , defStyleAttr, defStyleRes)
        }
        actionBarBackground = a.getDrawable(R.styleable.ActionBarLayout_actionBarBackground)
        isShowStatusBarHeight = a.getBoolean(R.styleable.ActionBarLayout_isShowStatusBarHeight, false)
        fractionAlpha = a.getFloat(R.styleable.ActionBarLayout_fractionAlpha, 1.0f)
        layoutId = a.getResourceId(R.styleable.ActionBarLayout_android_layout, R.layout.actionbar_layout)
        if (actionBarBackground == null) {
            actionBarBackground = ContextCompat.getDrawable(context, R.color.color_status_action_bar)
        }
        a.recycle()
    }

    fun getStatusBarHeight(): Int {
        return getInternalDimensionSize(resources, "status_bar_height")
    }

    private fun getInternalDimensionSize(res: Resources, key: String): Int {
        var result = 0
        val resourceId = res.getIdentifier(key, "dimen", "android")
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun init(context: Context?) {
        super.setBackgroundResource(android.R.color.transparent)
        actionBarTitleTextColor = resources.getColor(android.R.color.white)
        actionBarHeight = resources.getDimensionPixelSize(R.dimen.app_header_height)
        statusBarHeight = getStatusBarHeight()
        view = LayoutInflater.from(context).inflate(layoutId, this, false)
        backgroundView = view.findViewById(R.id.backgroundView)
        if (backgroundView != null) {
            setFractionBg(fractionAlpha)
            backgroundView!!.background = actionBarBackground
        }
        actionbarLayout = view.findViewById(R.id.actionbar_layout)
        customLayout = view.findViewById<View>(R.id.actionbar_custom_layout) as FrameLayout
        leftLayout = view.findViewById<View>(R.id.actionbar_left_layout) as FrameLayout
        leftTv = view.findViewById<View>(R.id.actionbar_left_tv) as TextView
        middleLayout = view.findViewById(R.id.actionbar_middle_layout)
        rightLayout = view.findViewById<View>(R.id.actionbar_right_layout) as FrameLayout
        rightLayoutAlign = view.findViewById<View>(R.id.actionbar_right_layout_align) as FrameLayout
        rightTv = view.findViewById<View>(R.id.actionbar_right_tv) as TextView
        rightTvAlign = view.findViewById<View>(R.id.actionbar_right_tv_align) as TextView
        title = view.findViewById<View>(R.id.actionbar_title) as TextView
        subTitle = view.findViewById<View>(R.id.actionbar_subtitle) as TextView
        leftImg = view.findViewById<View>(R.id.actionbar_left_img) as ImageView
        rightImg = view.findViewById<View>(R.id.actionbar_right_img) as ImageView
        rightImgAlign = view.findViewById<View>(R.id.actionbar_right_img_align) as ImageView
        addView(view, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
    }

    fun getCustomLayout(): FrameLayout? {
        middleLayout!!.visibility = View.GONE
        customLayout!!.visibility = View.VISIBLE
        return customLayout
    }

    fun getLeftLayout(): FrameLayout? {
        leftLayout!!.visibility = View.VISIBLE
        return leftLayout
    }

    fun getMiddleLayout(): LinearLayout? {
        middleLayout!!.visibility = View.VISIBLE
        customLayout!!.visibility = View.INVISIBLE
        return middleLayout
    }

    fun getRightLayout(): FrameLayout? {
        rightLayout!!.visibility = View.VISIBLE
        return rightLayout
    }

    fun getRightLayoutAlign(): FrameLayout? {
        rightLayoutAlign!!.visibility = View.VISIBLE
        return rightLayoutAlign
    }

    fun getTitle(): TextView? {
        getMiddleLayout()
        title!!.visibility = View.VISIBLE
        return title
    }

    fun getSubTitle(): TextView? {
        subTitle!!.visibility = View.VISIBLE
        return subTitle
    }

    fun getLeftImg(): ImageView? {
        getLeftLayout()
        leftTv!!.visibility = View.GONE
        leftImg!!.visibility = View.VISIBLE
        return leftImg
    }

    fun getLeftTv(): TextView? {
        getLeftLayout()
        leftImg!!.visibility = View.GONE
        leftTv!!.visibility = View.VISIBLE
        return leftTv
    }

    fun getRightImg(): ImageView? {
        getRightLayout()
        rightTv!!.visibility = View.GONE
        rightImg!!.visibility = View.VISIBLE
        rightTv!!.visibility = View.GONE
        return rightImg
    }

    fun getRightImgAlign(): ImageView? {
        getRightLayoutAlign()
        rightTvAlign!!.visibility = View.GONE
        rightImgAlign!!.visibility = View.VISIBLE
        return rightImgAlign
    }

    fun getRightTv(): TextView? {
        getRightLayout()
        rightImg!!.visibility = View.GONE
        rightTv!!.visibility = View.VISIBLE
        return rightTv
    }

    fun getRightTvAlign(): TextView? {
        getRightLayoutAlign()
        rightImgAlign!!.visibility = View.GONE
        rightTvAlign!!.visibility = View.VISIBLE
        return rightTvAlign
    }

    fun getActionbarLayout(): ViewGroup? {
        return actionbarLayout
    }
    fun getBackgroundView(): View? {
        return backgroundView
    }



    @Deprecated("")
    override fun setBackgroundColor(@ColorInt color: Int) {
//        super.setBackgroundColor(color);
    }

    @Deprecated("")
    override fun setBackgroundResource(@DrawableRes resid: Int) {
//        super.setBackgroundResource(resid);
    }

    @Deprecated("")
    override fun setBackground(background: Drawable) {
//        super.setBackground(background);
    }

    @Deprecated("")
    override fun setBackgroundDrawable(background: Drawable) {
//        super.setBackgroundDrawable(background);
    }

    @Deprecated("")
    override fun setBackgroundTintList(tint: ColorStateList?) {
//        super.setBackgroundTintList(tint);
    }

    @Deprecated("")
    override fun setBackgroundTintMode(tintMode: PorterDuff.Mode?) {
//        super.setBackgroundTintMode(tintMode);
    }

    fun setShowStatusBarHeight(showStatusBarHeight: Boolean) {
        isShowStatusBarHeight = showStatusBarHeight
        requestLayout()
    }

    fun setActionBarBackground(@DrawableRes bgResid: Int) {
        backgroundView!!.setBackgroundResource(bgResid)
    }

    fun setActionBarBackground(actionBarBackground: Drawable?) {
        backgroundView!!.background = actionBarBackground
    }

    /**
     * 设置背景颜色透明度变化；
     *
     * @param fraction 1显示；0隐藏；
     * @return
     */
    fun setFractionBg(fraction: Float) {
        var fraction = fraction
        if (fraction <= 0) {
            fraction = 0f
        } else if (fraction >= 1) {
            fraction = 1f
        }
        backgroundView!!.alpha = fraction
    }

    /**
     * 设置标题颜色透明度变化；
     *
     * @param fraction 1显示；0隐藏；
     * @return
     */
    fun setFractionTitleColor(fraction: Float) {
        var fraction = fraction
        if (fraction <= 0) {
            fraction = 0f
        } else if (fraction >= 1) {
            fraction = 1f
        }
        title!!.setTextColor(getFractionBgColor(fraction, actionBarTitleTextColor))
    }

    /**
     * @param flag false---变成不透明；
     */
    fun alphaBg(flag: Boolean) {
        val ani = if (flag) ValueAnimator.ofFloat(1f, 0f) else ValueAnimator.ofFloat(0f, 1f)
        ani.addUpdateListener { valueAnimator: ValueAnimator ->
            val alpah = valueAnimator.animatedValue as Float
            setFractionBg(alpah)
        }
        ani.interpolator = DecelerateInterpolator()
        ani.duration = 600
        ani.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(totalActionBarHeight, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    val totalActionBarHeight: Int
        get() = actionBarHeight + if (isShowStatusBarHeight
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) statusBarHeight else 0

    fun setActionBarBackAndTitle(@StringRes titleId: Int) {
        setActionBarBackAndTitle(resources.getString(titleId))
    }

    fun setActionBarBackAndTitle(title: String?) {
        //设置标题文本
        getTitle()!!.text = title
        //设置返回按钮图片
        getLeftImg()!!.setImageResource(R.drawable.ic_back)
        //设置返回按钮点击事件
        getLeftLayout()!!.setOnClickListener { (context as Activity).finish() }
    }

    companion object {
        /**
         * 设置颜色透明度变化；
         *
         * @param fraction   1显示；0隐藏；
         * @param colorValue
         * @return
         */
        fun getFractionBgColor(fraction: Float, colorValue: Int): Int {
            return colorValue and ((255 * fraction).toInt() shl 24 or 0xffffff)
        }
    }
}