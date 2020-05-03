package com.example.tabbed.Model

data class UserBill(
    val bill: Bill,
    val id_table: Int,
    val role: String,
    val user: String
)