package com.example.todo.di

import ToDoScreenVIewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module{
viewModel { ToDoScreenVIewModel(get(),get(),get()) }
}