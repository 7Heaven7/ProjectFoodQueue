package com.example.tabbed.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tabbed.Model.Orderlist
import com.example.tabbed.R

import kotlinx.android.synthetic.main.list_admin_bill2.view.*

class SubAdminBillAdapter(private val orderlist: List<Orderlist>) : RecyclerView.Adapter<SubAdminBillAdapter.SubAdminBillViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubAdminBillViewHolder {
        return SubAdminBillViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_admin_bill2, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SubAdminBillViewHolder, position: Int)  {
        val currentOrderlist: Orderlist = orderlist[position]
        holder.bindItem(currentOrderlist)


    }

    override fun getItemCount(): Int {
        return orderlist.size
    }

    inner class SubAdminBillViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        private val TAG = "AdminQueueAdapter"

        fun bindItem(currentOrderlist : Orderlist){
            itemView.txtMenuAdminBill2.text = currentOrderlist.menu.menu_name
            itemView.txtQntyAdminBill2.text = "${currentOrderlist.quantity}"
            itemView.txtPriceAdminBill2.text = "${currentOrderlist.menu.price} BAHT"

            //button
/*            itemView.btn.setOnClickListener {
                listener.orderListOnClick(itemView.btnUpdateAdminQueue, order)
            }*/



        }
    }


}