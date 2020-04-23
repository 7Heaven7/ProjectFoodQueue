package com.example.tabbed.Util

import android.view.View
import com.example.tabbed.Model.OrderListDetail

interface OrderListRecyclerViewClickListener {
    fun orderListOnClick(view: View, orderData: OrderListDetail)
}