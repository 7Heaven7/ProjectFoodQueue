package com.example.tabbed.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tabbed.Model.OrderListDetail
import com.example.tabbed.R
import com.example.tabbed.Util.OrderListRecyclerViewClickListener
import kotlinx.android.synthetic.main.list_admin_get_order.view.*

class AdminQueueAdapter(private val listener: OrderListRecyclerViewClickListener) : ListAdapter<OrderListDetail, AdminQueueAdapter.AdminQueueViewHolder>(DIFF_CALLBACK)  {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrderListDetail>() {
            override fun areItemsTheSame(oldItem: OrderListDetail, newItem: OrderListDetail): Boolean {
                val isSameItem = (oldItem.id_orderlist == newItem.id_orderlist)
                return isSameItem
            }

            override fun areContentsTheSame(oldItem: OrderListDetail, newItem: OrderListDetail): Boolean {
                val isSameContent = (oldItem.id_orderlist == newItem.id_orderlist &&
                        oldItem.id_bill == newItem.id_bill &&
                        oldItem.reference == newItem.reference &&
                        oldItem.id_menu == newItem.id_menu &&
                        oldItem.menu_status == newItem.menu_status &&
                        oldItem.quantity == newItem.quantity)
                return isSameContent
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminQueueViewHolder {
        return AdminQueueViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_admin_get_order, parent, false)
        )
    }

    //override fun getItemCount() = orderData.size

    override fun onBindViewHolder(holder: AdminQueueViewHolder, position: Int)  {
        val currentOrder: OrderListDetail = getItem(position)
        holder.bindItem(currentOrder)
    }

    inner class AdminQueueViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        private val TAG = "AdminQueueAdapter"

        fun bindItem(order: OrderListDetail){
            itemView.txtTableAdminQueue.text = order.bill.user.user
            itemView.txtMenuAdminQueue.text = order.menu.menu_name
            itemView.txtStatusAdminQueue.text = order.menu_status
            itemView.txtQntyAdminQueue.text = order.quantity.toString()

            if (order.menu_status != "Waiting"){
                itemView.btnCancelAdminQueue.visibility = View.GONE
                //Log.d(TAG, order.toString())
            } else {
                itemView.btnCancelAdminQueue.visibility = View.VISIBLE
            }

            //button
            itemView.btnUpdateAdminQueue.setOnClickListener {
                listener.orderListOnClick(itemView.btnUpdateAdminQueue, order)
            }
            itemView.btnCancelAdminQueue.setOnClickListener{
                listener.orderListOnClick(itemView.btnCancelAdminQueue, order)
            }

        }
    }
}