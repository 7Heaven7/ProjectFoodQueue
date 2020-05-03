package com.example.tabbed.Adapter

import android.util.Log
import com.example.tabbed.R

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tabbed.Backend.RetrofitClient
import com.example.tabbed.Backend.SharedPrefManager
import com.example.tabbed.Model.*
import com.example.tabbed.Util.MenuRecyclerViewClickListener
import kotlinx.android.synthetic.main.list_menu.view.*


class MenuListAdapter(private val listener: MenuRecyclerViewClickListener) : ListAdapter<MenuModel, MenuListAdapter.MenuListViewHolder>(DIFF_CALLBACK)  {
    private val URL = RetrofitClient.BASE_URL
    private val TAG = "AdminBillAdapter"

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MenuModel>() {
            override fun areItemsTheSame(oldItem: MenuModel, newItem: MenuModel): Boolean {
                val isSameItem = (oldItem.id_menu == newItem.id_menu)
                return isSameItem
            }

            override fun areContentsTheSame(oldItem: MenuModel, newItem: MenuModel): Boolean {
                val isSameContent = (oldItem.menu_name == newItem.menu_name &&
                        oldItem.type == newItem.type &&
                        oldItem.price == newItem.price &&
                        oldItem.image_file == newItem.image_file &&
                        oldItem.latest_update == newItem.latest_update)
                return isSameContent
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuListViewHolder {
        return MenuListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_menu, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MenuListViewHolder, position: Int)  {
        val currentMenu: MenuModel = getItem(position)
        holder.bindItem(currentMenu)

    }

    fun getData2(order: ToOrderItem): ToOrderItem{
        return order
    }

    inner class MenuListViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        private val TAG = "MenuListAdapter"

        fun bindItem(menu: MenuModel){

            if (SharedPrefManager.getInstance(itemView.context).loginStatus != "Customer"){
                itemView.btnAdd.visibility = View.INVISIBLE
                itemView.btnMinus.visibility = View.INVISIBLE
                itemView.btnOrder.visibility = View.INVISIBLE
                itemView.txtNumber.visibility = View.INVISIBLE
            }

            var itemQuantity = 0

            itemView.txtMenuName.text = menu.menu_name
            itemView.txtPrice.text = "${menu.price} Baht"
            itemView.txtNumber.text = itemQuantity.toString()

            //image loading
            var imageURL = URL+menu.image_file
            Glide.with(itemView.context)
                .load(imageURL)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(itemView.imgFood)

            //button
            itemView.btnOrder.setOnClickListener {
                listener.menuOnClick(itemView.btnOrder, menu, itemQuantity)
                itemQuantity = 0
                itemView.txtNumber.text = itemQuantity.toString()
            }

            //+ and - quantity
            itemView.btnAdd.setOnClickListener{
                itemQuantity += 1
                itemView.txtNumber.text = itemQuantity.toString()
            }
            itemView.btnMinus.setOnClickListener {
                if(itemQuantity != 0){
                    itemQuantity -= 1
                    itemView.txtNumber.text = itemQuantity.toString()
                }
            }

            itemView.imgFood.setOnLongClickListener {
                if(SharedPrefManager.getInstance(view.context).loginStatus == "Manager") {
                    val newMenu = MenuModel(menu.id_menu, menu.menu_name, imageURL, menu.price, menu.type, menu.latest_update)
                    listener.menuOnClick(itemView.imgFood, newMenu, itemQuantity)
                }
                true
            }



        }
    }
}
