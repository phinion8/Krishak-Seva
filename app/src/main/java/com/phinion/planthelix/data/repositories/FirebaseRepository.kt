package com.phinion.planthelix.data.repositories

import android.content.Context
import com.google.android.gms.common.api.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.phinion.planthelix.models.CropIssueQuestion
import com.phinion.planthelix.utils.LoadingDialog
import com.phinion.planthelix.utils.Resource
import com.phinion.planthelix.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseRepository(private val context: Context) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userId = firebaseAuth.currentUser?.uid
    private val database: FirebaseFirestore = Firebase.firestore

    fun addQuestionToDatabase(cropIssueQuestion: CropIssueQuestion) = flow<Resource<Result>> {
        try {
            emit(Resource.Loading())

            val documentReference = database.collection("questions").add(cropIssueQuestion).await()
            val documentId = documentReference.id

            database.collection("questions").document(documentId)
                .update("id", documentId).await()

            if (userId != null) {

                val userDocumentReference =
                    database.collection("users").document(userId)
                        .collection("askedQuestions")
                        .add(cropIssueQuestion.copy(id = documentId)).await()
                val userDocumentId = userDocumentReference.id

                database.collection("users")
                    .document(userId)
                    .collection("askedQuestions")
                    .document(userDocumentId)
                    .update("userDocumentId", userDocumentId).await()

                database.collection("questions")
                    .document(documentId)
                    .update("userDocumentId", userDocumentId).await()

                emit(Resource.Success(Result(false, "Success", "")))
            } else {
                emit(Resource.Error("User ID is null"))
            }
        } catch (exception: Exception) {
            emit(Resource.Error("Something went wrong: ${exception.message}"))
        }
    }


    fun getQuestionList() = flow<Resource<List<CropIssueQuestion>>> {
        emit(Resource.Loading())
        try {
            val questionCollection = userId?.let {
                database.collection("users").document(it).collection("askedQuestions")
            }
            val querySnapshot = questionCollection?.get()?.await()
            val items = querySnapshot?.toObjects(CropIssueQuestion::class.java)
            if (items != null) {
                emit(Resource.Success(items))
            } else {
                emit(Resource.Error("No data available"))
            }
        } catch (e: Exception) {
            e.printStackTrace() // Log the exception for debugging
            emit(Resource.Error(e.message ?: "An error occurred..."))
        }
    }


}

data class Result(
    val isBoolean: Boolean,
    val success: String,
    val error: String
)