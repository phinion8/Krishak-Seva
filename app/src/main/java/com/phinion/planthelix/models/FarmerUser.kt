package com.phinion.planthelix.models

data class FarmerUser(
   val name: String = "",
   val mobileNumber: String = "",
   val email: String = "",
   val coins: Int = -1,
   val isExpertUser: Boolean = false
)
