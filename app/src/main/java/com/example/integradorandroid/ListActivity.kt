package com.example.integradorandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
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
    private val randomActivity ="Random"
    private val list = mutableListOf(
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
            if(participants > 0)
                getActivityByParticipantsAndType(participants, it)
            else
                getActivityByType(it)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        selectRandomActivity()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    private fun getActivityByType(type: String)
    {
        val typeToLower =type.lowercase()
        CoroutineScope(Dispatchers.IO).launch {
            val call =
                getRetrofit().create(APIService::class.java).getActivityByType(typeToLower)
            val response = call.body()
            Log.d("SERVER", response.toString())
            if (call.isSuccessful && response?.key!=null){
                val detailIntent = Intent(this@ListActivity,DetailActivity::class.java)
                detailIntent.putExtra("response",response)
                detailIntent.putExtra("activitySelected", type)
                detailIntent.putExtra("apiCall", ApiCall.TYPE_ACTIVITY_CALL)
                startActivity(detailIntent)
            }

        }
    }

    private fun getActivityByParticipantsAndType(participants: Int, type: String) {
        val typeToLower =type.lowercase()
        CoroutineScope(Dispatchers.IO).launch {
            val call =
                getRetrofit().create(APIService::class.java).getActivityByParticipantsAndType(participants, typeToLower)
            val response = call.body()
            Log.d("SERVER", response.toString())
            if (call.isSuccessful && response?.key!=null){
                val detailIntent = Intent(this@ListActivity,DetailActivity::class.java)
                detailIntent.putExtra("response",response)
                detailIntent.putExtra("activitySelected", type)
                detailIntent.putExtra("apiCall", ApiCall.TYPE_AND_PARTICIPANTS_CALL)
                startActivity(detailIntent)
            }

        }
    }

    private fun getRandomActivity() {
        CoroutineScope(Dispatchers.IO).launch {
            val call =
                getRetrofit().create(APIService::class.java).getRandomActivity()
            val response = call.body()
            Log.d("SERVER", response.toString())
            if (call.isSuccessful && response?.key!=null){
                val detailIntent = Intent(this@ListActivity,DetailActivity::class.java)
                detailIntent.putExtra("response",response)
                detailIntent.putExtra("activitySelected", randomActivity)
                detailIntent.putExtra("apiCall", ApiCall.RANDOM_ACTIVITY)
                startActivity(detailIntent)
            }

        }
    }

    private fun selectRandomActivity(){
        val randomButton = findViewById<ImageButton>(R.id.randomActivityBt)
        randomButton.setOnClickListener {
            getRandomActivity()
        }
    }

}