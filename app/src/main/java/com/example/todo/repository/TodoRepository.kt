package com.example.todo.repository

import androidx.datastore.preferences.core.clear
import androidx.datastore.preferences.core.edit
import com.example.todo.datastore.UserPreferences
import com.example.todo.model.auth.AuthRequest
import com.example.todo.model.auth.loginResponse.LoginResponse
import com.example.todo.model.todo.TodoRequest
import com.example.todo.model.todo.allListResponse.Todo
import com.example.todo.network.TodoAPI
import com.example.todo.other.Resource
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val todoAPI: TodoAPI,
    private val userPreferences: UserPreferences
) {

    suspend fun login(@Body loginRequest: AuthRequest) : Resource<LoginResponse>{
        return try {
            val response = todoAPI.login(loginRequest)
            when{
                response.isSuccessful -> {
                    response.body()?.let {
                        Resource.success(it)
                    }!!
                }
                else -> {
                    Resource.error(response.code().toString(),null)
                }
            }
        }catch (t : Throwable){
            Resource.error(t.toString(),null)
        }
    }

    suspend fun register(registerRequest: AuthRequest) : Resource<LoginResponse>{
        return try {
            val response = todoAPI.register(registerRequest)
            when{
                response.isSuccessful -> {
                    response.body()?.let {
                        Resource.success(it)
                    }!!
                }
                else -> {
                    Resource.error(response.code().toString(),null)
                }
            }
        }catch (t : Throwable){
            Resource.error(t.toString(),null)
        }
    }
    suspend fun logout(@Header("Authorization") token : String) : Resource<Unit>{
        return try {
            val response = todoAPI.logout("Bearer $token")
            when{
                response.isSuccessful -> {
                    response.body()?.let {
                        Resource.success(Unit)
                    }!!
                }
                else -> {
                    Resource.error(response.code().toString(),null)
                }
            }
        }catch (t : Throwable){
            Resource.error(t.toString(),null)
        }
    }

    suspend fun saveToken(token: String){
        userPreferences.saveAuthToken(token)
    }
    suspend fun getAuthToken(): String? {
        return userPreferences.getAuthToken()
    }

    suspend fun clearToken() = userPreferences.clearToken()


    suspend fun getTodoList(
        token : String,
        type : String
    ): Resource<List<Todo>>{
        return try {
            val response = todoAPI.getTodoList("Bearer $token",type)
            when{
                response.isSuccessful -> {
                    response.body()?.let {
                        Resource.success(it.todoList)
                    }!!
                }

                else -> {
                    Resource.error(response.code().toString(),null)
                }
            }
        }catch (t : Throwable){
            Resource.error(t.toString(),null)
        }
    }

    suspend fun getTaskByID(
       token : String,
       type : String,
       id : String
    ): Resource<Todo>{
        return try {
            val response = todoAPI.getTaskByID("Bearer $token",type,id)
            when{
                response.isSuccessful -> {
                    response.body()?.let {
                        Resource.success(it.todo)
                    }!!
                }

                else -> {
                    Resource.error(response.code().toString(),null)
                }
            }
        }catch (t : Throwable){
            Resource.error(t.toString(),null)
        }
    }

    suspend fun updateTaskByID(
        token : String,
        id : String,
        updateTodoRequest: TodoRequest
    ): Resource<Unit>{
        return try {
            val response = todoAPI.updateTodoByID("Bearer $token",id, updateTodoRequest)
            when{
                response.isSuccessful -> {
                    Resource.success(Unit)
                }

                else -> {
                    Resource.error(response.code().toString(),null)
                }
            }
        }catch (t : Throwable){
            Resource.error(t.toString(),null)
        }

    }

    suspend fun deleteTodoByID(
        token : String,
        type : String,
        id : String
    ): Resource<Unit>{
        return try {
            val response = todoAPI.deleteTodoByID("Bearer $token",type,id)
            when{
                response.isSuccessful -> {
                    Resource.success(Unit)
                }

                else -> {
                    Resource.error(response.code().toString(),null)
                }
            }
        }catch (t : Throwable){
            Resource.error(t.toString(),null)
        }


    }

    suspend fun addTodo(
        @Header("Authorization") token : String,
        @Body todo : TodoRequest
    ): Resource<Unit>{
        return try {
            val response = todoAPI.addTodo("Bearer $token",todo)
            when{
                response.isSuccessful -> {
                    Resource.success(Unit)
                }
                else -> {
                    Resource.error(response.code().toString(),null)
                }
            }
        }catch (t : Throwable){
            Resource.error(t.toString(),null)
        }
    }
}