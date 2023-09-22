package com.phinion.planthelix

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.planthelix.databinding.FragmentExpertProfileBinding
import com.phinion.planthelix.models.ExpertUser
import com.phinion.planthelix.screens.LanguageSelectionActivity
import kotlin.math.exp

class ExpertProfileFragment : Fragment() {

    private lateinit var binding: FragmentExpertProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExpertProfileBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()
        database = Firebase.firestore

        val userId = auth.currentUser?.uid

        if (userId != null) {
            database.collection("users")
                .document(userId)
                .addSnapshotListener{value, error->

                    val expertUser = value?.toObject(ExpertUser::class.java)
                    binding.userEmail.text = expertUser?.email
                    binding.userName.text = expertUser?.name

                }
        }

        binding.logOutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireContext(), LanguageSelectionActivity::class.java))
            requireActivity().finish()
        }

        return binding.root
    }

}