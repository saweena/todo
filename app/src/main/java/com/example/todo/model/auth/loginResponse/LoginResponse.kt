package com.example.todo.model.auth.loginResponse

data class LoginResponse(
    val token: String,
    val user: User
)