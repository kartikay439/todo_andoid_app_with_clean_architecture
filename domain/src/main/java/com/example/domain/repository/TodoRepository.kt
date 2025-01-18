package com.example.domain.repository


import kotlinx.coroutines.flow.Flow

interface TodoRepository{
    suspend fun addTodo(todo: ToDo)
    suspend fun getAllTodo(): Flow<List<ToDo>>
    suspend fun deleteTodo(todo: Int)
}