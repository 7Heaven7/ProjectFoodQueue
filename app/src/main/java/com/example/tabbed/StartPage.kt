package com.example.tabbed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tabbed.Backend.SharedPrefManager
import kotlinx.android.synthetic.main.activity_customer_login.*
import kotlinx.android.synthetic.main.activity_start_page.*

class StartPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_page)

        setupButton()
    }

    private fun setupButton(){
        role_customer_start.setOnClickListener {
            val intent = Intent(applicationContext, CustomerLogin::class.java)
            startActivity(intent)
        }

        role_admin_start.setOnClickListener {
            val intent = Intent(applicationContext, AdminLogin::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        if (SharedPrefManager.getInstance(this).loginStatus == "Admin") {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        } else if (SharedPrefManager.getInstance(this).loginStatus == "Customer") {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }else{
            Log.d("Login",(SharedPrefManager.getInstance(this).loginStatus))
        }

    }

}
