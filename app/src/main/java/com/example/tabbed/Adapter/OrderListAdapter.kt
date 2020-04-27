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
import kotlinx.android.synthetic.main.list_order.view.*


class OrderListAdapter(private val listener: OrderRecyclerViewClickListener) : ListAdapter<OrderListItem2, OrderListAdapter.OrderListHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OrderListItem2>() {
            override fun areItemsTheSame(oldItem: OrderListItem2, newItem: OrderListItem2): Boolean {
                val isSameItem = (oldItem.id == newItem.id)
                Log.d("TheSameItem", "-------${isSameItem} ${newItem.menu_name} ${newItem.id}")
                return isSameItem
            }

            override fun areContentsTheSame(oldItem: OrderListItem2, newItem: OrderListItem2): Boolean {
                val isSameContent = (oldItem.id == newItem.id &&
                        oldItem.id_menu == newItem.id_menu &&
                        oldItem.menu_name == newItem.menu_name &&
                        oldItem.price == newItem.price &&
                        oldItem.type == newItem.type &&
                        oldItem.quantity == newItem.quantity)
                Log.d("TheSameContent", "${isSameContent} ${newItem.menu_name} id = ${newItem.id} ${oldItem.quantity}:${newItem.quantity}")
                return isSameContent
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.list_order, parent, false)
        return OrderListHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrderListHolder, position: Int) {
        val currentOrder: OrderListItem2 = getItem(position)
        holder.bindItem(currentOrder)
    }

    inner class OrderListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(currentOrder: OrderListItem2){
            itemView.txtMenu.text = currentOrder.menu_name
            itemView.txtPrice.text = "${currentOrder.price} Baht"
            itemView.txtQuantity.text = "${currentOrder.quantity}"

            itemView.cartBtnAdd.setOnClickListener{
                listener.orderOnClick(itemView.cartBtnAdd, currentOrder)
            }
            itemView.cartBtnMinus.setOnClickListener {
                listener.orderOnClick(itemView.cartBtnMinus, currentOrder)
            }
        }
    }

}