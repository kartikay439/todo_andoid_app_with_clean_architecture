package com.example.data.localdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    // Query to get all todos as a Flow
    @Query("SELECT * FROM todo")
    fun getAllTodo(): Flow<List<ToDoData>>

    // Insert a new todo
    @Insert
    suspend fun insertTodo(todo: ToDoData)

    // Delete a todo by its ID
    @Query("DELETE FROM todo WHERE uid = :id")
    suspend fun deleteTodoById(id: Int)
}
