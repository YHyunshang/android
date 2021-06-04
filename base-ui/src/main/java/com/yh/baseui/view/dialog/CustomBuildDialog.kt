package com.yh.baseui.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yh.base.utils.DisplayUtil.getScreenWidth
import com.yh.base.utils.StringUtils.isNullOrEmpty
import java.util.*

/**
 * @description
 *   对话框构造类：
 * 1。可以传入任何的布局资源；
 * 2。CustomBuildDialogParams 设置对话框一些参数；
 * a.如果设置文本 setViewIdText(@IdRes Integer id, String title);
 * b.设置view点击事件 setViewIdOnclick(@IdRes Integer id);
 * c.设置对话框布局  setLayoutId(int layoutId)；
 * 。。。。等等
 * @time 2021/4/7 10:59 AM
 * @author zengbobo
 */
class CustomBuildDialog(context: Context, params: CustomBuildDialogParams?) : Dialog(context), View.OnClickListener {

    private var onClickListener: OnClickListener? = null
    private var isClickDismissDialog: Boolean = true //是否点击view消失

    init {
        if (params == null || params.layoutId == 0) {
            IllegalArgumentException("CustomBuildDialog params cannot params == null || params.layoutId == 0")
        }
        val mWindow = window
        val lp = mWindow!!.attributes
        setContentView(params!!.layoutId)
        lp.width = if (params.width == 0) getScreenWidth() else params.width
        lp.gravity = params.gravity
        if (params.windowAnimations != -1) {
            lp.windowAnimations = params.windowAnimations
        }
        if (params.backgroundResource != 0) {
            mWindow.setBackgroundDrawableResource(params.backgroundResource)
        }
        setCanceledOnTouchOutside(params.canceledOnTouchOutside)
        setCancelable(params.canceledOnKeyBack)
        this.onClickListener = params.onClickListener
        this.isClickDismissDialog = params.isOnClickDismiss
        //设置文本;
        val textIt: Iterator<Int> = params.textMap.keys.iterator()
        while (textIt.hasNext()) {
            val i = textIt.next()
            setViewText(mWindow, i, params.textMap[i])
        }

        //设置是否显示;
        val visibilyIt: Iterator<Int> = params.visibilityMap.keys.iterator()
        while (visibilyIt.hasNext()) {
            val i = visibilyIt.next()
            setViewVisibility(mWindow, i, params.visibilityMap[i]!!)
        }

        //设置点击事件;
        for (id in params.onclickList) {
            setViewOnclick(mWindow, id)
        }

        //
        val drawableIt: Iterator<Int> = params.drawLeftMap.keys.iterator()
        while (drawableIt.hasNext()) {
            val i = drawableIt.next()
            setViewDrawableLeft(mWindow, i, params.drawLeftMap[i], params.drawRightMap[i])
        }

        //
        val srcIt: Iterator<Int> = params.srcMap.keys.iterator()
        while (srcIt.hasNext()) {
            val i = srcIt.next()
            setViewImageSrc(mWindow, i, params.srcMap[i]!!)
        }
        val bgIt: Iterator<Int> = params.bgMap.keys.iterator()
        while (bgIt.hasNext()) {
            val i = bgIt.next()
            setViewBg(mWindow, i, params.bgMap[i]!!)
        }

        val recyclerIt: Iterator<Int> = params.recyclerAdapterMap.keys.iterator()
        while (recyclerIt.hasNext()) {
            val i = recyclerIt.next()
            setRecyclerAdapter(mWindow, i, params.recyclerAdapterMap[i]!!)
        }
    }


