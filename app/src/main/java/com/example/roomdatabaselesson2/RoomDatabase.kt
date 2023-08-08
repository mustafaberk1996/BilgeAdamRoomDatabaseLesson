package com.example.roomdatabaselesson2

import android.content.Context
import androidx.room.Room
import com.example.roomdatabaselesson2.data.AppDatabase

object RoomDatabase {
    fun getDatabase(context:Context) =
        Room.databaseBuilder(context, AppDatabase::class.java,"database").build()
}