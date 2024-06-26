package com.phinion.planthelix.screens.fragments.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.phinion.planthelix.data.repositories.FirebaseRepository
import com.phinion.planthelix.models.CropIssueQuestion
import com.phinion.planthelix.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FirebaseRepository
): ViewModel() {


     fun getQuestionList() : Flow<Resource<List<CropIssueQuestion>>>{
         return repository.getQuestionList()
     }


}