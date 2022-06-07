package com.example.integradorandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class TermsAndConditions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)
        changeToActivityMain()
    }

    fun changeToActivityMain() {
        val linkTermsAndConditions = findViewById<ImageButton>(R.id.closeTermsIB)
        linkTermsAndConditions.setOnClickListener {
           finish()
        }
    }
}