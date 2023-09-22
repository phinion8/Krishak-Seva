package com.phinion.planthelix.utils

import android.R
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.phinion.planthelix.databinding.WarningDialogBinding

class WarningDialog(context: Context) {

    private lateinit var warningDialog: AlertDialog
    private val warningDialogBinding =
        WarningDialogBinding.inflate(LayoutInflater.from(context))

    init {
        warningDialog = AlertDialog.Builder(context)
            .setView(warningDialogBinding.root)
            .setCancelable(false)
            .create()
        warningDialog.window!!.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent)))
    }

    fun showSuccessDialog(message: String, okOnClick:() -> Unit){
        warningDialog.show()
        warningDialogBinding.errorTitle.text = message
        warningDialogBinding.okButton.setOnClickListener {
            okOnClick()
        }
    }

    fun dismissSuccessDialog(){
        warningDialog.dismiss()
    }
}