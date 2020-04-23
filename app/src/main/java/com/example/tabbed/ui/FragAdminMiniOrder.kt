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
import com.example.tabbed.Response.AdminGetCountResponse
import com.example.tabbed.Response.AdminGetOrderResponse
import com.example.tabbed.Util.ClickListenerGetView
import com.example.tabbed.Util.OrderListRecyclerViewClickListener
import com.example.tabbed.ViewModel.AdminQueueViewModel
import kotlinx.android.synthetic.main.fragment_frag_admin_miniorder.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FragAdminMiniOrder() : Fragment() {
    private val TAG = "TAG FragAdminMiniOrder"
    private var timer = Timer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_admin_miniorder, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun loadOrder() {
        //swipeRefreshAdminQueue.isRefreshing = true
        RetrofitClient.instance.adminGetCountOrderAPI()
            .enqueue(object : Callback<AdminGetCountResponse> {
                override fun onFailure(call: Call<AdminGetCountResponse>, t: Throwable) {
                    //swipeRefreshAdminQueue.isRefreshing = false
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                    Log.d("$TAG Failure", t.message!!)
                }

                override fun onResponse(
                    call: Call<AdminGetCountResponse>,
                    response: Response<AdminGetCountResponse>
                ) {
                    //swipeRefreshAdminQueue.isRefreshing = false
                    if (response.isSuccessful) {
                        val order = response.body()!!.count
                        setView(order)
                        //Toast.makeText(activity?.applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
                    } else { //response.errorBody()?.string()
                        //Toast.makeText(requireContext(), response.errorBody()?.string(), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun setView(count: Int) {
        when (count) {
            0 -> txtAdminMiniOrder.text = "THERE IS NO ORDER"
            1 -> txtAdminMiniOrder.text = "($count) CUSTOMER IS HUNGRY"
            else -> txtAdminMiniOrder.text = "($count) CUSTOMERS ARE HUNGRY"
        }
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
        }, 500, 20 * 1000)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
        timer.cancel()
        timer = Timer()
        //timer.purge()
    }
}