    private fun setViewBg(mWindow: Window?, id: Int, bgRes: Int) {
        try {
            if (id == 0 || bgRes == 0) {
                return
            }
            val v = mWindow!!.findViewById<View>(id)
            v.setBackgroundResource(bgRes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setViewImageSrc(mWindow: Window?, id: Int, imageSrc: Int) {
        try {
            if (id == 0 || imageSrc == 0) {
                return
            }
            val v = mWindow!!.findViewById<ImageView>(id)
            v.setImageResource(imageSrc)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置文本
     *
     * @param window
     * @param id
     * @param text
     */
    private fun setViewText(window: Window?, id: Int, text: CharSequence?) {
        if (id == 0 || isNullOrEmpty(text)) {
            return
        }
        val v = window!!.findViewById<View>(id)
        if (v == null
                || v !is TextView) {
            return
        }
        v.text = text
    }

    /**
     * 设置是否显示
     *
     * @param window
     * @param id
     * @param visibility
     */
    private fun setViewVisibility(window: Window?, id: Int, visibility: Int) {
        if (id == 0) {
            return
        }
        val v = window!!.findViewById<View>(id) ?: return
        v.visibility = visibility
    }

    private fun setRecyclerAdapter(mWindow: Window?, id: Int, adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) {
        try {
            if (id == 0) {
                return
            }
            val v = mWindow!!.findViewById<RecyclerView>(id)
            v.layoutManager = LinearLayoutManager(context)
            v.adapter = adapter
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * @param window
     * @param id
     * @param drawableLeft
     * @param drawableRight
     */
    private fun setViewDrawableLeft(window: Window?, id: Int, drawableLeft: Drawable?, drawableRight: Drawable?) {
        if (id == 0
                || drawableLeft == null && drawableRight == null) {
            return
        }
        val v = window!!.findViewById<TextView>(id)
        if (v == null
                || v !is TextView) {
            return
        }
        v.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, drawableRight, null)
    }

    /**
     * 设置点击事件
     *
     * @param window
     * @param id
     */
    private fun setViewOnclick(window: Window?, id: Int) {
        if (id == 0) {
            return
        }
        val v = window!!.findViewById<View>(id) ?: return
        v.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (isClickDismissDialog) {
            dismiss()
        }
        onClickListener?.onClick(this, v)
    }

    /**
     * 对话框构建者，及对话框所要的参数；
     */
    class CustomBuildDialogParams {
        internal var layoutId: Int = 0//布局 = 0
        internal var width: Int = 0 // = 0
        internal var gravity = Gravity.BOTTOM //
        internal var windowAnimations = -1 //
        internal var canceledOnTouchOutside = true
        internal var canceledOnKeyBack = true
        internal var isOnClickDismiss: Boolean = true //是否点击view消失
        internal var backgroundResource = 0
        internal var onClickListener: OnClickListener? = null //点击事件回调
        internal val textMap: MutableMap<Int, CharSequence> = HashMap() //设置view文本
        internal val drawLeftMap: MutableMap<Int, Drawable> = HashMap()
        internal val drawRightMap: MutableMap<Int, Drawable> = HashMap()
        internal val srcMap: MutableMap<Int, Int> = HashMap() //
        internal val bgMap: MutableMap<Int, Int> = HashMap() //
        internal val visibilityMap: MutableMap<Int, Int> = HashMap() //设置view显示隐藏
        internal val onclickList: MutableList<Int> = ArrayList() //设置view是否可以点击
        internal val recyclerAdapterMap: MutableMap<Int, RecyclerView.Adapter<out RecyclerView.ViewHolder>> = HashMap() //recyclerView

        /**
         * view点击事件后是否可以消失对话框
         *
         * @param isClickDismissDialog
         * @return
         */
        fun setOnClickDismiss(isClickDismissDialog: Boolean): CustomBuildDialogParams {
            isOnClickDismiss = isClickDismissDialog
            return this
        }

        /**
         * 点击布局外是否可以隐藏对话框
         *
         * @param canceledOnTouchOutside
         * @return
         */
        fun setCanceledOnTouchOutside(canceledOnTouchOutside: Boolean): CustomBuildDialogParams {
            this.canceledOnTouchOutside = canceledOnTouchOutside
            return this
        }

        /**
         * 点击返回按钮是否可以隐藏对话框
         *
         * @param canceledOnKeyBack
         * @return
         */
        fun setCanceledOnKeyBack(canceledOnKeyBack: Boolean): CustomBuildDialogParams {
            this.canceledOnKeyBack = canceledOnKeyBack
            return this
        }

        /**
         * 对话框背景
         *
         * @param backgroundResource
         * @return
         */
        fun setBackgroundResource(backgroundResource: Int): CustomBuildDialogParams {
            this.backgroundResource = backgroundResource
            return this
        }

        /**
         * 对话框---布局
         *
         * @param layoutId
         * @return
         */
        fun setLayoutId(layoutId: Int): CustomBuildDialogParams {
            this.layoutId = layoutId
            return this
        }

        /**
         * 对话框---宽
         *
         * @param width
         * @return
         */
        fun setWidth(width: Int): CustomBuildDialogParams {
            this.width = width
            return this
        }

        /**
         * 对话框---高
         *
         * @param gravity
         * @return
         */
        fun setGravity(gravity: Int): CustomBuildDialogParams {
            this.gravity = gravity
            return this
        }

        /**
         * 对话框----显示和消失动画
         *
         * @param windowAnimations
         * @return
         */
        fun setWindowAnimations(windowAnimations: Int): CustomBuildDialogParams {
            this.windowAnimations = windowAnimations
            return this
        }

        /**
         * 设置Drawables left
         *
         * @param id
         * @param drawableLeft
         * @return
         */
        fun setViewIdDrawableLeft(@IdRes id: Int, drawableLeft: Drawable): CustomBuildDialogParams {
            drawLeftMap[id] = drawableLeft
            return this
        }

        /**
         * 设置Drawables right
         *
         * @param id
         * @param drawableRight
         * @return
         */
        fun setViewIdDrawableRight(@IdRes id: Int, drawableRight: Drawable): CustomBuildDialogParams {
            drawRightMap[id] = drawableRight
            return this
        }

        /**
         * 设置ImageView setImageResource
         *
         * @param id
         * @param imageSrc
         * @return
         */
        fun setViewIdImageSrc(@IdRes id: Int, imageSrc: Int): CustomBuildDialogParams {
            srcMap[id] = imageSrc
            return this
        }

        /**
         * 设置背景
         *
         * @param id
         * @param bgRes
         * @return
         */
        fun setViewIdBgRes(@IdRes id: Int, bgRes: Int): CustomBuildDialogParams {
            bgMap[id] = bgRes
            return this
        }

        /**
         * 设置textview文本
         *
         * @param id
         * @param title
         * @return
         */
        fun setViewIdText(@IdRes id: Int, title: CharSequence): CustomBuildDialogParams {
            textMap[id] = title
            return this
        }

        /**
         * 设置view显示
         *
         * @param id
         * @param visible
         * @return
         */
        fun setViewIdVisibility(@IdRes id: Int, visible: Int): CustomBuildDialogParams {
            visibilityMap[id] = visible
            return this
        }

        /**
         * view设置点击
         *
         * @param id
         * @return
         */
        fun setViewIdOnclick(@IdRes id: Int): CustomBuildDialogParams {
            onclickList.add(id)
            return this
        }

        /**
         * view设置点击
         *
         * @param id
         * @return
         */
        fun setViewIdOnRecycler(@IdRes id: Int, adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>): CustomBuildDialogParams {
            recyclerAdapterMap[id] = adapter
            return this
        }

        /**
         * 自定义CustomBuildDialog点击事件回调，所有设置 setViewIdOnclick(@IdRes Integer id) 后都会到这里
         *
         * @param onClickListener
         * @return
         */
        fun setOnClickListener(onClickListener: OnClickListener?): CustomBuildDialogParams {
            this.onClickListener = onClickListener
            return this
        }

        /**
         * 创建对话框实例
         *
         * @param context
         * @return
         */
        fun create(context: Context): CustomBuildDialog {
            return CustomBuildDialog(context, this)
        }

        /**
         * 显示对话框；
         *
         * @param context
         */
        fun show(context: Context) {
            val dialog = CustomBuildDialog(context, this)
            dialog.show()
        }
    }

    interface OnClickListener {
        fun onClick(dialog: Dialog?, view: View?)
    }

}