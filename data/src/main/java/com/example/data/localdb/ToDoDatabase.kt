package com.example.data.localdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ToDoData::class], version = 1)
abstract class ToDoDatabase:RoomDatabase() {
    abstract fun todoDao(): ToDoDao
    companion object {
        const val DATABASE_NAME = "todo_db"

        @Volatile
         private var INSTANCE : ToDoDatabase? =null

        fun getInstance(context: Context):ToDoDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    DATABASE_NAME

                ).build()
                INSTANCE = instance
                instance
            }
        }
    }


}