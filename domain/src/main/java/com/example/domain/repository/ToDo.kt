package com.example.domain.repository

data class ToDo(
    val uid: Int ,
    val title: String
){
    fun toDomain():ToDo{
        return ToDo(
            uid=uid,
            title = title
        )

    }
}
