package com.example.integradorandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.integradorandroid.R

class ListAdapter(private val activitiesList: List<String>, val listenerer: (String) -> Unit) :
    RecyclerView.Adapter<ListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListViewHolder(layoutInflater.inflate(R.layout.item_recycler, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = activitiesList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {listenerer(item)}
    }

    override fun getItemCount(): Int = activitiesList.size
}