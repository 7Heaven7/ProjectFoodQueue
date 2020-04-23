package com.example.tabbed.Response

import com.example.tabbed.Model.UserModel

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val user: UserModel
)