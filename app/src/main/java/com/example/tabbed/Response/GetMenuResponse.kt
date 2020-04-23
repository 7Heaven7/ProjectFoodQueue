package com.example.tabbed.Response

import com.example.tabbed.Model.MenuModel

data class GetMenuResponse(
    val error: Boolean,
    val menu: List<MenuModel>,
    val message: String
)