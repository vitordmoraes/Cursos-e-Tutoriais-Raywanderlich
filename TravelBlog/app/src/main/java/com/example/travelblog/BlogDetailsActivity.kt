package com.example.travelblog

import android.os.Bundle
import android.text.Html
import android.view.View
import kotlinx.android.synthetic.main.activity_blog_details.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.DrawableTransformation
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.travelblog.http.Blog
import com.example.travelblog.http.BlogHttpClient
import com.google.android.material.snackbar.Snackbar


class BlogDetailsActivity : AppCompatActivity () {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_details)

        imageBack.setOnClickListener { finish() }

        loadData()
    }


    private fun loadData() {
        BlogHttpClient.loadBlogArticles(
                onSuccess = { list: List<Blog> ->
                    runOnUiThread { showData(list[0]) }
                },
                onError = {
                    runOnUiThread { showErrorSnackbar() }
                }
        )
    }


    private fun showData(blog: Blog) {
        progressBar.visibility = View.GONE
        textTitle.text = blog.title
        textDate.text = blog.date
        textAuthor.text = blog.author.name
        textRating.text = blog.rating.toString()
        textViews.text = String.format("(%d views)", blog.views)
        textDescription.text = Html.fromHtml(blog.description)
        textDescription.text = blog.description
        ratingBar.rating = blog.rating

        Glide.with(this)
                .load(blog.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageMain)
        Glide.with(this)
                .load(blog.author.avatar)
                .transform(CircleCrop())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageAvatar)
    }


    private fun showErrorSnackbar() {
        Snackbar.make(findViewById(android.R.id.content),
                "Error during loading blog articles", Snackbar.LENGTH_INDEFINITE).run {
            setActionTextColor(resources.getColor(R.color.orange500))
            setAction("Retry") {
                loadData()
                dismiss()
            }
        }.show()
    }
}
