package com.example.domain.di

import com.example.domain.usecases.AddTodoUseCase
import com.example.domain.usecases.DeleteTodoUseCase
import com.example.domain.usecases.GetAllTodoUseCase
import org.koin.dsl.module

val todoUseCase = module {
    single {
        AddTodoUseCase(get())
    }
    single {
        DeleteTodoUseCase(get())
    }
    single {
        GetAllTodoUseCase(get())
    }

}