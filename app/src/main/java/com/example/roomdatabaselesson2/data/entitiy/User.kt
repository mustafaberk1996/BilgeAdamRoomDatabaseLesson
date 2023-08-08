package com.example.roomdatabaselesson2.data.entitiy

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val name:String?,
    val surName:String?,
    val age:Int? = 0,
    val email:String?,
    val password:String?
){
    fun fullName() = "$name $surName"
}
