package com.example.todolist2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todoAdapter = TodoAdapter(mutableListOf())

        rvToDoItens.adapter = todoAdapter
        rvToDoItens.layoutManager = LinearLayoutManager(this)

        btnToDo.setOnClickListener {
            val todoTitle = etToDoItens.text.toString()
            if (todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle)
                todoAdapter.addTodo(todo)
                etToDoItens.text.clear()
            }
         }
        btnDelete.setOnClickListener {
            todoAdapter.deleteDoneTodos()
        }
        }
    }
