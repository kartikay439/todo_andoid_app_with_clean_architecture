package com.example.domain.usecases

import com.example.domain.repository.ToDo
import com.example.domain.repository.TodoRepository

class AddTodoUseCase(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(todo: ToDo) {
        todoRepository.addTodo(todo)
    }
}