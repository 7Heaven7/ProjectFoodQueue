package com.example.tabbed.Util

import android.view.View
import com.example.tabbed.Model.OrderListItem2

interface OrderRecyclerViewClickListener {
    fun orderOnClick(view: View, orderListItem2: OrderListItem2)
}