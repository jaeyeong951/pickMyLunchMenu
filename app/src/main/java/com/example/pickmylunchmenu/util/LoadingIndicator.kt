package com.example.pickmylunchmenu.util

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.example.pickmylunchmenu.R

class LoadingIndicator(context: Context?) : Dialog(context!!) {
    init {
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (window != null) {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        setContentView(R.layout.loading)
    }

    override fun setOnShowListener(listener: DialogInterface.OnShowListener?) {
        super.setOnShowListener(listener)
    }
}