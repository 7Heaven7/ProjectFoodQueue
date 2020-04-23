package com.example.tabbed.Backend

import android.content.Context
import android.widget.Toast
import com.example.tabbed.Model.UserModel


class SharedPrefManager private constructor(private val mCtx: Context) {

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt("id_user", -1) != -1
        }

    val loginStatus: String
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("role", "NOLOG")!!
        }

    val getUser: UserModel
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return UserModel(
                sharedPreferences.getInt("id_user", -1),
                sharedPreferences.getString("user", null)!!,
                sharedPreferences.getString("role", null)!!,
                sharedPreferences.getInt("is_call", -1)
            )
        }


    fun saveUser(user: UserModel) {

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("id_user", user.id_user)
        editor.putString("user", user.user)
        editor.putString("role", user.role)

        editor.apply()

    }

    fun logoutUser() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        Toast.makeText(mCtx, "Log out success", Toast.LENGTH_LONG).show()

    }

    companion object {
        private val SHARED_PREF_NAME = "LoginStatus"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance =
                    SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

}