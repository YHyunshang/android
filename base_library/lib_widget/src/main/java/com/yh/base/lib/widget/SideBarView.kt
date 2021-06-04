package com.yh.base.lib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller

/**
 * @Link https://blog.csdn.net/qq_35352552/article/details/64918980
 * @description $
 * @date: 2021/4/26 3:55 PM
 * @author: zengbobo
 */
class SideBarView : View {
    private var mListener: OnTouchLetterChangedListener? = null

    interface OnTouchLetterChangedListener {
        fun onTouchLetterChanged(letter: String?, position: Int)
    }

    fun setOnTouchLetterChangedListener(listener: OnTouchLetterChangedListener?) {
        mListener = listener
    }

    // 向右偏移多少画字符， default 30
    var mWidthOffset = 30.0f

    // 最小字体大小
    var mMinFontSize = dp2px(24f)

    // 最大字体大小
    var mMaxFontSize = dp2px(48f)

    // 提示字体大小
    var mTipFontSize = dp2px(52f)

    // 提示字符的额外偏移
    var mAdditionalTipOffset = 20.0f

    // 贝塞尔曲线控制的高度
    var mMaxBezierHeight = 150.0f

    // 贝塞尔曲线单侧宽度
    var mMaxBezierWidth = 240.0f

    // 贝塞尔曲线单侧模拟线量
    var mMaxBezierLines = 32

    // 列表字符颜色
    var mFontColor = -0x1

    // 提示字符颜色
    var mTipFontColor = -0x2cc1b8

