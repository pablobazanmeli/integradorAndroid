package com.example.integradorandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.integradorandroid.retrofit.APIService
import com.example.integradorandroid.retrofit.BoredData
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailActivity : AppCompatActivity() {
    val activities  = mutableSetOf<BoredData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val response = intent.getSerializableExtra("response") as BoredData
        val activitySelected = intent.getSerializableExtra("activitySelected")
        val apiCall = intent.getSerializableExtra("apiCall")
        val selectedActivity =findViewById<TextView>(R.id.activityTypeTV)
        selectedActivity.text =activitySelected.toString()
        renderActivity(response)
        onBackButtonClick()
        onClickTryAnother(response, apiCall.toString())

    }
/**
 * Function that render the activity received by the api response
 * */
    private fun renderActivity(response: BoredData){
        val titleTV = findViewById<TextView>(R.id.textActivity)
        val participantsTV = findViewById<TextView>(R.id.textParticipants)
        val priceTV = findViewById<TextView>(R.id.textPrice)
        val activityTypeTV = findViewById<TextView>(R.id.textType)
        titleTV.text = response.activity
        participantsTV.text = response.participants.toString()
        priceTV.text = response.price.toString()
        activityTypeTV.text = response.type

    }
    /**
     * function to generate a rest call to a remote server
     * */
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
    /**
     *Function that stops the currently-running activity
     * */
    fun onBackButtonClick(){
        val backButton = findViewById<ImageButton>(R.id.detailBackBt)
        backButton.setOnClickListener {
            activities.clear()
            finish()
        }
    }

    fun onClickTryAnother(response: BoredData, apiCall :String ){

        val tryAnotherBt =findViewById<Button>(R.id.tryAnotherBT)

        tryAnotherBt.setOnClickListener {
            getActivity(apiCall, response)
        }
    }
    //TODO control response on this activity
    private fun getActivity(apiCall: String, response:BoredData) {
        val activities  = mutableSetOf<BoredData>()
        activities.add(response)

        CoroutineScope(Dispatchers.IO).launch {
        var call=getRetrofit().create(APIService::class.java).getRandomActivity()
        when(apiCall){
            ApiCall.RANDOM_ACTIVITY.toString()->call=getRetrofit().create(APIService::class.java).getRandomActivity()
            ApiCall.TYPE_AND_PARTICIPANTS_CALL.toString()->call=getRetrofit().create(APIService::class.java).getActivityByParticipantsAndType(response.participants, response.type.lowercase())
            ApiCall.TYPE_ACTIVITY_CALL.toString()->call=getRetrofit().create(APIService::class.java).getActivityByType(response.type.lowercase())
        }

            val res = call.body() as BoredData
            val activity = activities.find { it.key == res.key }
            if (call.isSuccessful && activity==null) {
                println(activities)
                activities.add(res)
                createActivity(res)
                println("success!!! ")
                println("res key "+res.key)
                println("response key "+response?.key)
                Log.d("SERVER", res.toString())
            } else {
                showSnackBar(findViewById(R.id.detailConstraint), "Same activity, try again!")
                Log.d("SERVER", res.toString())
            }
        }

    }

    private fun createActivity(response: BoredData) :BoredData{
        return response
    }


    private fun showSnackBar(view: View, message: String) {
      val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
      snack.show()
  }
}