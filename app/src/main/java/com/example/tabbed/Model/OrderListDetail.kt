package com.example.tabbed.Model

data class OrderListDetail(
    val id_orderlist: Int,
    val id_bill: Int,
    val id_menu: Int,
    val quantity: Int,
    val menu_status: String,
    val reference: String,

    val menu: MenuModel,
    val bill: BillModel
)