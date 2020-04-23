package com.example.tabbed.Model

data class BillModel(
    val id_bill: Int,
    val id_usertable: Int,
    val total: Int,
    val reference: String,
    val bill_status: String,
    val created_at: String,

    val user: UserModel
)