package com.example.tabbed.Model

data class MenuModel(
    val id_menu: Int,
    val menu_name: String,
    val image_file: String,
    val price: Int,
    val type: String,
    val latest_update: String? = ""
)