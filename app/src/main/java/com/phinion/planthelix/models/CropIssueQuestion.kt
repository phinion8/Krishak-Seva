package com.phinion.planthelix.models

data class CropIssueQuestion(
    val id: String = "",
    val userId: String = "",
    val expertId: String = "",
    val image: List<String> = ArrayList(),
    val question: String = "",
    val questionDescription: String = "",
    val expertAnswer: String = ""
)
