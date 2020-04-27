package com.example.tabbed.ui

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabbed.Adapter.CustomerConfirmOrderAdapter
import com.example.tabbed.Model.MenuModel
import com.example.tabbed.Model.ToOrderItem
import com.example.tabbed.Model.OrderListItem2

import com.example.tabbed.R
import com.example.tabbed.Util.ClickListenerGetView

import com.example.tabbed.Util.OrderRecyclerViewClickListener
import com.example.tabbed.ViewModel.OrderListViewModel

import kotlinx.android.synthetic.main.fragment_frag_customer_confirm_order.*

class FragCustomerConfirmOrder(private val listener: ClickListenerGetView) : Fragment(), OrderRecyclerViewClickListener {
    private val TAG = "TAG FragCustomerConfirmOrder"
    lateinit var orderViewModel: OrderListViewModel
    private val adapter = CustomerConfirmOrderAdapter(this)
    private var totalPrice = 0
    var listToSendToServer: MutableList<ToOrderItem> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_customer_confirm_order, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerViewCusConfirmOrder.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerViewCusConfirmOrder.setHasFixedSize(true)
        recyclerViewCusConfirmOrder.adapter = adapter

        orderViewModel = ViewModelProviders.of(this).get(OrderListViewModel::class.java)
        setData()


        btnSubmitCusOrder.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireContext())
                // if the dialog is cancelable
                .setCancelable(false)
                .setPositiveButton("SUBMIT", DialogInterface.OnClickListener {
                        _, _ ->
                    listener.getListToSendToServer(btnSubmitCusOrder, listToSendToServer)
                    orderViewModel.deleteAllOrders()

                })
                .setNegativeButton("CANCEL", DialogInterface.OnClickListener {
                        dialog, _ ->
                    dialog.dismiss()
                })

            val alert = dialogBuilder.create()
            alert.setTitle("CHECK YOUR LIST?")
            alert.setMessage("Please check your list before submit")
            alert.show()

        }
        btnBackCusOrder.setOnClickListener {
            listener.getViewOnClick(btnBackCusOrder)
        }

    }

    private fun setData(){
        orderViewModel.getAllOrdersVM().observe(viewLifecycleOwner, Observer<List<OrderListItem2>> {
            listToSendToServer.clear()
            adapter.submitList(it.toList())
            totalPrice = 0
            it.forEach{
                val menuData = MenuModel(it.id_menu, it.menu_name, it.image_file, it.price, it.type, it.latest_update)
                listToSendToServer.add(ToOrderItem(menuData, it.quantity))
                totalPrice += it.price*it.quantity
            }
            txtTotalPriceCusOrder.text = "Total : ${totalPrice} Baht"

        })
    }

    override fun orderOnClick(view: View, oldOrder: OrderListItem2) {
        when(view.id){
            R.id.btnSubmitCusOrder ->{
                Log.d(TAG, listToSendToServer.toString())
            }
            R.id.btnAddCusOrder ->{
                val newOrder = oldOrder.copy(quantity = oldOrder.quantity.plus(1))
                newOrder.id = oldOrder.id
                orderViewModel.update(newOrder)

            }
            R.id.btnMinusCusOrder -> {
                if (oldOrder.quantity < 2) {
                    orderViewModel.delete(oldOrder)
                } else  {
                    val newOrder = oldOrder.copy(quantity = oldOrder.quantity.minus(1))
                    newOrder.id = oldOrder.id
                    orderViewModel.update(newOrder)
                }
            }
        }
    }

}
