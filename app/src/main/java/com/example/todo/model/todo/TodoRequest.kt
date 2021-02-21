package com.example.todo.model.todo

data class TodoRequest(
    val completed: Boolean?,
    val description: String?
)