package com.example.todo

import android.app.Application
import com.example.data.di.dataModule
import com.example.domain.di.domainModule
import com.example.todo.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ToDO: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ToDO)
            modules(
                appModule,
                domainModule,
                dataModule
            )
        }
    }
}