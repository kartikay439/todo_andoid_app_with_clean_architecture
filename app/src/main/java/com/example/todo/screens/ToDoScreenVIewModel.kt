package com.example.todo.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.ToDo
import com.example.domain.usecases.AddTodoUseCase
import com.example.domain.usecases.DeleteTodoUseCase
import com.example.domain.usecases.GetAllTodoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ToDoScreenVIewModel(
    private val addTodoUseCase: AddTodoUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val getAllTodoUseCase: GetAllTodoUseCase
) : ViewModel() {
    val todos_state = MutableStateFlow<List<ToDo>>(emptyList())
    val state = todos_state.asStateFlow()
    init {
        getAllTodo()
    }

    fun addTodo(todo: ToDo) {
        viewModelScope.launch {
            addTodoUseCase(todo)
            getAllTodo()

        }
    }

    fun deleteTodo(uid: Int) {
        viewModelScope.launch {
            deleteTodoUseCase(uid)
            getAllTodo()
        }
    }

    fun getAllTodo() {
        viewModelScope.launch {
            getAllTodoUseCase().collect {
                todos_state.value = it
            }
        }

    }
}