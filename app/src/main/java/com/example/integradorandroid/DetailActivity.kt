package com.example.integradorandroid

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.integradorandroid.retrofit.BoredData
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val title = findViewById<TextView>(R.id.textActivity)
        val participants = findViewById<TextView>(R.id.textParticipants)
        val price = findViewById<TextView>(R.id.textPrice)
        val type = findViewById<TextView>(R.id.textType)
        val selectedActivity =findViewById<TextView>(R.id.activityTypeTV)
        val response = intent.getSerializableExtra("response") as BoredData
        val activitySelected = intent.getSerializableExtra("activitySelected")
        title.text = response.activity
        participants.text = response.participants.toString()
        price.text = response.price.toString()
        type.text = response.type
        selectedActivity.text =activitySelected.toString()

        onBackButtonClick()

    }

    fun onBackButtonClick(){
        val backButton = findViewById<ImageButton>(R.id.detailBackBt)
        backButton.setOnClickListener {
            finish()
        }
    }
}