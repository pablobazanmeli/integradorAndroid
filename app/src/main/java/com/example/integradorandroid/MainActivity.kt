package com.example.integradorandroid

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

const val baseUrl = "http://www.boredapi.com/api/"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        changeToActivityTermsAndConditions()
        startListActivity()
    }



    private fun startListActivity() {
        /**
         * This function will open the ListActivity in case the EditText
         * is not empty and the number entered is greater than 0,
         * otherwise it will display a SnackBar with the error message
         */
       val button=findViewById<Button>(R.id.startBtn)
        val editText = findViewById<EditText>(R.id.inputNumberET)
        button.setOnClickListener{
            val participants = editText.text.toString()
            if(participants.isNotEmpty() && participants.toInt()>0){
                getSharedPreferences("PREFS",MODE_PRIVATE).edit().putInt("PARTICIPANTS",participants.toInt()).apply()
                changeToActivityListActivity()
            }
            else
            {
                showSnackBar(this.findViewById(android.R.id.content), "Enter a number in Participants greater than 0")
            }
        }
    }

    private fun showSnackBar(view: View, message: String) {
        val snack = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snack.show()
    }

    private fun changeToActivityListActivity() {
        val intent = Intent(this, ListActivity::class.java)
        startActivity(intent)
    }

    private fun changeToActivityTermsAndConditions() {
        val linkTermsAndConditions = findViewById<TextView>(R.id.termsTV)
        linkTermsAndConditions.setOnClickListener {
            val intent = Intent(this, TermsAndConditions::class.java)
            startActivity(intent)
        }
    }

}