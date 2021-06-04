package com.yh.baseui.view.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import com.yh.base.utils.DisplayUtil
import com.yh.base.utils.Util
import com.yh.baseui.R

class AlertHelper {

    class TwoBtnNoTitle(context: Context) {
        var context: Context = context
        var confirm: (() -> Unit)? = null
        var cancel: (() -> Unit)? = null
        fun show(): CustomBuildDialog {
            val dialog: CustomBuildDialog = CustomBuildDialog.CustomBuildDialogParams()
                    .setLayoutId(R.layout.layout_dialog_2button_notitle)
                    .setBackgroundResource(R.drawable.bg_white_radius12)
                    .setGravity(Gravity.CENTER)
                    .setWidth(DisplayUtil.getScreenWidth() - DisplayUtil.dp2px(96f))
                    .setViewIdOnclick(R.id.ok)
                    .setViewIdOnclick(R.id.cancel).apply {
                        confirmText?.let { setViewIdText(R.id.ok, it) }
                        content?.let { setViewIdText(R.id.content, it) }
                        cancelText?.let { setViewIdText(R.id.cancel, it) }
                    }
                    .setOnClickListener(object : CustomBuildDialog.OnClickListener {
                        override fun onClick(dialog: Dialog?, view: View?) {
                            when (view?.id) {
                                R.id.ok -> {
                                    confirm?.invoke()
                                    cancel = null;
                                    dialog?.dismiss()
                                    return
                                }
                            }
                            cancel?.invoke()
                            cancel = null
                            dialog?.dismiss()
                        }
                    })
                    .create(context)
            dialog.setOnDismissListener {
                cancel?.invoke()
                cancel = null
            }
            dialog.show()
            return dialog
        }

        fun onConfirm(confirm: () -> Unit): TwoBtnNoTitle {
            this.confirm = confirm;
            return this
        }

        fun onCancel(cancel: () -> Unit = {}): TwoBtnNoTitle {
            this.cancel = cancel;
            return this
        }

        var content: CharSequence? = null
        var confirmText: CharSequence? = null
        var cancelText: CharSequence? = null

        fun content(content: CharSequence): TwoBtnNoTitle {
            this.content = content;
            return this
        }

        fun confirmText(confirmText: CharSequence): TwoBtnNoTitle {
            this.confirmText = confirmText;
            return this
        }

        fun cancelText(cancelText: CharSequence): TwoBtnNoTitle {
            this.cancelText = cancelText;
            return this
        }
    }


    class OneBtnNoTitle(context: Context) {
        var context: Context = context
        var confirm: (() -> Unit)? = null
        var cancel: (() -> Unit)? = null
        fun show(): CustomBuildDialog {
            val dialog: CustomBuildDialog = CustomBuildDialog.CustomBuildDialogParams()
                    .setLayoutId(R.layout.layout_dialog_1button_notitle)
                    .setBackgroundResource(R.drawable.bg_white_radius12)
                    .setGravity(Gravity.CENTER)
                    .setWidth(DisplayUtil.getScreenWidth() - DisplayUtil.dp2px(96f))
                    .setViewIdOnclick(R.id.confirm).apply {
                        confirmText?.let { setViewIdText(R.id.confirm, it) }
                        content?.let { setViewIdText(R.id.content, it) }
                    }
                    .setOnClickListener(object : CustomBuildDialog.OnClickListener {
                        override fun onClick(dialog: Dialog?, view: View?) {
                            when (view?.id) {
                                R.id.confirm -> {
                                    confirm?.invoke()
                                    cancel = null;
                                    dialog?.dismiss()
                                    return
                                }
                            }
                            cancel?.invoke()
                            cancel = null
                            dialog?.dismiss()
                        }
                    })
                    .create(context)
            dialog.setOnDismissListener {
                cancel?.invoke()
                cancel = null
            }
            dialog.show()
            return dialog
        }

        fun onConfirm(confirm: () -> Unit): OneBtnNoTitle {
            this.confirm = confirm;
            return this
        }

        fun onCancel(cancel: () -> Unit): OneBtnNoTitle {
            this.cancel = cancel;
            return this
        }

        var content: CharSequence? = null
        var confirmText: CharSequence? = null

        fun content(content: CharSequence): OneBtnNoTitle {
            this.content = content;
            return this
        }

        fun confirmText(confirmText: CharSequence): OneBtnNoTitle {
            this.confirmText = confirmText;
            return this
        }
    }
}