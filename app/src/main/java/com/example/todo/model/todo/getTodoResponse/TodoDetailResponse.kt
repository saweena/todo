package com.example.todo.model.todo.getTodoResponse

import com.example.todo.model.todo.allListResponse.Todo
import com.google.gson.annotations.SerializedName

data class TodoDetailResponse(
    @SerializedName("data")
    val todo: Todo,
    val success: Boolean
)