package com.example.tabbed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.tabbed.Backend.RetrofitClient
import com.example.tabbed.Backend.SharedPrefManager
import com.example.tabbed.Response.LoginResponse
import kotlinx.android.synthetic.main.activity_admin_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminLogin : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        setupButton()
    }

    private fun setupButton(){
        btnAdminLogin.setOnClickListener{
            adminlogin()
        }
        btnGoBackAdminLogin.setOnClickListener{
            onBackPressed()
        }
    }

    private fun adminlogin(){
        val admin = editTextUsernameAdminLogin.text.toString().trim()
        val password = editTextPasswordAdminLogin.text.toString().trim()

        RetrofitClient.instance.adminLoginAPI(admin, password)
            .enqueue(object: Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if(response.isSuccessful){
                        Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_LONG).show()
                        if(!response.body()?.error!!) {
                            SharedPrefManager.getInstance(applicationContext)
                                .saveUser(response.body()?.user!!)

                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                            startActivity(intent)
                        }

                    }else{
                        Toast.makeText(applicationContext, response.errorBody()?.string(), Toast.LENGTH_LONG).show()
                        //Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_LONG).show()
                    }

                }
            })
    }

    override fun onStart() {
        super.onStart()

        if(SharedPrefManager.getInstance(this).loginStatus == "Admin"){
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        else if(SharedPrefManager.getInstance(this).loginStatus == "Customer"){
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        else{
            Log.d("Login",(SharedPrefManager.getInstance(this).loginStatus == "Admin").toString())
        }


    }

}