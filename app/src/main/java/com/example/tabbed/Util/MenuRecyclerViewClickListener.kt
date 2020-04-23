package com.example.tabbed.Util

import android.view.View
import com.example.tabbed.Model.MenuModel

interface MenuRecyclerViewClickListener {
    fun menuOnClick(view: View, menuData: MenuModel, quantity: Int)
}