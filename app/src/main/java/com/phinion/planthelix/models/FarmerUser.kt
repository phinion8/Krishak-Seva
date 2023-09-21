package com.phinion.planthelix.models

data class FarmerUser(
   val name: String = "",
   val email: String = "",
   val coins: Int = -1,
   val isExpertUser: Boolean = false
)
