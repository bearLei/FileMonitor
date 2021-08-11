package com.example.junkanalyse

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.widget.TextView


/**
 * 弹窗扩展
 */
object DialogExt {

    fun Activity.showDialog(iAction: IAction) {
        val dialog = Dialog(this)
        val layout = LayoutInflater.from(this).inflate(R.layout.dialog_notify, null)
        layout.findViewById<TextView>(R.id.cancel).setOnClickListener {
            dialog.dismiss()
            iAction.cancel()
        }
        layout.findViewById<TextView>(R.id.ok).setOnClickListener {
            dialog.dismiss()
            iAction.ok()
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        dialog.setContentView(layout)
    }

    interface IAction{
        fun cancel()
        fun ok()
    }
}