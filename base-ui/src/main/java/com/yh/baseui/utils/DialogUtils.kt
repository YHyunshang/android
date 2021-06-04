package com.yh.baseui.utils

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import android.view.View
import com.yh.base.ui.recyclerView.RecyclerAdapter
import com.yh.base.utils.DisplayUtil
import com.yh.base.utils.Util
import com.yh.baseui.R
import com.yh.baseui.databinding.ItemTextCenter18dpBinding
import com.yh.baseui.databinding.ItemTextCenterBinding
import com.yh.baseui.view.dialog.CustomBuildDialog

/**
 * @description $
 * @date: 2021/4/27 3:13 PM
 * @author: zengbobo
 */
object DialogUtils {

    /**
     * 拨打电话
     *
     * @description
     * @time 2021/4/28 2:16 PM
     * @author zengbobo
     */
    fun callPhoneService(activity: Activity, title: String, phoneList: List<CharSequence>) {
        CustomBuildDialog.CustomBuildDialogParams()
                .setLayoutId(R.layout.dialog_call_phone)
                .setBackgroundResource(R.color.transparent)
                .setGravity(Gravity.BOTTOM)
                .setWidth(DisplayUtil.getScreenWidth())
                .setViewIdOnclick(R.id.tv_cancle)
                .setViewIdText(R.id.call_phone, title)
//                .setViewIdOnclick(R.id.tv_call)
                .setViewIdOnRecycler(R.id.recyclerView, object : RecyclerAdapter<CharSequence, ItemTextCenterBinding>(phoneList) {
                    override fun convert(holder: ViewHolder<ItemTextCenterBinding>, data: CharSequence, position: Int) {
                        holder.viewBinding.tvCall.text = data
                        holder.itemView.setOnClickListener {
                            Utils.callPhone(it.context, data)
                        }
                    }
                })
//                .setOnClickListener(object : CustomBuildDialog.OnClickListener {
//                    override fun onClick(dialog: Dialog?, view: View?) {
//                        when (view?.id) {
//                            R.id.tv_call -> {
//                                Utils.callPhone(view?.context, phone)
//                            }
//                        }
//                    }
//                })
                .create(activity)
                .show()
    }

    /**
     * 拨打电话
     *
     * @description
     * @time 2021/4/28 2:16 PM
     * @author zengbobo
     */
    fun callPhoneService(activity: Activity, nameList: List<CharSequence>, phoneList: List<CharSequence>) {
        CustomBuildDialog.CustomBuildDialogParams()
                .setLayoutId(R.layout.dialog_call_phone_list)
                .setBackgroundResource(R.color.transparent)
                .setGravity(Gravity.BOTTOM)
                .setWidth(DisplayUtil.getScreenWidth())
                .setViewIdOnclick(R.id.tv_cancle)
                .setViewIdOnRecycler(R.id.recyclerView, object : RecyclerAdapter<CharSequence, ItemTextCenter18dpBinding>(nameList) {
                    override fun convert(holder: ViewHolder<ItemTextCenter18dpBinding>, data: CharSequence, position: Int) {
                        holder.viewBinding.tvCall.text = "${data}    ${phoneList[position]}"
                        holder.itemView.setOnClickListener {
                            Utils.callPhone(it.context, phoneList[position])
                        }
                    }
                })
                .create(activity)
                .show()
    }

    /**
     * 针对提示类弹框：内容+2个按钮（按钮不带背景）
     *
     * @description
     * @time 2021/4/28 2:14 PM
     * @author zengbobo
     */
    fun noticeDialog(activity: Activity,
                     cancelBlock: (Dialog?) -> Unit,
                     okBlock: (Dialog?) -> Unit,
                     content: CharSequence,
                     cancel: CharSequence = activity.resources.getString(R.string.text_cancel),
                     ok: CharSequence = activity.resources.getString(R.string.text_ok)) {
        CustomBuildDialog.CustomBuildDialogParams()
                .setLayoutId(R.layout.layout_dialog_2button_notitle)
                .setGravity(Gravity.CENTER)
                .setWidth(DisplayUtil.getScreenWidth() - DisplayUtil.dp2px(96f))
                .setViewIdText(R.id.content, content)
                .setViewIdText(R.id.cancel, cancel)
                .setViewIdText(R.id.ok, ok)
                .setOnClickDismiss(false)
                .setViewIdOnclick(R.id.cancel)
                .setViewIdOnclick(R.id.ok)
                .setOnClickListener(object : CustomBuildDialog.OnClickListener {
                    override fun onClick(dialog: Dialog?, view: View?) {
                        when (view?.id) {
                            R.id.cancel -> {
                                cancelBlock(dialog)
                            }
                            R.id.ok -> {
                                okBlock(dialog)
                            }
                        }
                    }
                })
                .create(activity)
                .show()
    }

    /**
     * 针对提示类弹框：内容+2个按钮()带边框背景
     *
     * @description
     * @time 2021/4/28 2:14 PM
     * @author zengbobo
     */
    fun noticeDialog1(activity: Activity,
                      cancelBlock: (Dialog?) -> Unit,
                      okBlock: (Dialog?) -> Unit,
                      content: CharSequence,
                      cancel: CharSequence = activity.resources.getString(R.string.text_cancel),
                      ok: CharSequence = activity.resources.getString(R.string.text_ok)) {
        CustomBuildDialog.CustomBuildDialogParams()
                .setLayoutId(R.layout.layout_dialog_2button_notitle_1)
                .setGravity(Gravity.CENTER)
                .setBackgroundResource(R.color.transparent)
                .setWidth(DisplayUtil.getScreenWidth() - DisplayUtil.dp2px(76f))
                .setViewIdText(R.id.content, content)
                .setViewIdText(R.id.cancel, cancel)
                .setViewIdText(R.id.ok, ok)
                .setOnClickDismiss(false)
                .setViewIdOnclick(R.id.cancel)
                .setViewIdOnclick(R.id.ok)
                .setOnClickListener(object : CustomBuildDialog.OnClickListener {
                    override fun onClick(dialog: Dialog?, view: View?) {
                        when (view?.id) {
                            R.id.cancel -> {
                                cancelBlock(dialog)
                            }
                            R.id.ok -> {
                                okBlock(dialog)
                            }
                        }
                    }
                })
                .create(activity)
                .show()
    }
}