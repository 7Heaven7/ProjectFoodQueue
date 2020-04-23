package com.example.tabbed.Response

import com.example.tabbed.Model.Bill

data class UserBillResponse(
    val error: Boolean,
    val message: String,
    val count: Int,
    val user_bill: Bill?
)