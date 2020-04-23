package com.example.tabbed.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabbed.Adapter.AdminBillAdapter
import com.example.tabbed.Backend.RetrofitClient
import com.example.tabbed.CustomerDetailActivity
import com.example.tabbed.Model.UserBill

import com.example.tabbed.R
import com.example.tabbed.Response.AdminBillResponse
import com.example.tabbed.Util.AdminBillRecyclerViewClickListener
import com.example.tabbed.Util.ClickListenerGetView
import kotlinx.android.synthetic.main.fragment_frag_admin_bill.*
import kotlinx.android.synthetic.main.list_admin_bill.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FragAdminBill(private val listener: ClickListenerGetView) : Fragment(), AdminBillRecyclerViewClickListener {
    private val TAG = "TAG FragAdminBill"
    private val adapter = AdminBillAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_admin_bill, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerViewAdminBill.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerViewAdminBill.setHasFixedSize(true)
        recyclerViewAdminBill.adapter = adapter

        /*swipeRefreshAdminBill.setOnRefreshListener {
            loadBill()
        }
        loadBill()*/

        setupButton()
    }

    private fun setupButton(){
        btnBackAdminBill.setOnClickListener {
            listener.getViewOnClick(btnBackAdminBill)
        }

        btnQueueAdminBill.setOnClickListener {
            listener.getViewOnClick(btnQueueAdminBill)
        }
    }

    private fun loadBill() {
        //swipeRefreshAdminBill.isRefreshing = true

        RetrofitClient.instance.adminGetBillAPI()
            .enqueue(object: Callback<AdminBillResponse> {
                override fun onFailure(call: Call<AdminBillResponse>, t: Throwable) {
                    //swipeRefreshAdminBill.isRefreshing = false
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                    Log.d("$TAG Failure", t.message)
                }
                override fun onResponse(call: Call<AdminBillResponse>, response: Response<AdminBillResponse>) {
                    //swipeRefreshAdminBill.isRefreshing = false
                    if(response.isSuccessful){
                        val order = response.body()?.user_bill
                        showOrder(order)
                        //Toast.makeText(activity?.applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                    else{ //response.errorBody()?.string()
                        //Toast.makeText(requireContext(), response.errorBody()?.string(), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }
    private fun showOrder(getOrder: List<UserBill>?){
        adapter.submitList(getOrder)
    }


    private var timer = Timer()
    override fun onResume() {
        super.onResume()
        timer.schedule(object : TimerTask() {
            override fun run() {
                activity!!.runOnUiThread {
                    loadBill()
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

    override fun billOnClick(view: View, billData: UserBill) {
        when(view.id) {
            R.id.btnCheckAdminBill -> {
                listener.getBillToUpdateToServer(btnCheckAdminBill, billData)
            }
        }
    }

}
