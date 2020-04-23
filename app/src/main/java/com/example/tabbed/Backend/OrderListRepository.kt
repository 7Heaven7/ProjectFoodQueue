package com.example.tabbed.Backend

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.tabbed.Model.OrderListItem2

class OrderListRepository(application: Application) {
    private var orderListDao: OrderListDao
    private var allLists: LiveData<List<OrderListItem2>>

    init {
        val database: OrderListDatabase = OrderListDatabase.getInstance(
            application.applicationContext
        )
        orderListDao = database.orderListDao()
        allLists = orderListDao.getAllOrders()

    }

    fun insert(orderListItem: OrderListItem2) {InsertOrderAsyncTask(orderListDao).execute(orderListItem)}
    fun update(orderListItem: OrderListItem2) {UpdateOrderAsyncTask(orderListDao).execute(orderListItem)}
    fun delete(orderListItem: OrderListItem2) {DeleteOrderAsyncTask(orderListDao).execute(orderListItem)}
    fun deleteAllOrders() {DeleteAllOrdersAsyncTask(orderListDao).execute()}

    fun getAllOrdersRepo(): LiveData<List<OrderListItem2>> {return allLists}
    fun findExistOrderRepo(id: Int): List<OrderListItem2>{return orderListDao.findExistOrder(id)}
    fun findExistOrderRepo2(id: Int): List<OrderListItem2>? {
        val menu = findExistOrderAsyncTask2(orderListDao).execute(id)
        return menu.get()
    }


    companion object {
        private class InsertOrderAsyncTask(orderDao: OrderListDao) : AsyncTask<OrderListItem2, Unit, Unit>() {
            val orderDao = orderDao

            override fun doInBackground(vararg p0: OrderListItem2?) {
                orderDao.insert(p0[0]!!)
            }
        }

        private class UpdateOrderAsyncTask(orderDao: OrderListDao) : AsyncTask<OrderListItem2, Unit, Unit>() {
            val orderDao = orderDao

            override fun doInBackground(vararg p0: OrderListItem2?) {
                orderDao.update(p0[0]!!)
            }
        }

        private class DeleteOrderAsyncTask(orderDao: OrderListDao) : AsyncTask<OrderListItem2, Unit, Unit>() {
            val orderDao = orderDao

            override fun doInBackground(vararg p0: OrderListItem2?) {
                orderDao.delete(p0[0]!!)
            }
        }

        private class DeleteAllOrdersAsyncTask(orderDao: OrderListDao) : AsyncTask<Unit, Unit, Unit>() {
            val orderDao = orderDao

            override fun doInBackground(vararg p0: Unit?) {
                orderDao.deleteAllOrders()
            }
        }

        private class findExistOrderAsyncTask(orderDao: OrderListDao) : AsyncTask<Int, Unit, Unit>() {
            val orderDao = orderDao

            override fun doInBackground(vararg p0: Int?) {
                orderDao.findExistOrder(p0[0]!!)
            }
        }

        private class findExistOrderAsyncTask2(orderDao: OrderListDao) : AsyncTask<Int, Unit, List<OrderListItem2>>() {
            val orderDao = orderDao

            override fun doInBackground(vararg p0: Int?): List<OrderListItem2>? {
                val result = orderDao.findExistOrder(p0[0]!!)
                return result

            }
        }
    }
}