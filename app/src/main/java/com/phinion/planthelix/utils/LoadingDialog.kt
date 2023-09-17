package com.phinion.planthelix.utils

import android.R
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.phinion.planthelix.databinding.LoadingDialogLayoutBinding

class LoadingDialog(private val context: Context) {

    private lateinit var loadingDialog: AlertDialog

    init {
       val loadingDialogBinding  = LoadingDialogLayoutBinding.inflate(LayoutInflater.from(context))
        loadingDialog = AlertDialog.Builder(context)
            .setView(loadingDialogBinding.root)
            .setCancelable(false)
            .create()
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent)))
    }

}