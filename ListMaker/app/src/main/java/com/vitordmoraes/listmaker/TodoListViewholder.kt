package com.vitordmoraes.listmaker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoListViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {

    var listPositionTextView = itemView.findViewById<TextView>(R.id.itemNumber)
    var listItemTittle = itemView.findViewById<TextView>(R.id.itemString)

}