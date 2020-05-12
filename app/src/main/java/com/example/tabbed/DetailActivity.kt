package com.example.tabbed

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.tabbed.Backend.RetrofitClient
import com.example.tabbed.Backend.SharedPrefManager
import com.example.tabbed.Model.OrderListDetail
import com.example.tabbed.Model.ToOrderItem
import com.example.tabbed.Model.UserBill
import com.example.tabbed.Model.UserModel
import com.example.tabbed.Response.DefaultResponse
import com.example.tabbed.Util.ClickListenerGetView
import com.example.tabbed.ui.*
import kotlinx.android.synthetic.main.activity_customer_detail.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity(), ClickListenerGetView {
    private val fragmentManager = supportFragmentManager
    private val fragmentCustomerOrder = FragCustomerConfirmOrder(this)
    private val fragmentCustomerQueue = FragCustomerQueue(this)
    private val fragmentAdminQueue = FragAdminQueue(this)
    private val fragmentAdminBill = FragAdminBill(this)

    private var user: UserModel? = null
    private val TAG = "TAG CustomerDetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_detail)

        if (SharedPrefManager.getInstance(this).isLoggedIn) {
            user = SharedPrefManager.getInstance(this).getUser
            if (user!!.role == "Customer") {
                txtUserNameCustomerDetail.text = "TABLE : ${user!!.user}"
            } else {
                txtUserNameCustomerDetail.text = "${user!!.user}"
            }
        } else {
            val intent = Intent(applicationContext, StartPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        setupFragment()
    }

    ////////////////////////////////////////////////////////////////////////////

    private fun setupFragment(){
        if (intent.extras != null) {
            val bundle: Bundle? = intent.extras

            val message = bundle!!.getString("FRAGMENT_CODE")
            Log.d(TAG, message!!)

            when (message) {
                "ConfirmOrder" -> {
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(
                        R.id.listItemLayoutInAcCusOrder,
                        fragmentCustomerOrder
                    ).commit()
                }
                "CustomerSeeQueue" -> {
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(
                        R.id.listItemLayoutInAcCusOrder,
                        fragmentCustomerQueue
                    ).commit()
                }
                "AdminSeeQueue" -> {
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.listItemLayoutInAcCusOrder, fragmentAdminQueue)
                        .commit()
                }
                "AdminGetBill" -> {
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.listItemLayoutInAcCusOrder, fragmentAdminBill)
                        .commit()
                }
            }
        }
    }

    private fun changeCurrentFragment(fragmentCode: String){
        val fragmentTransaction = fragmentManager.beginTransaction()
        //fragmentTransaction.addToBackStack(null)
        when(fragmentCode){
            "CustomerBill" -> {
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.remove(FragCustomerQueue(this))
                fragmentTransaction.replace(R.id.listItemLayoutInAcCusOrder, FragCustomerBill(this))
                fragmentManager.popBackStack()
                fragmentTransaction.commit()
            }
            "CustomerQueue" -> {
                fragmentTransaction.remove(FragCustomerConfirmOrder(this))
                fragmentTransaction.replace(R.id.listItemLayoutInAcCusOrder, FragCustomerQueue(this))
                fragmentManager.popBackStack()
                fragmentTransaction.commit()
            }
            "AdminBill" -> {
                fragmentTransaction.remove(FragAdminQueue(this))
                fragmentTransaction.replace(R.id.listItemLayoutInAcCusOrder, FragAdminBill(this))
                fragmentManager.popBackStack()
                fragmentTransaction.commit()
            }
            "AdminQueue" -> {
                fragmentTransaction.remove(FragAdminBill(this))
                fragmentTransaction.replace(R.id.listItemLayoutInAcCusOrder, FragAdminQueue(this))
                fragmentManager.popBackStack()
                fragmentTransaction.commit()

            }
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //////////////////////////////// Server API ////////////////////////////////

    private fun sendOrderToServer(listToSendToServer: List<ToOrderItem>) {
        if (user != null && user!!.role == "Customer") {
            val user = (SharedPrefManager.getInstance(applicationContext).getUser)
            val id_user = user.id_user

            val hashMap = hashMapOf<String, Int>()
            for (i in listToSendToServer.indices) {
                hashMap.put("id_menu[$i]", listToSendToServer[i].menuData.id_menu)
                hashMap.put("id_table", id_user)
                hashMap.put("quantity[$i]", listToSendToServer[i].quantity)
            }
                //RetrofitClient.instance.orderMenuAPI2(id_menu, id_usertable, quantity)
            RetrofitClient.instance.orderMenuAPI2(hashMap)
                    .enqueue(object : Callback<DefaultResponse> {
                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                            Log.d("$TAG Order menu failed", t.message)
                        }
                        override fun onResponse(
                            call: Call<DefaultResponse>,
                            response: Response<DefaultResponse>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
                                changeCurrentFragment("CustomerQueue")

                            } else { //response.errorBody()?.string()
                                Toast.makeText(
                                    fragmentCustomerOrder.context,
                                    response.message(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    })
             //end for loop

        } else {
            Toast.makeText(fragmentCustomerOrder.context, "Something went wrong", Toast.LENGTH_LONG)
                .show()
        }
        //Toast.makeText(applicationContext, responseMessage, Toast.LENGTH_LONG).show()
        //Log.d(TAG, responseMessage)


    } //end fun

    private fun updateStatusOrderToServer(orderToSendToServer: OrderListDetail) {
        val id_orderlist = orderToSendToServer.id_orderlist
        val menu_status = orderToSendToServer.menu_status
        //oaRefresh.isRefreshing = true
        //Toast.makeText(this, "Clicked: id $id_orderlist ${orderData.menu.menu_name} ${orderData.quantity}", Toast.LENGTH_SHORT).show()

        RetrofitClient.instance.adminUpdateOrderAPI(id_orderlist, menu_status)
            .enqueue(object : Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    Log.d("$TAG admin update status failed", t.message)
                }

                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            response.body()?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    } else { //response.errorBody()?.string()
                        Toast.makeText(
                            applicationContext,
                            response.errorBody()?.string(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    private fun adminCancelOrderToServer(orderToSendToServer: OrderListDetail) {
        val id_orderlist = orderToSendToServer.id_orderlist
        val menu_status = orderToSendToServer.menu_status
        //oaRefresh.isRefreshing = true
        //Toast.makeText(this, "Clicked: id $id_orderlist ${orderData.menu.menu_name} ${orderData.quantity}", Toast.LENGTH_SHORT).show()

        RetrofitClient.instance.cancelOrderAPI(id_orderlist, menu_status)
            .enqueue(object : Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    Log.d("$TAG admin cancel failed", t.message)
                }

                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("$TAG admin cancel success", response.body()?.message)
                        Toast.makeText(
                            applicationContext,
                            response.body()?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    } else { //response.errorBody()?.string()
                        Log.d("$TAG admin cancel not success", response.body()?.message)
                        Toast.makeText(
                            applicationContext,
                            response.errorBody()?.string(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    private fun pendingBillToServer() {
        if (user != null && user!!.role == "Customer") {
            val user = (SharedPrefManager.getInstance(applicationContext).getUser)
            val id_usertable = user.id_user

            RetrofitClient.instance.customerPendingBillAPI(id_usertable)
                .enqueue(object : Callback<DefaultResponse> {
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(fragmentCustomerQueue.context, t.message, Toast.LENGTH_LONG)
                            .show()
                        Log.d("CustomerQueue failed", t.message)
                    }

                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
                            Log.d("CustomerQueue success", response.body()?.message.toString())
                            changeCurrentFragment("CustomerBill")

                        } else { //response.errorBody()?.string()
                            Toast.makeText(
                                fragmentCustomerQueue.context,
                                response.errorBody()?.string(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                })

        } else Log.d(TAG, "Something went wrong.")

    } //end fun

    private fun adminUpdatePaidBillToServer(bill: UserBill) {
        val id_bill = bill.bill.id_bill
        val bill_status = bill.bill.bill_status
        RetrofitClient.instance.adminUpdateBillAPI(id_bill, bill_status)
            .enqueue(object : Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(fragmentAdminBill.context, t.message, Toast.LENGTH_LONG).show()
                    Log.d("$TAG AdminUpdateBill failed", t.message)
                }

                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("$TAG AdminUpdateBill success", response.body()?.message.toString())
                    } else { //response.errorBody()?.string()
                        Toast.makeText(
                            fragmentAdminBill.context,
                            response.errorBody()?.string(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    ////////////////////////////////////////////////////////////////////////////
    /////////////////////////// Interface Implement ////////////////////////////

    override fun getViewOnClick(view: View) {
        when (view.id) {
            R.id.btnBackAdminBill -> {
                onBackPressed()
            }
            R.id.btnBackAdminQueue -> {
                onBackPressed()
            }
            R.id.btnBackCusOrder  -> {
                onBackPressed()
            }
            R.id.btnBackCustomerQueue -> {
                onBackPressed()
            }
            R.id.btnBackCustomerBill -> {
                onBackPressed()
            }
            R.id.btnBillCustomerQueue -> {
                val dialogBuilder = AlertDialog.Builder(this)
                    // if the dialog is cancelable
                    .setCancelable(false)
                    .setPositiveButton("YES", DialogInterface.OnClickListener { _, _ ->
                        pendingBillToServer()
                    })
                    .setNegativeButton("NO", DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                    })

                val alert = dialogBuilder.create()
                alert.setTitle("PENDING BILL")
                alert.setMessage("Are you sure?")
                alert.show()
            }
            R.id.btnBillAdminQueue -> {
                changeCurrentFragment("AdminBill")
            }
            R.id.btnQueueAdminBill -> {
                changeCurrentFragment("AdminQueue")
            }
        }
    }
    override fun getListToSendToServer(view: View, listToSendToServer: List<ToOrderItem>) {
        when (view.id) {
            R.id.btnSubmitCusOrder -> {
                sendOrderToServer(listToSendToServer)
            }
        }
    }
    override fun getOrderToUpdateToServer(view: View, orderData: OrderListDetail) {
        when (view.id) {
            R.id.btnUpdateAdminQueue -> {
                updateStatusOrderToServer(orderData)
            }
            R.id.btnCancelAdminQueue -> {
                val dialogBuilder = AlertDialog.Builder(this)
                    // if the dialog is cancelable
                    .setCancelable(false)
                    .setPositiveButton("YES", DialogInterface.OnClickListener { _, _ ->
                        adminCancelOrderToServer(orderData)
                    })
                    .setNegativeButton("NO", DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                    })

                val alert = dialogBuilder.create()
                alert.setTitle("CANCELING '${orderData.menu.menu_name}' ON '${orderData.bill.user.user}' ")
                alert.setMessage("Are you sure?")
                alert.show()

            }
            R.id.btnCancelCustomerQueue -> {
                val dialogBuilder = AlertDialog.Builder(this)
                    // if the dialog is cancelable
                    .setCancelable(false)
                    .setPositiveButton("YES", DialogInterface.OnClickListener { _, _ ->
                        adminCancelOrderToServer(orderData)
                    })
                    .setNegativeButton("NO", DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                    })

                val alert = dialogBuilder.create()
                alert.setTitle("CANCELING '${orderData.menu.menu_name}' ")
                alert.setMessage("Are you sure?")
                alert.show()
            }

        }
    }
    override fun getBillToUpdateToServer(view: View, bill: UserBill) {
        when (view.id) {
            R.id.btnCheckAdminBill -> {
                val dialogBuilder = AlertDialog.Builder(this)
                    // if the dialog is cancelable
                    .setCancelable(false)
                    .setPositiveButton("YES", DialogInterface.OnClickListener { _, _ ->
                        adminUpdatePaidBillToServer(bill)
                    })
                    .setNegativeButton("NO", DialogInterface.OnClickListener { dialog, _ ->
                        dialog.dismiss()
                    })

                val alert = dialogBuilder.create()
                alert.setTitle("UPDATE BILL")
                alert.setMessage("Are you sure?")
                alert.show()
            }
        }
    }
}

