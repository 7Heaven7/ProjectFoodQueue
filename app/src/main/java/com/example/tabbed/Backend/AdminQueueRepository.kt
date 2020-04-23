package com.example.tabbed.Backend

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tabbed.Model.OrderListDetail
import com.example.tabbed.Response.AdminGetOrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminQueueRepository {
    private var queueData = MutableLiveData<AdminGetOrderResponse>()
    private val TAG = "AdminQueueRepository"


        private var instance: AdminQueueRepository? = null
        fun getInstance(): AdminQueueRepository{
            if (instance == null){
                Log.d("View", "get == null")
                instance = AdminQueueRepository()
            }
            Log.d("View", "get != null")
            return instance!!
        }

//    init {
//        var instance: AdminQueueRepository? = null
//        fun getInstance(): AdminQueueRepository {
//            if (instance == null){
//                instance = AdminQueueRepository(application)
//            }
//            return instance!!
//        }
//    }

    fun getAdminQueue(): MutableLiveData<AdminGetOrderResponse>{
        RetrofitClient.instance.adminGetOrderAPI().enqueue(object : Callback<AdminGetOrderResponse>{
            override fun onFailure(call: Call<AdminGetOrderResponse>, t: Throwable) {
                Log.d("$TAG Error getting queue", t.message)
                queueData.value = null
            }
            override fun onResponse(call: Call<AdminGetOrderResponse>, response: Response<AdminGetOrderResponse>) {
                if(response.isSuccessful){
                    Log.d("$TAG ", response.body()?.message)
                    queueData.postValue(response.body())
                    queueData.value = response.body()

                }
                else{ //response.errorBody()?.string()
                    Log.d("$TAG Something occur", response.errorBody().toString())
                    queueData.value = null
                }
            }
        })
        return queueData
    }

    fun getLiveData(): MutableLiveData<AdminGetOrderResponse>{
        if(queueData.equals(null)){
            queueData = MutableLiveData<AdminGetOrderResponse>()
        }
        getAdminQueue()
        return queueData
    }
}