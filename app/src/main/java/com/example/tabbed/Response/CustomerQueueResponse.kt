package com.example.tabbed.Response

import com.example.tabbed.Model.OrderListDetail

data class CustomerQueueResponse(
    val error: Boolean,
    val message: String,
    val total_order: Int,
    val queue_left: Int,
    val orderlist: List<OrderListDetail>
)