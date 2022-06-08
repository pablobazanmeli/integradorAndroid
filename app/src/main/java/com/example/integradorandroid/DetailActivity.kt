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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        //val response = intent.getSerializableExtra("response") as BoredData
        val participants = getSharedPreferences("PREFS", MODE_PRIVATE).getInt("PARTICIPANTS", 1)
        val activitySelected = intent.getSerializableExtra("activitySelected").toString()
        //val apiCall = intent.getSerializableExtra("apiCall")
        val selectedActivity =findViewById<TextView>(R.id.activityTypeTV)
        selectedActivity.text =activitySelected.toString()
        getActivity(activitySelected,participants )
        //renderActivity(response)
        onBackButtonClick()
        onClickTryAnother(activitySelected, participants)

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
            finish()
        }
    }

    fun onClickTryAnother( activitySelected: String,participants: Int,){

        val tryAnotherBt =findViewById<Button>(R.id.tryAnotherBT)

        tryAnotherBt.setOnClickListener {
            getActivity(activitySelected, participants )
        }
    }

    private fun getActivity(activitySelected :String, participants :Int) {
        val participantsShared: Int? = if (participants==0)null else participants
        val activitySelectedSafe: String? = if (activitySelected=="Random") null else activitySelected
        CoroutineScope(Dispatchers.IO).launch {
            var call: Response<BoredData> = getRetrofit().create(APIService::class.java).getActivityCall(
                participantsShared,
                activitySelectedSafe?.lowercase()
            )
            if (call.isSuccessful) {
                val res = call.body() as BoredData
                if (res.key != null) {

                    Log.d("SERVER", res.toString())
                    runOnUiThread {
                        renderActivity(res)
                    }
                }else {

                    runOnUiThread {
                        val noActivityText = findViewById<TextView>(R.id.noActivityTv)
                        noActivityText.visibility=View.VISIBLE
                    }
                    Log.d("SERVER", res.toString())

                }
            }
        }

    }


    private fun showSnackBar(view: View, message: String) {
      val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
      snack.show()
  }
}