package com.example.integradorandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
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
        val participants = getSharedPreferences("PREFS", MODE_PRIVATE).getInt("PARTICIPANTS", 0)
        val activitySelected = intent.getSerializableExtra("activitySelected").toString()
        tryAnotherActivity()
        //val apiCall = intent.getSerializableExtra("apiCall")
        val selectedActivity = findViewById<TextView>(R.id.activityTypeTV)
        selectedActivity.text = activitySelected.toString()
        getActivity(activitySelected, participants)
        //renderActivity(response)
        onBackButtonClick()
        onClickTryAnother(activitySelected, participants)

    }

    /**
     * Function that render the activity received by the api response
     * */
    private fun renderActivity(response: BoredData, typeRandom: Boolean) {
        val titleTV = findViewById<TextView>(R.id.textActivity)
        val participantsTV = findViewById<TextView>(R.id.textParticipants)
        val priceTV = findViewById<TextView>(R.id.textPrice)
        val activityTypeTV = findViewById<TextView>(R.id.textType)
        val activityTypeText = findViewById<TextView>(R.id.textView4)
        titleTV.text = response.activity
        participantsTV.text = response.participants.toString()
        priceTV.text = determinePrice(response.price)
        activityTypeTV.text = response.type
        if (typeRandom){
            activityTypeTV.visibility= View.VISIBLE
        }else {
            activityTypeTV.visibility=View.INVISIBLE
            activityTypeText.visibility=View.INVISIBLE
        }


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
    private fun onBackButtonClick() {
        val backButton = findViewById<ImageButton>(R.id.detailBackBt)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun onClickTryAnother(activitySelected: String, participants: Int) {

        val tryAnotherBt = findViewById<Button>(R.id.tryAnotherBT)

        tryAnotherBt.setOnClickListener {
            getActivity(activitySelected, participants)
        }
    }

    private fun getActivity(activitySelected: String, participants: Int) {

        showDetails(false)
        showNoActivity(false)
        showLoadingProgressBar(true)

        val participantsShared: Int? = if (participants == 0) null else participants
        val activitySelectedSafe: String? =
            if (activitySelected == "Random") null else activitySelected
        CoroutineScope(Dispatchers.IO).launch {
            val call: Response<BoredData> =
                getRetrofit().create(APIService::class.java).getActivityCall(
                    participantsShared,
                    activitySelectedSafe?.lowercase()
                )
            if (call.isSuccessful) {
                val res = call.body() as BoredData
                if (res.key != null) {
                    Log.d("SERVER", res.toString())
                    runOnUiThread {
                        showDetails(true)
                        showLoadingProgressBar(false)
                        renderActivity(res, activitySelected == "Random")
                    }
                } else {
                    runOnUiThread {
                        showNoActivity(true)
                        showLoadingProgressBar(false)
                    }
                    Log.d("SERVER", res.toString())

                }
            }
        }

    }

    private fun showLoadingProgressBar(show: Boolean) {
        val loading = findViewById<ProgressBar>(R.id.loadingPB)

        if (show) {
            loading.visibility = View.VISIBLE
        } else {
            loading.visibility = View.INVISIBLE
        }
    }

    private fun showDetails(show: Boolean) {
        val details = findViewById<View>(R.id.detailSV)
        if (show) {
            details.visibility = View.VISIBLE
        } else {
            details.visibility = View.INVISIBLE
        }
    }

    private fun showNoActivity(show: Boolean) {
        val noActivity = findViewById<View>(R.id.noActivityTv)
        val tryAnotherActivity = findViewById<Button>(R.id.tryAnotherActivityBtn)
        if (show) {
            noActivity.visibility = View.VISIBLE
            tryAnotherActivity.visibility = View.VISIBLE
        } else {
            noActivity.visibility = View.INVISIBLE
            tryAnotherActivity.visibility = View.INVISIBLE
        }
    }

    private fun tryAnotherActivity() {
        val tryAnotherActivity = findViewById<Button>(R.id.tryAnotherActivityBtn)
        tryAnotherActivity.setOnClickListener {
            finish()
        }
    }

    private fun determinePrice(price: Float): String {
        return when {
            (price >= 0.6F) -> "HIGH"
            (price >= 0.3F) -> "MEDIUM"
            (price > 0F) -> "LOW"
            else -> "FREE"
        }
    }


}