package com.example.todo.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.model.auth.AuthRequest
import com.example.todo.model.auth.loginResponse.LoginResponse
import com.example.todo.model.todo.TodoRequest
import com.example.todo.model.todo.allListResponse.Todo
import com.example.todo.other.Event
import com.example.todo.other.Resource
import com.example.todo.repository.TodoRepository
import kotlinx.coroutines.launch
import retrofit2.http.Body
import retrofit2.http.Header

class TodoViewModel @ViewModelInject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    private val _loginResponse : MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse : LiveData<Resource<LoginResponse>> = _loginResponse

    private val _registerResponse : MutableLiveData<Event<Resource<LoginResponse>>> = MutableLiveData()
    val registerResponse : LiveData<Event<Resource<LoginResponse>>> = _registerResponse

    private val _authToken = MutableLiveData<String>()
    val authToken : LiveData<String> = _authToken

    private val _allTodoResponse : MutableLiveData<Resource<List<Todo>>> = MutableLiveData()
    val allTodoResponse : LiveData<Resource<List<Todo>>> = _allTodoResponse

    private val _todoDetailResponse : MutableLiveData<Resource<Todo>> = MutableLiveData()
    val todoDetailResponse : LiveData<Resource<Todo>> = _todoDetailResponse

    private val _addUpdateResponse : MutableLiveData<Event<Resource<Unit>>> = MutableLiveData()
    val addUpdateResponse : LiveData<Event<Resource<Unit>>> = _addUpdateResponse

    private val _deleteResponse : MutableLiveData<Event<Resource<Unit>>> = MutableLiveData()
    val deleteResponse : LiveData<Event<Resource<Unit>>> = _deleteResponse

    init {
        getAuthToken()
    }
    fun login(loginRequest: AuthRequest) = viewModelScope.launch{
        _loginResponse.postValue(repository.login(loginRequest))
    }

    fun register(registerRequest: AuthRequest) = viewModelScope.launch {
        _registerResponse.postValue(Event(repository.register(registerRequest)))
    }
    suspend fun logout(token : String) {
        repository.logout(token)
        repository.clearToken()
    }

    suspend fun clearToken(){
        repository.clearToken()
    }

    fun saveToken(token: String) = viewModelScope.launch {
        repository.saveToken(token)
    }

    fun getAuthToken() = viewModelScope.launch {
        _authToken.postValue(repository.getAuthToken())

    }

    fun getTodoList(token : String, type : String ) = viewModelScope.launch {
        _allTodoResponse.postValue(repository.getTodoList(token,type))
    }

    fun getTodoByID(token : String, type : String, id : String) = viewModelScope.launch {
        _todoDetailResponse.postValue(repository.getTaskByID(token, type, id))
    }

    fun updateTodoByID(token : String, id : String, updateTodoRequest: TodoRequest) = viewModelScope.launch {
        _addUpdateResponse.postValue(Event(repository.updateTaskByID(token, id, updateTodoRequest)))
    }

    fun deleteTodoByID(token : String, type : String, id : String) = viewModelScope.launch {
        _deleteResponse.postValue(Event(repository.deleteTodoByID(token, type, id)))
    }

    fun addTodo(token : String, todo : TodoRequest) = viewModelScope.launch {
        _addUpdateResponse.postValue(Event(repository.addTodo(token, todo)))
    }
}