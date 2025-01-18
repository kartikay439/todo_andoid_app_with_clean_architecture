package com.example.todo.di

import com.example.todo.screens.ToDoScreenVIewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module{
viewModel { ToDoScreenVIewModel(get(),get(),get()) }
}