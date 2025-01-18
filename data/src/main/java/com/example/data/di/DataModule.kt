package com.example.data.di

import com.example.data.localdb.ToDoDatabase
import com.example.data.repository.TodoRepositoryImpl
import com.example.domain.repository.TodoRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { ToDoDatabase.getInstance(androidContext())  }
    // get type  TOdo Database and from that database we will get dao
    single { get<ToDoDatabase>().todoDao() }
    single<TodoRepository> {TodoRepositoryImpl(get())  }
}