package com.phinion.planthelix.utils

import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide.init
import com.google.android.play.integrity.internal.c
import com.phinion.planthelix.MainActivity
import com.phinion.planthelix.databinding.PhotoChooserDialogBinding
import com.phinion.planthelix.databinding.SuccessDialogLayoutBinding

class PhotoChooserDialog(context: Context, cameraOnClick: () -> Unit, galleryOnClick: () -> Unit) {

    private lateinit var photoPickerDialog: AlertDialog
    private val photoChooserDialogBinding =
        PhotoChooserDialogBinding.inflate(LayoutInflater.from(context))

    init {
        photoPickerDialog = AlertDialog.Builder(context)
            .setView(photoChooserDialogBinding.root)
            .setCancelable(true)
            .create()
        photoChooserDialogBinding.cameraBtn.setOnClickListener {
           cameraOnClick()
        }

        photoChooserDialogBinding.galleryBtn.setOnClickListener {
           galleryOnClick()
        }
        photoPickerDialog.window!!.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent)))
    }

    fun showPhotoPickerDialog(){
        photoPickerDialog.show()
    }

    fun dismissPhotoPickerDialog(){
        photoPickerDialog.dismiss()
    }
}