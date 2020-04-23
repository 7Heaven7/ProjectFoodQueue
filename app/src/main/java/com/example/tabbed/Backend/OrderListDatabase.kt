package com.example.tabbed.Backend

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tabbed.Model.OrderListItem2

@Database(entities = [OrderListItem2::class], version = 1)
abstract class OrderListDatabase : RoomDatabase() {

    abstract fun orderListDao(): OrderListDao


    companion object {
        private var instance: OrderListDatabase? = null

        fun getInstance(context: Context): OrderListDatabase {
            return instance
                ?: synchronized(this) {
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): OrderListDatabase {
            return Room.databaseBuilder(context, OrderListDatabase::class.java, "orderlist_database")
                .build()
        }

/*        fun getInstance(context: Context): OrderListDatabase? {
            if (instance == null) {
                synchronized(OrderListDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        OrderListDatabase::class.java, "orderlist_database"
                    )
                        .fallbackToDestructiveMigration() // when version increments, it migrates (deletes db and creates new) - else it crashes
                        .addCallback(roomCallback)
                        .build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance)
                    .execute()
            }
        }*/
    }

    class PopulateDbAsyncTask(db: OrderListDatabase?) : AsyncTask<Unit, Unit, Unit>() {
        private val orderListDao = db?.orderListDao()

        override fun doInBackground(vararg p0: Unit?) {

        }
    }

}