package com.example.tabbed.Util

import android.view.View
import com.example.tabbed.Model.UserBill

interface AdminBillRecyclerViewClickListener {
    fun billOnClick(view: View, billData: UserBill)
}