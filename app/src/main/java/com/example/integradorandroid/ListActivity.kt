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
        "Education",
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
        //val participants = getSharedPreferences("PREFS", MODE_PRIVATE).getInt("PARTICIPANTS", 1)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler)

        val adapter = ListAdapter(list) {
            startActivityDetail(it)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        selectRandomActivity()
        backButton()
    }

    private fun startActivityDetail ( activity :String){
        val detailIntent = Intent(this@ListActivity,DetailActivity::class.java)
        detailIntent.putExtra("activitySelected", activity)
        startActivity(detailIntent)
    }

   private fun selectRandomActivity(){
        val randomButton = findViewById<ImageButton>(R.id.randomActivityBt)
        randomButton.setOnClickListener {
            //getRandomActivity()
            startActivityDetail ( randomActivity)

        }
    }

    private fun backButton(){
        val backButton=findViewById<ImageButton>(R.id.listBackBt)
        backButton.setOnClickListener {
            finish()
        }
    }

}