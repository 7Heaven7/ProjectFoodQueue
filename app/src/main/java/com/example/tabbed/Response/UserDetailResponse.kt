package com.example.tabbed.Response

import com.example.tabbed.Model.UserModel

data class UserDetailResponse(
    val error: Boolean,
    val message: String,
    val user: List<UserModel>
)