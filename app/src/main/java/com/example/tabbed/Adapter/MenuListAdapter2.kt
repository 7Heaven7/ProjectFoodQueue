package com.example.tabbed.Adapter

import android.os.Bundle
import com.example.tabbed.R

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tabbed.Backend.SharedPrefManager
import com.example.tabbed.Model.*
import com.example.tabbed.Util.MenuRecyclerViewClickListener
import kotlinx.android.synthetic.main.list_menu.view.*

/*

class MenuListAdapter(var menus : List<MenuModel>, private val listener: MenuRecyclerViewClickListener) : RecyclerView.Adapter<MenuListAdapter.MenuViewHolder>() {
    private val URL = RetrofitClient.BASE_URL

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_menu, parent, false)
        )
    }

    override fun getItemCount() = menus.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int)  {
        holder.bindItem(menus[position], URL, listener)

    }

    fun getData(position: Int): MenuModel{
        return menus[position]
    }

    fun getData2(order: ToOrderItem): ToOrderItem{
        return order
    }


    class MenuViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        private var user: UserModel? = null
        private val TAG = "MenuListAdapter"

        fun bindItem(menu: MenuModel, URL: String, listener: MenuRecyclerViewClickListener){

            if (SharedPrefManager.getInstance(itemView.context).loginStatus == "Admin"){
                itemView.btnAdd.visibility = View.INVISIBLE
                itemView.btnMinus.visibility = View.INVISIBLE
                itemView.btnOrder.visibility = View.INVISIBLE
                itemView.txtNumber.visibility = View.INVISIBLE
            }

            var itemQuantity = 0

            itemView.txtMenuName.text = menu.menu_name
            itemView.txtPrice.text = menu.price.toString() +" Baht"
            itemView.txtNumber.text = itemQuantity.toString()

            //image loading
            val imageURL = URL+menu.image_file
            Glide.with(itemView.context)
                .load(imageURL)
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
                if(SharedPrefManager.getInstance(view.context).loginStatus == "Admin") {
                    Toast.makeText(view.context, "Admin Long click detected", Toast.LENGTH_SHORT).show()
                    val newMenu = MenuModel(menu.id_menu, menu.menu_name, imageURL, menu.price, menu.type)
                    listener.menuOnClick(itemView.imgFood, newMenu, itemQuantity)
                }
                else Toast.makeText(view.context, "User Long click detected", Toast.LENGTH_SHORT).show()
                true
            }



        }
    }
}
*/
