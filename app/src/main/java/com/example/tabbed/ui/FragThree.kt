package com.example.tabbed.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabbed.Adapter.MenuListAdapter
import com.example.tabbed.Backend.RetrofitClient
import com.example.tabbed.Model.MenuModel
import com.example.tabbed.Model.ToOrderItem
import com.example.tabbed.Model.OrderListItem2

import com.example.tabbed.R
import com.example.tabbed.Response.GetMenuResponse
import com.example.tabbed.Util.MenuRecyclerViewClickListener
import com.example.tabbed.ViewModel.OrderListViewModel
import kotlinx.android.synthetic.main.fragment_frag_two.*
import kotlinx.android.synthetic.main.list_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragThree(private val listener: MenuRecyclerViewClickListener) : Fragment(), MenuRecyclerViewClickListener {
    private val TAG = "TAG FragThree"
    private val type = "Dessert"
    private val adapter = MenuListAdapter(this)
    lateinit var orderViewModel: OrderListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_two, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerViewMenus.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewMenus.setHasFixedSize(true)
        recyclerViewMenus.adapter = adapter

        loadMenus()

        orderViewModel = ViewModelProviders.of(this).get(OrderListViewModel::class.java)
    }

    private fun loadMenus(){
        RetrofitClient.instance.getMenusAPI2(type)
            .enqueue(object: Callback<GetMenuResponse> {
                override fun onFailure(call: Call<GetMenuResponse>, t: Throwable) {
                    Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                    Log.d("$TAG failed getting menus", t.message)
                }
                override fun onResponse(call: Call<GetMenuResponse>, response: Response<GetMenuResponse>) {
                    if(response.isSuccessful){
                        val retrievedMenus = response.body()?.menu
                        showOrder(retrievedMenus)
                        //Toast.makeText(activity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                    else{ //response.errorBody()?.string()
                        //Toast.makeText(activity, response.errorBody()?.string(), Toast.LENGTH_LONG).show()
                    }
                }

            })
    }

    private fun showOrder(menu: List<MenuModel>?){
        adapter.submitList(menu)
    }

    private fun orderMenuClick(order: ToOrderItem) {
        //val order = adapter.getData2(order)

        //check quantity if > 0 get new order
        if(order.quantity == 0){
            Toast.makeText(requireContext(), "Cannot order with 0 ${order.menuData.menu_name}.", Toast.LENGTH_SHORT).show()
        }
        //check if order the same item
        else {
            val existOrder = orderViewModel.findExistOrderVM2(order.menuData.id_menu)
            //new item
            if (existOrder.isNullOrEmpty()) {
                val _order = OrderListItem2(
                    order.menuData.id_menu,
                    order.menuData.menu_name,
                    order.menuData.image_file,
                    order.menuData.price,
                    order.menuData.type,
                    order.menuData.latest_update,
                    order.quantity
                )
                orderViewModel.insert(_order)
                Toast.makeText(requireContext(), "${order.quantity} ${order.menuData.menu_name} added to list.", Toast.LENGTH_SHORT).show()

            } else{
                //same item
                existOrder[0].quantity += order.quantity
                orderViewModel.update(existOrder[0])
                Toast.makeText(requireContext(), "${order.quantity} ${order.menuData.menu_name} added to list.", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun menuOnClick(view: View, menuData: MenuModel, quantity: Int) {
        when(view.id){
            R.id.btnOrder -> {
                val order = ToOrderItem(menuData, quantity)
                orderMenuClick(order)
            }
            R.id.imgFood -> {
                listener.menuOnClick(imgFood, menuData, quantity)
            }
        }

    }

}

