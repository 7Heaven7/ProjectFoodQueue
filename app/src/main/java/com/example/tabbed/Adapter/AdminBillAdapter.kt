package com.example.tabbed.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.AnimationUtilsCompat
import com.example.tabbed.Model.UserBill
import com.example.tabbed.R
import com.example.tabbed.Util.AdminBillRecyclerViewClickListener
import com.google.android.material.animation.AnimationUtils
import kotlinx.android.synthetic.main.list_admin_bill.view.*

class AdminBillAdapter(private val listener: AdminBillRecyclerViewClickListener) : ListAdapter<UserBill, AdminBillAdapter.AdminBillViewHolder>(DIFF_CALLBACK)  {
    private val TAG = "AdminBillAdapter"
    private val viewPool = RecyclerView.RecycledViewPool()

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserBill>() {
            override fun areItemsTheSame(oldItem: UserBill, newItem: UserBill): Boolean {
                val isSameItem = (oldItem.bill.id_bill == newItem.bill.id_bill)
                return isSameItem
            }

            override fun areContentsTheSame(oldItem: UserBill, newItem: UserBill): Boolean {
                val isSameContent = (oldItem.bill.id_bill == newItem.bill.id_bill &&
                        oldItem.bill.bill_status == newItem.bill.bill_status &&
                        oldItem.bill.reference == newItem.bill.reference &&
                        oldItem.bill.orderlist == newItem.bill.orderlist &&
                        oldItem.bill.total == newItem.bill.total &&
                        oldItem.id_user == newItem.id_user)
                return isSameContent
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminBillViewHolder {
        return AdminBillViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_admin_bill, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AdminBillViewHolder, position: Int)  {
        val currentBill: UserBill = getItem(position)
        holder.bindItem(currentBill)

        val childLayoutManager = LinearLayoutManager(holder.itemView.recyclerViewAdminBillMain.context, RecyclerView.VERTICAL, false)
        holder.itemView.recyclerViewAdminBillMain.apply {
            layoutManager = childLayoutManager
            adapter = SubAdminBillAdapter(currentBill.bill.orderlist)
            setRecycledViewPool(viewPool)
        }

    }

    inner class AdminBillViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        private val TAG = "AdminQueueAdapter"

        fun bindItem(userBill: UserBill){
            itemView.txtTableAdminBill.text = "TABLE : ${userBill.user}"
            itemView.txtTotalAdminBill.text = "${userBill.bill.total} BAHT"
            itemView.txtRefAdminBill.text = "REF  ${userBill.bill.reference}"
            itemView.imgExpandAdminBill.setOnClickListener {
                //show
                if (itemView.recyclerViewAdminBillMain.visibility == View.GONE){
                    val animation = android.view.animation.AnimationUtils.loadAnimation(itemView.recyclerViewAdminBillMain.context, R.anim.slide_down)
                    itemView.recyclerViewAdminBillMain.visibility = View.VISIBLE
                    itemView.recyclerViewAdminBillMain.startAnimation(animation)

                    val animation2 = android.view.animation.AnimationUtils.loadAnimation(itemView.layoutTitleAdminBill.context, R.anim.rotateninety)
                    itemView.imgExpandAdminBill.startAnimation(animation2)
                    itemView.imgExpandAdminBill.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp)

                } //hide
                else {
                    val animation = android.view.animation.AnimationUtils.loadAnimation(itemView.recyclerViewAdminBillMain.context, R.anim.slide_up)
                    itemView.recyclerViewAdminBillMain.visibility = View.GONE
                    itemView.recyclerViewAdminBillMain.startAnimation(animation)

                    val animation2 = android.view.animation.AnimationUtils.loadAnimation(itemView.layoutTitleAdminBill.context, R.anim.rotatebackninety)
                    itemView.imgExpandAdminBill.startAnimation(animation2)
                    itemView.imgExpandAdminBill.setBackgroundResource(R.drawable.ic_keyboard_arrow_right_black_24dp)

                }
            }

            itemView.btnCheckAdminBill.setOnClickListener {
                listener.billOnClick(itemView.btnCheckAdminBill, userBill)
            }
        }
    }
}