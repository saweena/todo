package com.example.todo.model.auth

data class AuthRequest(
    val age: Int? = null,
    val email: String,
    val name: String? = null,
    val password: String
)