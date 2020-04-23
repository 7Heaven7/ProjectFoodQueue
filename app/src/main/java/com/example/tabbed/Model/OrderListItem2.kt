package com.example.tabbed.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orderlist_table")
data class OrderListItem2(
    var id_menu: Int = -1,
    var menu_name: String = "",
    var image_file: String = "",
    var price: Int = 0,
    var type: String = "",
    var latest_update: String? = "",
    var quantity: Int = 0
) {

    //does it matter if these are private or not?
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}