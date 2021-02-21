package com.example.todo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todo.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
    }
}