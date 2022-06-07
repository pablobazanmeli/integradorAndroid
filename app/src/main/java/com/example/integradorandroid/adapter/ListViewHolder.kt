package com.example.integradorandroid.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.integradorandroid.R

class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val textView = view.findViewById<TextView>(R.id.listItem)
    fun bind(text: String) {
        textView.text = text
    }
}