    //    private val ConstChar = arrayOf("#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
//            , "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
    private var ConstChar = mutableListOf<String>()
    var mChooseIndex = -1
    var mPaint = Paint()
    var mTouch = PointF()
    lateinit var mBezier1: Array<PointF?>
    lateinit var mBezier2: Array<PointF?>
    var mLastOffset = FloatArray(ConstChar.size) // 记录每一个字母的x方向偏移量, 数字<=0
    var mPointF = PointF()
    var mScroller: Scroller? = null
    var mAnimating = false
    var mAnimationOffset = 0f
    var mHideAnimation = false
    var mAlpha = 255
    var mHideWaitingHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == 1) {
                mHideAnimation = true
                mAnimating = false
                this@SideBarView.invalidate()
                return
            }
            super.handleMessage(msg)
        }
    }

    fun initConstChar(list: List<String>) {
        ConstChar.clear()
        if (list != null && list.isNotEmpty()) {
            ConstChar.addAll(list)
        }
        mLastOffset = FloatArray(ConstChar.size)
        postInvalidate()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initData(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initData(context, attrs)
    }

    constructor(context: Context?) : super(context) {
        initData(null, null)
    }

    private fun initData(context: Context?, attrs: AttributeSet?) {
        if (context != null && attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.FancyIndexer, 0, 0)
            mWidthOffset = a.getDimension(R.styleable.FancyIndexer_widthOffset, mWidthOffset)
            // 最小
            mMinFontSize = a.getDimensionPixelSize(R.styleable.FancyIndexer_minFontSize, mMinFontSize)
            // 最大
            mMaxFontSize = a.getDimensionPixelSize(R.styleable.FancyIndexer_maxFontSize, mMaxFontSize)
            mTipFontSize = a.getDimensionPixelSize(R.styleable.FancyIndexer_tipFontSize, mTipFontSize)
            mMaxBezierHeight = a.getDimension(R.styleable.FancyIndexer_maxBezierHeight, mMaxBezierHeight)
            mMaxBezierWidth = a.getDimension(R.styleable.FancyIndexer_maxBezierWidth, mMaxBezierWidth)
            mMaxBezierLines = a.getInteger(R.styleable.FancyIndexer_maxBezierLines, mMaxBezierLines)
            mAdditionalTipOffset = a.getDimension(R.styleable.FancyIndexer_additionalTipOffset, mAdditionalTipOffset)
            // 颜色
            mFontColor = a.getColor(R.styleable.FancyIndexer_fontColor, mFontColor)
            // 提示颜色
            mTipFontColor = a.getColor(R.styleable.FancyIndexer_tipFontColor, mTipFontColor)
            a.recycle()
        }
        mScroller = Scroller(getContext())
        mTouch.x = 0f
        mTouch.y = -10 * mMaxBezierWidth
        mBezier1 = arrayOfNulls(mMaxBezierLines)
        mBezier2 = arrayOfNulls(mMaxBezierLines)
        calculateBezierPoints()
    }

    override fun onDraw(canvas: Canvas) {
        if (ConstChar.size <= 0) {
            return
        }
        // 控件宽高
        val height = height
        val width = width
        // 单个字母高度
        val singleHeight = height / ConstChar.size.toFloat()
        var workHeight = 0
        if (mAlpha == 0) {
            return
        }
        mPaint.reset()
        var saveCount = 0
        if (mHideAnimation) {
            saveCount = canvas.save()
            canvas.saveLayerAlpha(0f, 0f, width.toFloat(), height.toFloat(), mAlpha, Canvas.ALL_SAVE_FLAG)
        }
        for (i in ConstChar.indices) {
            mPaint.color = mFontColor
            mPaint.isAntiAlias = true
            val xPos = width - mWidthOffset
            var yPos = workHeight + singleHeight / 2

            // float adjustX = adjustXPos( yPos, i == mChooseIndex );
            // 根据当前字母y的位置计算得到字体大小
            val fontSize = adjustFontSize(i, yPos)
            mPaint.textSize = fontSize.toFloat()

            // 添加一个字母的高度
            workHeight += singleHeight.toInt()

            // 绘制字母
            drawTextInCenter(canvas, ConstChar[i], xPos + ajustXPosAnimation(i, yPos), yPos)

            // 绘制的字母和当前触摸到的一致, 绘制红色被选中字母
            if (i == mChooseIndex) {
                mPaint.color = mTipFontColor
                mPaint.isFakeBoldText = true
                mPaint.textSize = mTipFontSize.toFloat()
                yPos = mTouch.y
                var pos = 0f
                println("SideBarView onDraw mAnimating =$mAnimating  mHideAnimation =$mHideAnimation")
                if (mAnimating || mHideAnimation) {
                    pos = mPointF.x
                    yPos = mPointF.y
                } else {
                    pos = xPos + ajustXPosAnimation(i, yPos) - mAdditionalTipOffset
                    mPointF.x = pos
                    mPointF.y = yPos
                }
                println("SideBarView onDraw ConstChar[i]:" + ConstChar[i] + ",pos:" + pos + ",yPos:" + yPos)
                drawTextInCenter(canvas, ConstChar[i], pos, yPos)
            }
            mPaint.reset()
        }
        if (mHideAnimation) {
            canvas.restoreToCount(saveCount)
        }
    }

    /**
     * @param canvas  画板
     * @param string  被绘制的字母
     * @param xCenter 字母的中心x方向位置
     * @param yCenter 字母的中心y方向位置
     */
    private fun drawTextInCenter(canvas: Canvas, string: String, xCenter: Float, yCenter: Float) {
        val fm = mPaint.fontMetrics
        val fontHeight = mPaint.fontSpacing
        var drawY = yCenter + fontHeight / 2 - fm.descent
        if (drawY < -fm.ascent - fm.descent) {
            drawY = -fm.ascent - fm.descent
        }
        if (drawY > height) {
            drawY = height.toFloat()
        }
        mPaint.textAlign = Paint.Align.CENTER
        canvas.drawText(string, xCenter, drawY, mPaint)
    }

    private fun adjustFontSize(i: Int, yPos: Float): Int {

        // 根据水平方向偏移量计算出一个放大的字号
        val adjustX = Math.abs(ajustXPosAnimation(i, yPos))
        return ((mMaxFontSize - mMinFontSize) * adjustX / mMaxBezierHeight).toInt() + mMinFontSize
    }

    /**
     * x 方向的向左偏移量
     *
     * @param i    当前字母的索引
     * @param yPos y方向的初始位置
     * @return
     */
    private fun ajustXPosAnimation(i: Int, yPos: Float): Float {
        var offset: Float
        if (mAnimating || mHideAnimation) {
            // 正在动画中或在做隐藏动画
            offset = mLastOffset[i]
            if (offset != 0.0f) {
                offset += mAnimationOffset
                if (offset > 0) {
                    offset = 0f
                }
            }
        } else {
            // 根据当前字母y方向位置, 计算水平方向偏移量
            offset = adjustXPos(yPos)
            // 当前触摸的x方向位置
            val xPos = mTouch.x
            var width = width - mWidthOffset

            // 字母绘制时向左偏移量 进行修正, offset需要是<=0的值
            if (offset != 0.0f && xPos > width) {
                offset += xPos - width
            }
            if (offset > 0) {
                offset = 0f
            }
            mLastOffset[i] = offset
        }
        return offset
    }

    private fun adjustXPos(yPos: Float): Float {
        val dis = yPos - mTouch.y // 字母y方向位置和触摸时y值坐标的差值, 距离越小, 得到的水平方向偏差越大
        if (dis > -mMaxBezierWidth && dis < mMaxBezierWidth) {
            // 在2个贝赛尔曲线宽度范围以内 (一个贝赛尔曲线宽度是指一个山峰的一边)
            // 第一段 曲线
            if (dis > mMaxBezierWidth / 4) {
                for (i in mMaxBezierLines - 1 downTo 1) {
                    // 从下到上, 逐个计算
                    if (dis == -mBezier1[i]!!.y) { // 落在点上
                        return mBezier1[i]!!.x
                    }

                    // 如果距离dis落在两个贝塞尔曲线模拟点之间, 通过三角函数计算得到当前dis对应的x方向偏移量
                    if (dis > -mBezier1[i]!!.y && dis < -mBezier1[i - 1]!!.y) {
                        return (dis + mBezier1[i]!!.y) * (mBezier1[i - 1]!!.x - mBezier1[i]!!.x) / (-mBezier1[i - 1]!!.y + mBezier1[i]!!.y) + mBezier1[i]!!.x
                    }
                }
                return mBezier1[0]!!.x
            }

            // 第三段 曲线, 和第一段曲线对称
            if (dis < -mMaxBezierWidth / 4) {
                for (i in 0 until mMaxBezierLines - 1) {
                    // 从上到下
                    if (dis == mBezier1[i]!!.y) { // 落在点上
                        return mBezier1[i]!!.x
                    }

                    // 如果距离dis落在两个贝塞尔曲线模拟点之间, 通过三角函数计算得到当前dis对应的x方向偏移量
                    if (dis > mBezier1[i]!!.y && dis < mBezier1[i + 1]!!.y) {
                        return (dis - mBezier1[i]!!.y) * (mBezier1[i + 1]!!.x - mBezier1[i]!!.x) / (mBezier1[i + 1]!!.y - mBezier1[i]!!.y) + mBezier1[i]!!.x
                    }
                }
                return mBezier1[mMaxBezierLines - 1]!!.x
            }

            // 第二段 峰顶曲线
            for (i in 0 until mMaxBezierLines - 1) {
                if (dis == mBezier2[i]!!.y) {
                    return mBezier2[i]!!.x
                }

                // 如果距离dis落在两个贝塞尔曲线模拟点之间, 通过三角函数计算得到当前dis对应的x方向偏移量
                if (dis > mBezier2[i]!!.y && dis < mBezier2[i + 1]!!.y) {
                    return (dis - mBezier2[i]!!.y) * (mBezier2[i + 1]!!.x - mBezier2[i]!!.x) / (mBezier2[i + 1]!!.y - mBezier2[i]!!.y) + mBezier2[i]!!.x
                }
            }
            return mBezier2[mMaxBezierLines - 1]!!.x
        }
        return 0.0f
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        println("SideBarView dispatchTouchEvent event.action=${event.action}")
        if (ConstChar.size <= 0) {
            return false
        }
        val action = event.action
        val y = event.y
        val oldmChooseIndex = mChooseIndex
        val listener = mListener
        val c = (y / height * ConstChar.size).toInt()
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (this.width > mWidthOffset) {
                    if (event.x < this.width - mWidthOffset * 2 - mMinFontSize
                            || event.x > this.width) {
                        return false
                    }
                }
                mHideWaitingHandler.removeMessages(1)
                mScroller!!.abortAnimation()
                mAnimating = false
                mHideAnimation = false
                mAlpha = 255
                mTouch.x = event.x
                mTouch.y = event.y
                if (oldmChooseIndex != c && listener != null) {
                    if (c > 0 && c < ConstChar.size) {
                        listener.onTouchLetterChanged(ConstChar[c], c)
                        mChooseIndex = c
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                mTouch.x = event.x
                mTouch.y = event.y
                invalidate()
                if (oldmChooseIndex != c && listener != null) {
                    if (c >= 0 && c < ConstChar.size) {
                        listener.onTouchLetterChanged(ConstChar[c], c)
                        mChooseIndex = c
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mTouch.x = event.x
                mTouch.y = event.y
                mScroller!!.startScroll(0, 0, mMaxBezierHeight.toInt(), 0, 500)
                mAnimating = true
                mPointF.x = width - mWidthOffset + adjustXPos(mTouch.y) - mAdditionalTipOffset
                mPointF.y = mTouch.y
                postInvalidate()
            }
        }
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller!!.computeScrollOffset()) {
            println("SideBarView computeScroll computeScrollOffset mAnimating=$mAnimating mHideAnimation=$mHideAnimation")
            if (mAnimating) {
                val x = mScroller!!.currX.toFloat()
                mAnimationOffset = x
            } else if (mHideAnimation) {
                mAlpha = 255 - mScroller!!.currX
            }
            invalidate()
        } else if (mScroller!!.isFinished) {
            println("SideBarView computeScroll isFinished mAnimating=$mAnimating mHideAnimation=$mHideAnimation")
            if (mAnimating) {
                mHideWaitingHandler.sendEmptyMessage(1)
            } else if (mHideAnimation) {
                mHideAnimation = false
                mChooseIndex = -1
                mTouch.x = -10000f
                mTouch.y = -10000f
            }
        }
    }


    /**
     * 计算出所有贝塞尔曲线上的点
     * 个数为 mMaxBezierLines * 2 = 64
     */
    private fun calculateBezierPoints() {
        val mStart = PointF() // 开始点
        val mEnd = PointF() // 结束点
        val mControl = PointF() // 控制点

        // 计算第一段红色部分 贝赛尔曲线的点
        // 开始点
        mStart.x = 0.0f
        mStart.y = -mMaxBezierWidth

        // 控制点
        mControl.x = 0.0f
        mControl.y = -mMaxBezierWidth / 2

        // 结束点
        mEnd.x = -mMaxBezierHeight / 2
        mEnd.y = -mMaxBezierWidth / 4
        mBezier1[0] = PointF()
        mBezier1[mMaxBezierLines - 1] = PointF()
        mBezier1[0]!!.set(mStart)
        mBezier1[mMaxBezierLines - 1]!!.set(mEnd)
        for (i in 1 until mMaxBezierLines - 1) {
            mBezier1[i] = PointF()
            mBezier1[i]!!.x = calculateBezier(mStart.x, mEnd.x, mControl.x, i / mMaxBezierLines.toFloat())
            mBezier1[i]!!.y = calculateBezier(mStart.y, mEnd.y, mControl.y, i / mMaxBezierLines.toFloat())
        }

        // 计算第二段蓝色部分 贝赛尔曲线的点
        mStart.y = -mMaxBezierWidth / 4
        mStart.x = -mMaxBezierHeight / 2
        mControl.y = 0.0f
        mControl.x = -mMaxBezierHeight
        mEnd.y = mMaxBezierWidth / 4
        mEnd.x = -mMaxBezierHeight / 2
        mBezier2[0] = PointF()
        mBezier2[mMaxBezierLines - 1] = PointF()
        mBezier2[0]!!.set(mStart)
        mBezier2[mMaxBezierLines - 1]!!.set(mEnd)
        for (i in 1 until mMaxBezierLines - 1) {
            mBezier2[i] = PointF()
            mBezier2[i]!!.x = calculateBezier(mStart.x, mEnd.x, mControl.x, i / mMaxBezierLines.toFloat())
            mBezier2[i]!!.y = calculateBezier(mStart.y, mEnd.y, mControl.y, i / mMaxBezierLines.toFloat())
        }
    }

    /**
     * 贝塞尔曲线核心算法
     *
     * @param start
     * @param end
     * @param control
     * @param val
     * @return 公式及动图, 维基百科: https://en.wikipedia.org/wiki/B%C3%A9zier_curve
     * 中文可参考此网站: http://blog.csdn.net/likendsl/article/details/7852658
     */
    private fun calculateBezier(start: Float, end: Float, control: Float, `val`: Float): Float {
        val s = 1 - `val`
        return start * s * s + 2 * control * s * `val` + end * `val` * `val`
    }
    private fun println(string: String){
//        LogUtils.d(string)
    }

    fun dp2px(dipValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

}