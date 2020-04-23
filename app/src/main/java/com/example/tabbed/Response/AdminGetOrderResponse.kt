package com.example.tabbed.Response

import com.example.tabbed.Model.OrderListDetail

data class AdminGetOrderResponse(
    val error: Boolean,
    val message: String,
    val count: Int,
    val orderlist: List<OrderListDetail>
)