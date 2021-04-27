package com.example.travelblog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelblog.adapter.MainAdapter
import com.example.travelblog.http.Blog
import com.example.travelblog.http.BlogHttpClient
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val adapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter // 3


        loadData()
    }

    private fun loadData() {
        BlogHttpClient.loadBlogArticles(
                onSuccess = { blogList: List<Blog> ->
                    runOnUiThread { adapter.submitList(blogList) }
                },
                onError = {
                    runOnUiThread { showErrorSnackbar() }
                }
        )
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