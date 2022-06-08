package com.example.integradorandroid

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.integradorandroid.retrofit.APIService
import com.example.integradorandroid.retrofit.BoredData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailActivity : AppCompatActivity() {
    lateinit var tittleText: TextView
    lateinit var participantsText: TextView
    lateinit var priceText: TextView
    lateinit var typeText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        tittleText = findViewById<TextView>(R.id.textActivity)
        participantsText = findViewById<TextView>(R.id.textParticipants)
        priceText = findViewById<TextView>(R.id.textPrice)
        typeText = findViewById<TextView>(R.id.textType)
        val tryButton = findViewById<Button>(R.id.button2)
        val selectedActivity = findViewById<TextView>(R.id.activityTypeTV)
        val response = intent.getSerializableExtra("response") as BoredData
        val activitySelected = intent.getSerializableExtra("activitySelected")
        val numberOfParticipants: Int? =
            if (response.participants == 0) null else response.participants
        val typeOfActivity = response.type
        tittleText.text = response.activity
        participantsText.text = response.participants.toString()
        priceText.text = when (response.price) {
            0F -> "FREE"
            in 0F..0.3F -> "LOW"
            in 0.3F..0.6F -> "MEDIUM"
            else -> "HIGH"
        }
        typeText.text = response.type
        selectedActivity.text = activitySelected.toString()

        tryButton.setOnClickListener {
            updateDataAndUI(numberOfParticipants, typeOfActivity)
        }
        onBackButtonClick()

    }


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    private fun updateDataAndUI(participants: Int?, type: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call =
                getRetrofit().create(APIService::class.java)
                    .getActivityByParticipantsAndType(participants, type)
            val response = call.body()
            Log.d("SERVER", response.toString())
            if (call.isSuccessful) {
                runOnUiThread {
                    tittleText.text = response?.activity
                    participantsText.text = response?.participants.toString()
                    priceText.text = response?.price.toString()
                     response?.price.let {
                         if (it != null) {
                             priceText.text = when (it) {
                                 0F -> "FREE"
                                 in 0F..0.3F -> "LOW"
                                 in 0.3F..0.6F -> "MEDIUM"
                                 else -> "HIGH"
                             }
                         }
                    }

                }

            }

        }
    }

    private fun onBackButtonClick() {
        val backButton = findViewById<ImageButton>(R.id.detailBackBt)
        backButton.setOnClickListener {
            finish()
        }
    }

}