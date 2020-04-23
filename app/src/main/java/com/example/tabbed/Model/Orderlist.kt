package com.example.tabbed.Model

data class Orderlist(
    val id_bill: Int,
    val id_menu: Int,
    val id_orderlist: Int,
    val menu: MenuModel,
    val menu_status: String,
    val quantity: Int,
    val reference: String
)