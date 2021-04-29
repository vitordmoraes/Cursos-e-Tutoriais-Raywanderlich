package com.vitordmoraes.listmaker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskListViewHolder (itemView : View) : RecyclerView.ViewHolder (itemView) {
    val taskTextView: TextView = itemView?.findViewById<TextView>(R.id.textView_task)



}