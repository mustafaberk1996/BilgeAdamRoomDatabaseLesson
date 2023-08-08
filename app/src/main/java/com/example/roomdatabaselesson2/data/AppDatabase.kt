package com.example.roomdatabaselesson2.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.roomdatabaselesson2.data.dao.UserDao
import com.example.roomdatabaselesson2.data.entitiy.User

@Database(
    entities = [User::class],
    version = 1
)
abstract class AppDatabase:RoomDatabase() {
    abstract fun userDao():UserDao
}