package com.phinion.planthelix

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.phinion.planthelix.databinding.FragmentProfileBinding
import com.phinion.planthelix.screens.LanguageSelectionActivity

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        database = Firebase.firestore
        val userId = auth.currentUser?.uid

        binding.logOutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireContext(), LanguageSelectionActivity::class.java))
        }

        if (userId != null) {
            database.collection("users")
                .document(userId)
                .addSnapshotListener{value, error->

                    val name = value?.getString("name")
                    val email = value?.getString("email")
                    binding.userEmail.text = email
                    binding.userName.text = name

                }
        }



        return binding.root
    }


}