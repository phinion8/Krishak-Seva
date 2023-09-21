package com.phinion.planthelix.models


const val SENT_BY_ME: String = "me"
const val SENT_BY_BOT: String = "bot"
data class Message(
    val message: String = "",
    val sendBy: String = ""
)
