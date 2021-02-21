package com.example.todo.network

import com.example.todo.model.auth.AuthRequest
import com.example.todo.model.auth.loginResponse.LoginResponse
import com.example.todo.model.todo.TodoRequest
import com.example.todo.model.todo.allListResponse.AllListResponse
import com.example.todo.model.todo.getTodoResponse.TodoDetailResponse
import retrofit2.Response
import retrofit2.http.*

interface TodoAPI {
    @POST("user/login")
    suspend fun login(@Body loginRequest: AuthRequest) : Response<LoginResponse>

    @POST("user/register")
    suspend fun register(@Body registerRequest: AuthRequest) : Response<LoginResponse>

    @POST("user/logout")
    suspend fun logout(@Header("Authorization") token : String) : Response<Unit>

    @GET("task")
    suspend fun getTodoList(
        @Header("Authorization") token : String,
        @Header("Content-Type") type : String
    ): Response<AllListResponse>

    @GET("task/{id}")
    suspend fun getTaskByID(
        @Header("Authorization") token : String,
        @Header("Content-Type") type : String,
        @Path("id") id : String
    ): Response<TodoDetailResponse>

    @PUT("task/{id}")
    suspend fun updateTodoByID(
        @Header("Authorization") token : String,
        //@Header("Content-Type") type : String,
        @Path("id") id : String,
        @Body updateTodoRequest: TodoRequest

    ): Response<Unit>

    @DELETE("task/{id}")
    suspend fun deleteTodoByID(
        @Header("Authorization") token : String,
        @Header("Content-Type") type : String,
        @Path("id") id : String
    ): Response<Unit>

    @POST("task")
    suspend fun addTodo(
        @Header("Authorization") token : String,
        @Body todo : TodoRequest
    ): Response<Unit>
}