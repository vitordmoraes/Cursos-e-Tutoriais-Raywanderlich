package com.example.travelblog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.travelblog.R
import com.example.travelblog.http.Blog
import kotlinx.android.synthetic.main.item_main.view.*

class MainAdapter() : ListAdapter<Blog, MainViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_main, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}

class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(blog: Blog) {
        itemView.textTitle.text = blog.title
        itemView.textDate.text = blog.date

        Glide.with(itemView)
                .load(blog.author.getAvatarUrl())
                .transform(CircleCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.imageAvatar)
    }
}

private val DIFF_CALLBACK: DiffUtil.ItemCallback<Blog> = object : DiffUtil.ItemCallback<Blog>() {
    override fun areItemsTheSame(oldData: Blog,
                                 newData: Blog): Boolean {
        return oldData.id == newData.id
    }

    override fun areContentsTheSame(oldData: Blog,
                                    newData: Blog): Boolean {
        return oldData == newData
    }
}