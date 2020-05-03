package com.example.tabbed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.tabbed.Backend.RetrofitClient
import com.example.tabbed.Backend.SharedPrefManager
import com.example.tabbed.Response.LoginResponse
import kotlinx.android.synthetic.main.activity_customer_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_login)

        setupButton()
    }

    private fun setupButton(){
        btnCusLogin.setOnClickListener {
            userlogin()
        }

        btnGoBackCusLogin.setOnClickListener {
            onBackPressed()
        }
    }

    private fun userlogin() {
        val user = editTextUsernameCusLogin.text.toString().trim()
        val password = editTextPasswordCusLogin.text.toString().trim()

        RetrofitClient.instance.userLoginAPI(user, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            response.body()?.message,
                            Toast.LENGTH_LONG
                        ).show()
                        if (!response.body()?.error!!) {
                            SharedPrefManager.getInstance(applicationContext)
                                .saveUser(response.body()?.user!!)

                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                            startActivity(intent)
                        }

                    } else {
                        Toast.makeText(
                            applicationContext,
                            response.errorBody()?.string(),
                            Toast.LENGTH_LONG
                        ).show()
                        //Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_LONG).show()
                    }

                }
            })
    }

    override fun onStart() {
        super.onStart()

        val staffRole: List<String> = listOf("Admin", "Manager", "Chef", "Waiter")
        if(staffRole.contains(SharedPrefManager.getInstance(this).loginStatus)){
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        } else if (SharedPrefManager.getInstance(this).loginStatus == "Customer") {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        } else{
            Log.d("Login",(SharedPrefManager.getInstance(this).loginStatus))
        }

    }
}
