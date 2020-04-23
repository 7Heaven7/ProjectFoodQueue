package com.example.tabbed.Model

data class Bill(
    val bill_status: String,
    val id_bill: Int,
    val id_user: Int,
    val orderlist: List<Orderlist>,
    val reference: String,
    val total: Int
)