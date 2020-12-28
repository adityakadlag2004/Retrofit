package com.smartherd.globofly.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartherd.globofly.R
import com.smartherd.globofly.helpers.DestinationAdapter
import com.smartherd.globofly.models.Destination
import com.smartherd.globofly.services.DestinationService
import com.smartherd.globofly.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_destiny_create.*
import kotlinx.android.synthetic.main.activity_destiny_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DestinationListActivity : AppCompatActivity() {

    private val TAG = "DestinationListActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destiny_list)


        fab.setOnClickListener {
            val intent = Intent(this@DestinationListActivity, DestinationCreateActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        loadDestinations()
    }

    private fun loadDestinations() {


        val destinationService = ServiceBuilder.buildService(DestinationService::class.java)

        val hash = hashMapOf<String, String>()

        val requestCall = destinationService.getDestinationList(hash)

        requestCall.enqueue(object : Callback<List<Destination>> {
            override fun onResponse(
                call: Call<List<Destination>>,
                response: Response<List<Destination>>
            ) {
                if (response.isSuccessful) {
                    val list = response.body()!!
                    destiny_recycler_view.layoutManager = LinearLayoutManager(applicationContext)
                    Log.d(TAG, "onResponse: ${list.toString()}")
                    val adapter = DestinationAdapter(list)
                    destiny_recycler_view.adapter = adapter
                    adapter.notifyDataSetChanged()
                } else {
                    Log.d(TAG, "onFailure:  failed ${response.message()}")
                    Toast.makeText(
                        applicationContext,
                        "${response.errorBody()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Destination>>, t: Throwable) {
                Log.d(TAG, "onFailure:  failed $call")
                t.printStackTrace()
            }

        })
    }
}
