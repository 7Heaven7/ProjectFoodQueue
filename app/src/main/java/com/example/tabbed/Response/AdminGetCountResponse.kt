package com.example.tabbed.Response

data class AdminGetCountResponse(
    val error: Boolean,
    val message: String,
    val count: Int
)