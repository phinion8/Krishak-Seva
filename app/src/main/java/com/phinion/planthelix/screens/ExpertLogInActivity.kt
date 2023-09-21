package com.phinion.planthelix.screens

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.planthelix.ExpertMainActivity
import com.phinion.planthelix.ExpertVerifyActivity
import com.phinion.planthelix.MainActivity
import com.phinion.planthelix.R
import com.phinion.planthelix.databinding.ActivityExpertLogInBinding

class ExpertLogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpertLogInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var loadingDialog: AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpertLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        auth = Firebase.auth
        database = Firebase.firestore

        binding.signupBtn.setOnClickListener {
            startActivity(Intent(this, ExpertSignUpActivity::class.java))
        }

        //Inflate the dialog as custom view
        val messageBoxView = LayoutInflater.from(this).inflate(R.layout.loading_dialog_layout, null)

        //AlertDialogBuilder
        loadingDialog = AlertDialog.Builder(this).setView(messageBoxView)
        val loadingDialogInstance = loadingDialog.create()
        loadingDialogInstance.setCancelable(false)
        loadingDialogInstance.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        binding.signupBtn.setOnClickListener {
            val email1 = binding.signupEmail.text.toString()
            val pass1 = binding.signupPass.text.toString()
            if (editTextValidation(email1, "Email") && editTextValidation(pass1, "Password")) {


                loadingDialogInstance.show()
                //Sign in with firebase
                auth.signInWithEmailAndPassword(
                    binding.signupEmail.text.toString().trim(),
                    binding.signupPass.text.toString().trim()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success")
                            loadingDialogInstance.dismiss()

                            startActivity(Intent(this, ExpertVerifyActivity::class.java))
                            finish()

                            Toast.makeText(
                                baseContext, getString(R.string.login_success),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.exception)

                            loadingDialogInstance.dismiss()
                            Toast.makeText(
                                baseContext, task.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

            }

        }


    }

    private fun editTextValidation(string: String, field: String): Boolean {
        return if (TextUtils.isEmpty(string)) {
            Toast.makeText(this, "$field field is empty", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }


}