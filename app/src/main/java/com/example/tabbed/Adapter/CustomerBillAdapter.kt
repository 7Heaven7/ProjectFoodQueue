package com.example.tabbed.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tabbed.Model.OrderListDetail
import com.example.tabbed.Model.Orderlist
import com.example.tabbed.R
import com.example.tabbed.Util.OrderListRecyclerViewClickListener
import kotlinx.android.synthetic.main.list_customer_bill.view.*

class CustomerBillAdapter : ListAdapter<Orderlist, CustomerBillAdapter.CustomerBillViewHolder>(DIFF_CALLBACK)  {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Orderlist>() {
            override fun areItemsTheSame(oldItem: Orderlist, newItem: Orderlist): Boolean {
                val isSameItem = (oldItem.id_orderlist == newItem.id_orderlist)
                return isSameItem
            }

            override fun areContentsTheSame(oldItem: Orderlist, newItem: Orderlist): Boolean {
                val isSameContent = (oldItem.id_orderlist == newItem.id_orderlist &&
                        oldItem.id_bill == newItem.id_bill &&
                        oldItem.id_menu == newItem.id_menu &&
                        oldItem.menu_status == newItem.menu_status &&
                        oldItem.quantity == newItem.quantity)
                return isSameContent
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerBillViewHolder {
        return CustomerBillViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_customer_bill, parent, false)
        )
    }

    //override fun getItemCount() = orderData.size

    override fun onBindViewHolder(holder: CustomerBillViewHolder, position: Int)  {
        val currentOrder: Orderlist = getItem(position)
        holder.bindItem(currentOrder)
    }

    inner class CustomerBillViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        private val TAG = "CustomerBillAdapter"

        fun bindItem(order: Orderlist){
            itemView.txtMenuNameCustomerBill.text = order.menu.menu_name
            itemView.txtQntyCustomerBill.text = order.quantity.toString()
            itemView.txtPriceCustomerBill.text = "${order.menu.price}  BAHT"

        }
    }
}