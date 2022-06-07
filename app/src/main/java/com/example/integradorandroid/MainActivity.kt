package com.example.integradorandroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

const val baseUrl = "http://www.boredapi.com/api/"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        changeToActivityTermsAndConditions()
        initButton()
    }

    private fun initButton() {
       val button=findViewById<Button>(R.id.startBtn)
        val editText = findViewById<EditText>(R.id.inputNumberET)
        button.setOnClickListener{
            val participants = editText.text.toString().toInt()
            getSharedPreferences("PREFS",MODE_PRIVATE).edit().putInt("PARTICIPANTS",participants).commit()
            startActivity(Intent(this,ListActivity::class.java))
            //TODO("validate input")
        }
    }



    private fun changeToActivityTermsAndConditions() {

        val linkTermsAndConditions = findViewById<TextView>(R.id.termsTV)
        linkTermsAndConditions.setOnClickListener {
            val intent = Intent(this, TermsAndConditions::class.java)
            startActivity(intent)
        }
    }

}