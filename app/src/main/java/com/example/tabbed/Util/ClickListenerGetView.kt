package com.example.tabbed.Util

import android.view.View
import com.example.tabbed.Model.OrderListDetail
import com.example.tabbed.Model.ToOrderItem
import com.example.tabbed.Model.UserBill

interface ClickListenerGetView {
    fun getViewOnClick(view: View)
    fun getListToSendToServer(view: View, orderListItem: List<ToOrderItem>)
    fun getOrderToUpdateToServer(view: View, order: OrderListDetail)
    fun getBillToUpdateToServer(view: View, bill: UserBill)


}