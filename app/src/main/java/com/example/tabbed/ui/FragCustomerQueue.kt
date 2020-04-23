package com.example.tabbed.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabbed.Adapter.CustomerQueueAdapter
import com.example.tabbed.Backend.RetrofitClient
import com.example.tabbed.Backend.SharedPrefManager
import com.example.tabbed.Model.OrderListDetail

import com.example.tabbed.R
import com.example.tabbed.Response.CustomerQueueResponse
import com.example.tabbed.Util.ClickListenerGetView
import com.example.tabbed.Util.OrderListRecyclerViewClickListener
import kotlinx.android.synthetic.main.fragment_frag_customer_queue.*
import kotlinx.android.synthetic.main.list_customer_queue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FragCustomerQueue(private val listener: ClickListenerGetView) : Fragment(),
    OrderListRecyclerViewClickListener {
    private val TAG = "TAG FragCustomerQueue"
    private val adapter = CustomerQueueAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_customer_queue, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupButton()
        recyclerViewCustomerQueue.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerViewCustomerQueue.setHasFixedSize(true)
        recyclerViewCustomerQueue.adapter = adapter

        /*swiperRefreshCustomerQueue.setOnRefreshListener {
            loadOrder()
        }
        loadOrder()*/

    }

    private fun setupButton() {
        btnBillCustomerQueue.setOnClickListener {
            listener.getViewOnClick(btnBillCustomerQueue)
        }
        btnBackCustomerQueue.setOnClickListener {
            listener.getViewOnClick(btnBackCustomerQueue)
        }
    }

    private fun loadOrder() {
        //swiperRefreshCustomerQueue.isRefreshing = true

        val user = (SharedPrefManager.getInstance(requireContext()).getUser)
        val id_usertable = user.id_user

        RetrofitClient.instance.customerGetOrderAPI(id_usertable)
            .enqueue(object: Callback<CustomerQueueResponse> {
                override fun onFailure(call: Call<CustomerQueueResponse>, t: Throwable) {
                    //swiperRefreshCustomerQueue.isRefreshing = false
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                    Log.d("$TAG Failure", t.message!!)
                }
                override fun onResponse(call: Call<CustomerQueueResponse>, response: Response<CustomerQueueResponse>) {
                    //swiperRefreshCustomerQueue.isRefreshing = false
                    if(response.isSuccessful){
                        val order = response.body()?.orderlist
                        showOrder(order)
                        order?.let{
                            response.body()?.queue_left?.let { it -> setView(it) }
                        }
                        /*if (order == null) setView(-1)
                        else setView(response.body()!!.queue_left)*/
                        //Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                    else{ //response.errorBody()?.string()
                        //Toast.makeText(requireContext(), response.errorBody()?.string(), Toast.LENGTH_LONG).show()
                        Log.d("$TAG not success", response.errorBody()?.string())
                    }
                }
            })
    }
    private fun showOrder(getOrder: List<OrderListDetail>?){
        adapter.submitList(getOrder)
    }
    private fun setView(queue: Int){
        when (queue) {
            -1 -> txtQueueLeftCustomerQueue.text = "THERE IS NO ORDER"
            0 -> txtQueueLeftCustomerQueue.text = "NEXT QUEUE"
            else -> txtQueueLeftCustomerQueue.text = "QUEUE LEFT : ${queue}"
        }
    }

    private var timer = Timer()
    override fun onResume() {
        super.onResume()
        timer.schedule(object : TimerTask() {
            override fun run() {
                activity!!.runOnUiThread {
                    loadOrder()
                    Log.d(TAG, "LoadBill ")
                }
            }
        }, 500, 10*1000)
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
        timer.cancel()
        timer = Timer()
        //timer.purge()
    }


    override fun orderListOnClick(view: View, orderData: OrderListDetail) {
        when(view.id) {
            R.id.btnCancelCustomerQueue -> {
                listener.getOrderToUpdateToServer(btnCancelCustomerQueue, orderData)
            }

        }
    }

}
