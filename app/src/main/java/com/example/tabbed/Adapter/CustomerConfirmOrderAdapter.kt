package com.example.tabbed.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tabbed.Model.OrderListItem2
import com.example.tabbed.R
import com.example.tabbed.Util.OrderRecyclerViewClickListener
import kotlinx.android.synthetic.main.list_cus_confirmorder.view.*

class CustomerConfirmOrderAdapter(private val listener: OrderRecyclerViewClickListener) : ListAdapter<OrderListItem2, CustomerConfirmOrderAdapter.CustomerConfirmOrderAdapterListHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrderListItem2>() {
            override fun areItemsTheSame(oldItem: OrderListItem2, newItem: OrderListItem2): Boolean {
                val isSameItem = (oldItem.id == newItem.id)
                Log.d("CustomerConfirmOrderAdapter", "SameItem: ${isSameItem} ${newItem.menu_name} ${newItem.id}")
                return isSameItem
            }

            override fun areContentsTheSame(oldItem: OrderListItem2, newItem: OrderListItem2): Boolean {
                val isSameContent = (oldItem.id == newItem.id &&
                        oldItem.id_menu == newItem.id_menu &&
                        oldItem.menu_name == newItem.menu_name &&
                        oldItem.price == newItem.price &&
                        oldItem.type == newItem.type &&
                        oldItem.quantity == newItem.quantity)
                Log.d("CustomerConfirmOrderAdapter", "SameContent: ${isSameContent} ${newItem.menu_name} id = ${newItem.id} ${oldItem.quantity}:${newItem.quantity}")
                return isSameContent
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerConfirmOrderAdapterListHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.list_cus_confirmorder, parent, false)
        return CustomerConfirmOrderAdapterListHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomerConfirmOrderAdapterListHolder, position: Int) {
        val currentOrder: OrderListItem2 = getItem(position)
        holder.bindItem(currentOrder)
    }

    inner class CustomerConfirmOrderAdapterListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(currentOrder: OrderListItem2){
            itemView.txtMenuNameCusOrder.text = currentOrder.menu_name
            itemView.txtPriceCusOrder.text = "${currentOrder.price}  BAHT"
            itemView.txtQuantityCusOrder.text = "${currentOrder.quantity}"

            itemView.btnAddCusOrder.setOnClickListener{
                listener.orderOnClick(itemView.btnAddCusOrder, currentOrder)
            }
            itemView.btnMinusCusOrder.setOnClickListener {
                listener.orderOnClick(itemView.btnMinusCusOrder, currentOrder)
            }
        }
    }

}