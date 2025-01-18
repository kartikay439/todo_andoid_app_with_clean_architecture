package com.example.domain.usecases

import com.example.domain.repository.ToDo
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class GetAllTodoUseCase(private val todoRepository: TodoRepository) {
    suspend operator fun invoke():Flow<List<ToDo>> {
        return todoRepository.getAllTodo()
    }
}