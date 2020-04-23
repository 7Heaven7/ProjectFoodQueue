package com.example.tabbed.Model

data class UserModel(
    val id_user: Int,
    val user: String,
    val role: String,
    var is_call: Int = -1
)