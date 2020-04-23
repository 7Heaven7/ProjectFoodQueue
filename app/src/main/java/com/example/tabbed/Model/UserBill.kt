package com.example.tabbed.Model

data class UserBill(
    val bill: Bill,
    val id_user: Int,
    val role: String,
    val user: String
)