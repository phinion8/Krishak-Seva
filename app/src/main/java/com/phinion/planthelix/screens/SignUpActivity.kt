package com.phinion.planthelix.screens

import android.R
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.planthelix.MainActivity
import com.phinion.planthelix.databinding.ActivitySignUpBinding
import com.phinion.planthelix.databinding.LoadingDialogLayoutBinding
import com.phinion.planthelix.models.FarmerUser

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    private lateinit var loadingDialog: AlertDialog
    private lateinit var loadingDialogBinding: LoadingDialogLayoutBinding
    private val signUpCoins = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        auth = Firebase.auth
        database = Firebase.firestore

        //Loading Dialog




        //Loading Dialog
        loadingDialogBinding  = LoadingDialogLayoutBinding.inflate(LayoutInflater.from(this))
        loadingDialog = AlertDialog.Builder(this)
            .setView(loadingDialogBinding.root)
            .setCancelable(false)
            .create()
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent)))


        binding.loginBtn2.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }



        binding.termsBtn.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.google.com"))
            startActivity(intent)
        }


        binding.signupBtn.setOnClickListener {

            if (editTextValidation(binding.signupName.text.toString().trim(), "Name")
                && editTextValidation(binding.signupEmail.text.toString().trim(), "Email")
                && editTextValidation(binding.signupPass.text.toString().trim(), "Password")
            ) {
                if (binding.checkBox.isChecked) {

                    signUpWithFirebase(
                        binding.signupName.text.toString(),
                        binding.signupEmail.text.toString().trim(),
                        binding.signupPass.text.toString().trim()
                    )
                } else {
                    Toast.makeText(
                        this,
                        getString(com.phinion.planthelix.R.string.please_agree_to_our_terms_and_conditions_string),
                        Toast.LENGTH_SHORT
                    ).show()

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

    private fun signUpWithFirebase(name: String, email: String, password: String) {



        loadingDialog.show()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    if (user != null) {
                        addDataToDatabase(user.uid, name, email, signUpCoins)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    loadingDialog.dismiss()
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, getString(com.phinion.planthelix.R.string.authentication_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                    Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }

    }

    private fun addDataToDatabase(
        uid: String,
        name: String,
        email: String,
        signUpCoins: Int
    ) {
        val db = Firebase.firestore
        val user = FarmerUser(name, email, signUpCoins)
        db.collection("users")
            .document(uid)
            .set(user)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    loadingDialog.dismiss()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    Toast.makeText(this, getString(com.phinion.planthelix.R.string.account_created_successfully_string), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    loadingDialog.dismiss()
                    Toast.makeText(
                        this,
                        getString(com.phinion.planthelix.R.string.something_went_wrong_please_try_again_later_string),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }


    }

}