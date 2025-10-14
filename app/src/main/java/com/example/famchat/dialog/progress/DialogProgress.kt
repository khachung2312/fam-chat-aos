package com.example.famchat.dialog.progress

import android.content.Context
import android.view.LayoutInflater
import com.example.famchat.databinding.FcDialogProgressBinding
import com.example.famchat.dialog.base.BaseDialog
import com.example.famchat.dialog.base.BuilderDialog

class DialogProgress :
    BaseDialog<FcDialogProgressBinding, DialogProgress.ExtendBuilder>() {

    class ExtendBuilder(context: Context) : BuilderDialog(context) {
        override fun build(): BaseDialog<*, *> {
            return DialogProgress().apply {
                builder = this@ExtendBuilder
            }
        }

    }

    override fun initListener() {
        // initListener

    }

    override val viewBinding: FcDialogProgressBinding
        get() = FcDialogProgressBinding.inflate(LayoutInflater.from(context))

}