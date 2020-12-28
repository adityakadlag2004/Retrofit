package com.smartherd.globofly.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.smartherd.globofly.R
import com.smartherd.globofly.services.MessageService
import com.smartherd.globofly.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_welcome.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val messageService = ServiceBuilder.buildService(MessageService::class.java)
        val call = messageService.getMessage("http://10.0.2.2:7000/messages")

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val msg=response.body()
                    msg?.let { message.text =msg }

                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.printStackTrace()
            }

        })
//		 To be replaced by retrofit code
    }

    fun getStarted(view: View) {
        val intent = Intent(this, DestinationListActivity::class.java)
        startActivity(intent)
        finish()
    }
}
