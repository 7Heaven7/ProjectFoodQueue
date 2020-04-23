package com.example.tabbed.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabbed.Adapter.OrderListAdapter2
import com.example.tabbed.Util.OrderRecyclerViewClickListener
import com.example.tabbed.Backend.RetrofitClient
import com.example.tabbed.Backend.SharedPrefManager
import com.example.tabbed.Model.*
import com.example.tabbed.R
import com.example.tabbed.Response.DefaultResponse
import com.example.tabbed.ViewModel.OrderListViewModel
import kotlinx.android.synthetic.main.fragment_frag_order_minilist.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class FragOrderList : Fragment(), OrderRecyclerViewClickListener {
    private val TAG = "OrderListAc"
    private val adapter = OrderListAdapter2(this)
    private var totalPrice = 0
    lateinit var orderViewModel: OrderListViewModel
    private var oldList: List<OrderListItem2> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_order_minilist, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerViewOrderList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerViewOrderList.setHasFixedSize(true)
        recyclerViewOrderList.adapter = adapter

        orderViewModel = ViewModelProviders.of(this).get(OrderListViewModel::class.java)

        setData()
    }

    private fun setData(){
        orderViewModel.getAllOrdersVM().observe(viewLifecycleOwner, Observer<List<OrderListItem2>> {

            adapter.submitList(it.toList())
            totalPrice = 0
            it.forEach{
                totalPrice += it.price*it.quantity
            }
            txtTotalPrice.text = "Total : ${totalPrice} Baht"

        })
    }

    override fun orderOnClick(view: View, oldOrder: OrderListItem2) {
        when(view.id){
            R.id.cartBtnAdd ->{
                var newOrder = oldOrder.copy(quantity = oldOrder.quantity.plus(1))
                newOrder.id = oldOrder.id
                orderViewModel.update(newOrder)
            }
            R.id.cartBtnMinus -> {
                if (oldOrder.quantity < 2) {
                    orderViewModel.delete(oldOrder)
                } else  {
                    var newOrder = oldOrder.copy(quantity = oldOrder.quantity.minus(1))
                    newOrder.id = oldOrder.id
                    orderViewModel.update(newOrder)
                }
            }
        }
    }



}
