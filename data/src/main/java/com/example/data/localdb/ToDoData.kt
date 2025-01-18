package com.example.data.localdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo")
data class ToDoData(
    @PrimaryKey(autoGenerate = true) val uid: Int= 0,
    @ColumnInfo(name = "title") val title: String,
)
