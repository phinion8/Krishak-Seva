package com.phinion.planthelix.screens.ask_question_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phinion.planthelix.data.repositories.FirebaseRepository
import com.phinion.planthelix.data.repositories.Result
import com.phinion.planthelix.models.CropIssueQuestion
import com.phinion.planthelix.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AskQuestionViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

     fun addQuestionToDatabase(cropIssueQuestion: CropIssueQuestion): Flow<Resource<Result>> {
        return firebaseRepository.addQuestionToDatabase(cropIssueQuestion)
    }

}