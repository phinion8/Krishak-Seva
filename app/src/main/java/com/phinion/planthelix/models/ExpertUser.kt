package com.phinion.planthelix.models

data class ExpertUser(
    val name: String = "",
    val mobileNumber: String = "",
    val email: String = "",
    val coins: Int = -1,
    val noOfQuestionAnswered: Int = -1,
    val isVerified: Boolean = false,
    val document: String = "",
    val isExpertUser: Boolean = true
)
