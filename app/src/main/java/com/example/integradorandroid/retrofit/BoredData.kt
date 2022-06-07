package com.example.integradorandroid.retrofit

import java.io.Serializable

data class BoredData(
    val activity: String,
    val type: String,
    val price: Float,
    val participants: Int
):Serializable
