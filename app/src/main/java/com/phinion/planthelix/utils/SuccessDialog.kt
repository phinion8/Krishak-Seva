package com.phinion.planthelix.utils

import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.google.android.play.integrity.internal.c
import com.phinion.planthelix.MainActivity
import com.phinion.planthelix.databinding.SuccessDialogLayoutBinding

class SuccessDialog(context: Context) {

    private lateinit var successDialog: AlertDialog
    private val successDialogLayoutBinding =
        SuccessDialogLayoutBinding.inflate(LayoutInflater.from(context))

    init {
        successDialog = AlertDialog.Builder(context)
            .setView(successDialogLayoutBinding.root)
            .setCancelable(false)
            .create()
        successDialog.window!!.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent)))
    }

    fun showSuccessDialog(title: String, message: String, okOnClick:() -> Unit){
        successDialogLayoutBinding.successText.text = message
        successDialogLayoutBinding.successTitle.text = title
        successDialogLayoutBinding.okBtn.setOnClickListener {
            okOnClick()
        }
        successDialog.show()
    }

    fun dismissSuccessDialog(){
        successDialog.dismiss()
    }
}