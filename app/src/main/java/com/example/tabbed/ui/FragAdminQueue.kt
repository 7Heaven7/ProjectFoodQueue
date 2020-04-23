package com.example.tabbed.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabbed.Adapter.AdminQueueAdapter
import com.example.tabbed.Backend.RetrofitClient
import com.example.tabbed.Model.OrderListDetail
import com.example.tabbed.R
import com.example.tabbed.Response.AdminGetOrderResponse
import com.example.tabbed.Util.ClickListenerGetView
import com.example.tabbed.Util.OrderListRecyclerViewClickListener
import com.example.tabbed.ViewModel.AdminQueueViewModel
import kotlinx.android.synthetic.main.fragment_frag_admin_queue.*
import kotlinx.android.synthetic.main.list_admin_get_order.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class FragAdminQueue(private val listener: ClickListenerGetView) : Fragment(), OrderListRecyclerViewClickListener {
    private val TAG = "TAG FragAdminQueue"
    private val adapter = AdminQueueAdapter(this)
    lateinit var adminQueueViewModel: AdminQueueViewModel
    private var timer = Timer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_admin_queue, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupButton()
        recyclerViewAdminQueue.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerViewAdminQueue.setHasFixedSize(true)
        recyclerViewAdminQueue.adapter = adapter

        /*swipeRefreshAdminQueue.setOnRefreshListener {
            loadOrder()
        }
        loadOrder()*/

/*        adminQueueViewModel = ViewModelProviders.of(this).get(AdminQueueViewModel::class.java)
        Log.d(TAG, "WTF DOOD")
        adminQueueViewModel.init()
        adminQueueViewModel.getAdminQueueVM().observe(viewLifecycleOwner, Observer{
            Log.d(TAG, "WTF DOODO")
            adapter.submitList(it.orderlist)
        })*/

    }

    private fun setupButton(){
        btnBackAdminQueue.setOnClickListener {
            listener.getViewOnClick(btnBackAdminQueue)
        }
        btnBillAdminQueue.setOnClickListener {
            listener.getViewOnClick(btnBillAdminQueue)
        }
    }

    private fun loadOrder() {
        //swipeRefreshAdminQueue.isRefreshing = true
        RetrofitClient.instance.adminGetOrderAPI()
            .enqueue(object: Callback<AdminGetOrderResponse> {
                override fun onFailure(call: Call<AdminGetOrderResponse>, t: Throwable) {
                    //swipeRefreshAdminQueue.isRefreshing = false
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                    Log.d("$TAG Failure", t.message!!)
                }
                override fun onResponse(call: Call<AdminGetOrderResponse>, response: Response<AdminGetOrderResponse>) {
                    //swipeRefreshAdminQueue.isRefreshing = false
                    if(response.isSuccessful){
                        val order = response.body()?.orderlist
                        showOrder(order)
                        if (order == null) setView(0)
                        else response.body()?.count?.let { setView(it) }
                        //Toast.makeText(activity?.applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                    else{ //response.errorBody()?.string()
                        //Toast.makeText(requireContext(), response.errorBody()?.string(), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    private fun showOrder(getOrder: List<OrderListDetail>?){
        adapter.submitList(getOrder)
    }
    private fun setView(count: Int){
        txtTitleAdminQueue.text = "USER'S  ORDER ($count)"
    }

    override fun onResume() {
        super.onResume()
        timer.schedule(object : TimerTask() {
            override fun run() {
                activity!!.runOnUiThread {
                    loadOrder()
                    Log.d(TAG, "LoadOrder ")
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
            R.id.btnUpdateAdminQueue -> {
                listener.getOrderToUpdateToServer(btnUpdateAdminQueue, orderData)
            }
            R.id.btnCancelAdminQueue -> {
                listener.getOrderToUpdateToServer(btnCancelAdminQueue, orderData)
            }
        }
    }
}
