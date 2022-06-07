package com.example.integradorandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.integradorandroid.adapter.ListAdapter
import com.example.integradorandroid.retrofit.APIService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListActivity : AppCompatActivity() {
    private val list = mutableListOf<String>(
        "Educational",
        "Recreational",
        "Social",
        "Diy",
        "Charity",
        "Cooking",
        "Relaxation",
        "Music",
        "Busywork"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        val participants = getSharedPreferences("PREFS", MODE_PRIVATE).getInt("PARTICIPANTS", 1)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler)
        val adapter = ListAdapter(list) {
            getDataFromServer(participants, it.lowercase())
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    private fun getDataFromServer(participants: Int, type: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call =
                getRetrofit().create(APIService::class.java).getDataFromServer(participants, type)
            val response = call.body()
            Log.d("SERVER", response.toString())
            if (call.isSuccessful){
                val detailIntent = Intent(this@ListActivity,DetailActivity::class.java)
                detailIntent.putExtra("response",response)
                startActivity(detailIntent)
            }

        }
    }

}