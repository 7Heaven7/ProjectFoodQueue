package com.example.tabbed.Backend

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tabbed.Model.OrderListItem2

@Dao
interface OrderListDao {

    @Insert
    fun insert(orderlist: OrderListItem2)

    @Update
    fun update(orderlist: OrderListItem2)

    @Delete
    fun delete(orderlist: OrderListItem2)

    @Query("DELETE FROM orderlist_table")
    fun deleteAllOrders()

    @Query("SELECT * FROM orderlist_table ORDER BY id ASC")
    fun getAllOrders(): LiveData<List<OrderListItem2>>

    @Query("SELECT * FROM orderlist_table ORDER BY id ASC")
    fun getAllOrders2(): List<OrderListItem2>

    @Query("SELECT * FROM orderlist_table WHERE id_menu LIKE :id_menu")
    fun findExistOrder(id_menu: Int): List<OrderListItem2>

    @Query("SELECT * FROM orderlist_table")
    fun getExistOrder(): List<OrderListItem2>

    @Query("SELECT * FROM orderlist_table WHERE id_menu LIKE :id_menu")
    fun findExistOrder2(id_menu: Int): OrderListItem2
}