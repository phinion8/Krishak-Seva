package com.phinion.planthelix.models

data class ProductItem(
    val id: String = "",
    val title: String = "",
    val price: Long = -1,
    val productImage: String = "",
    val productDescription: String = "",
    val category: String="",
    val size: ArrayList<String> = ArrayList(),
    val shop: String = "",
    val shopImage: String = "",
    val distance: String = "",
    val shopPhoneNumber: String = ""
)
