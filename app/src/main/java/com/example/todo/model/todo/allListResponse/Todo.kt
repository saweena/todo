package com.example.todo.model.todo.allListResponse

data class Todo(
    val __v: Int,
    val _id: String,
    val completed: Boolean,
    val createdAt: String,
    val description: String,
    val owner: String,
    val updatedAt: String
)