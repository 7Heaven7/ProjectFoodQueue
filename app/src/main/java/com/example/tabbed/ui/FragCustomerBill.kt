package com.example.tabbed.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabbed.Adapter.CustomerBillAdapter
import com.example.tabbed.Backend.RetrofitClient
import com.example.tabbed.Backend.SharedPrefManager
import com.example.tabbed.Model.Bill
import com.example.tabbed.Model.Orderlist
import com.example.tabbed.Model.UserBill
import com.example.tabbed.R
import com.example.tabbed.Response.UserBillResponse
import com.example.tabbed.Util.ClickListenerGetView
import kotlinx.android.synthetic.main.fragment_frag_customer_bill.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class FragCustomerBill(private val listener: ClickListenerGetView) : Fragment() {
    private val TAG = "TAG FragCustomerBill"
    private val adapter = CustomerBillAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_customer_bill, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerViewCustomerBill.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerViewCustomerBill.setHasFixedSize(true)
        recyclerViewCustomerBill.adapter = adapter

        btnBackCustomerBill.setOnClickListener {
            listener.getViewOnClick(btnBackCustomerBill)
        }
    }

    private fun loadBill() {
        val user = (SharedPrefManager.getInstance(requireContext()).getUser)
        val id_usertable = user.id_user

        RetrofitClient.instance.customerGetBillAPI(id_usertable)
            .enqueue(object: Callback<UserBillResponse> {
                override fun onFailure(call: Call<UserBillResponse>, t: Throwable) {
                    //Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                    Log.d("$TAG Failure", t.message!!)
                }
                override fun onResponse(call: Call<UserBillResponse>, response: Response<UserBillResponse>) {
                    if(response.isSuccessful){
                        val order = response.body()?.user_bill
                        showOrder(order?.orderlist)
                        order?.let {
                            setView(order.total, order.reference)
                        }

                        /*if (order == null) setView(0, "-1")
                        else setView(order.user_bill.total, order.user_bill.reference)*/
                        //Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                    else{ //response.errorBody()?.string()
                        //Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()
                        Log.d("$TAG not success", response.errorBody()?.string())
                    }
                }
            })
    }

    private fun showOrder(getOrder: List<Orderlist>?){
        adapter.submitList(getOrder)
    }
    private fun setView(total: Int, ref: String){
        if (total != 0){
            txtTotalPriceCustomerBill.text = "TOTAL :  ${total} BAHT"
            txtRefCustomerBill.text = "REF :  ${ref}"
            txtTotalPriceCustomerBill.visibility = View.VISIBLE
            txtRefCustomerBill.visibility = View.VISIBLE
        } else {
            txtTotalPriceCustomerBill.visibility = View.INVISIBLE
            txtRefCustomerBill.visibility = View.INVISIBLE
        }

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
        }, 1*1000, 15*1000)
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
        timer.cancel()
        timer = Timer()
        //timer.purge()
    }

}
