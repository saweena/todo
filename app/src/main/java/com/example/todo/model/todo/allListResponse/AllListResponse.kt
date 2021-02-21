package com.example.todo.model.todo.allListResponse

import com.google.gson.annotations.SerializedName

data class AllListResponse(
    val count: Int,
    @SerializedName("data")
    val todoList: List<Todo>


)