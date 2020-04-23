package com.example.tabbed.Response

import com.example.tabbed.Model.UserBill

data class AdminBillResponse(
    val count: Int,
    val error: Boolean,
    val message: String,
    val user_bill: List<UserBill>
)