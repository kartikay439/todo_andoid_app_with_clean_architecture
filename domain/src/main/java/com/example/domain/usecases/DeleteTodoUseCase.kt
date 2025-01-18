package com.example.domain.usecases

import com.example.domain.repository.TodoRepository

class DeleteTodoUseCase(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(todo: Int) {
        todoRepository.deleteTodo(todo)
    }
}