package com.example.tabbed.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.tabbed.Model.OrderListItem2
import com.example.tabbed.Backend.OrderListRepository

class OrderListViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: OrderListRepository = OrderListRepository(application)
    private var allOrders: LiveData<List<OrderListItem2>> = repository.getAllOrdersRepo()
    //private var testLivedata: MediatorLiveData<OrderListItem2> = testLivedata.addSource(allOrders)

    fun insert(orderList: OrderListItem2) {repository.insert(orderList)}
    fun update(orderList: OrderListItem2) {repository.update(orderList)}
    fun delete(orderList: OrderListItem2) {repository.delete(orderList)}

    fun deleteAllOrders() {repository.deleteAllOrders()}
    fun getAllOrdersVM(): LiveData<List<OrderListItem2>> {return allOrders}
    fun findExistOrderVM(id: Int): List<OrderListItem2>? {return repository.findExistOrderRepo(id)}
    fun findExistOrderVM2(id: Int): List<OrderListItem2>? {return repository.findExistOrderRepo2(id)}

}