package com.smartherd.globofly.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smartherd.globofly.R
import com.smartherd.globofly.helpers.SampleData
import com.smartherd.globofly.models.Destination
import com.smartherd.globofly.services.DestinationService
import com.smartherd.globofly.services.ServiceBuilder
import kotlinx.android.synthetic.main.activity_destiny_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DestinationDetailActivity : AppCompatActivity() {

    private val TAG = "DestinationDetailActivi"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destiny_detail)

        setSupportActionBar(detail_toolbar)
        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle? = intent.extras

        if (bundle?.containsKey(ARG_ITEM_ID)!!) {

            val id = intent.getIntExtra(ARG_ITEM_ID, 0)

            loadDetails(id)

            initUpdateButton(id)

            initDeleteButton(id)
        }
    }

    private fun loadDetails(id: Int) {

        // To be replaced by retrofit code


        val service = ServiceBuilder.buildService(DestinationService::class.java)

        val call = service.getDestination(id)

        call.enqueue(object : Callback<Destination> {
            override fun onResponse(call: Call<Destination>, response: Response<Destination>) {
                if (response.isSuccessful) {
                    val destination = response.body()!!
                    destination.let {
                        et_city.setText(destination.city)
                        et_description.setText(destination.description)
                        et_country.setText(destination.country)

                        collapsing_toolbar.title = destination.city
                    }
                }
            }

            override fun onFailure(call: Call<Destination>, t: Throwable) {
                t.printStackTrace()
                Log.d(TAG, "onFailure: ${t.toString()}")
            }

        })

    }

    private fun initUpdateButton(id: Int) {

        btn_update.setOnClickListener {

            val city = et_city.text.toString()
            val description = et_description.text.toString()
            val country = et_country.text.toString()

            // To be replaced by retrofit code
            val destination = Destination()
            destination.id = id
            destination.city = city
            destination.description = description
            destination.country = country

            val service = ServiceBuilder.buildService(DestinationService::class.java)
            val call = service.updateDestination(id, city, description, country)

            call.enqueue(object : Callback<Destination> {
                override fun onResponse(call: Call<Destination>, response: Response<Destination>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Updated", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<Destination>, t: Throwable) {
                    Log.d(TAG, "onFailure: $t")
                }

            })
        }
    }

    private fun initDeleteButton(id: Int) {

        btn_delete.setOnClickListener {

            val service = ServiceBuilder.buildService(DestinationService::class.java)
            val call = service.deleteDestination(id)

            call.enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "onResponse: Done")
                        finish()
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.d(TAG, "onFailure: $t")
                }

            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            navigateUpTo(Intent(this, DestinationListActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        const val ARG_ITEM_ID = "item_id"
    }
}
