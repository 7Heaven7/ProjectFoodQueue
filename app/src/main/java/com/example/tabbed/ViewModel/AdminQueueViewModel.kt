package com.example.tabbed.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tabbed.Backend.AdminQueueRepository
import com.example.tabbed.Response.AdminGetOrderResponse

class AdminQueueViewModel : ViewModel(){
    private var repository = AdminQueueRepository()
    private var adminQueueData = MutableLiveData<AdminGetOrderResponse>()

    fun init() {
        repository = repository.getInstance()
        adminQueueData = repository.getLiveData()
        Log.d("ViewModel", "no")
    }

    fun getAdminQueueVM(): LiveData<AdminGetOrderResponse>{
        return adminQueueData
    }
}