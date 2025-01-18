package com.example.data.repository

import android.util.Log
import com.example.data.localdb.ToDoData
import com.example.data.localdb.ToDoDao
import com.example.domain.repository.ToDo
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepositoryImpl(private val todoDao: ToDoDao): TodoRepository  {
    override suspend fun addTodo(todo: ToDo) {
        val todoData = ToDoData(
            title = todo.title,
        )
        todoDao.insertTodo(todoData)
    }

    override suspend fun getAllTodo(): Flow<List<ToDo>> {
        return todoDao.getAllTodo().map { it: List<ToDoData> ->
            it.map {
                ToDo(
                    uid = it.uid,
                    title = it.title
                )
            }
        }
    }

    override suspend fun deleteTodo(todo: Int) {
//        Log.v("test", todo.uid.toString())

        todoDao.deleteTodoById(todo)
    }


}