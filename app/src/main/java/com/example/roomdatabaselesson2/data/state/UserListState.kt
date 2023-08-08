package com.example.roomdatabaselesson2.data.state

import com.example.roomdatabaselesson2.data.entitiy.User

sealed class UserListState {
    object Idle:UserListState()
    object Loading:UserListState()
    object Empty:UserListState()
    class Result(val users:MutableList<User>):UserListState()
    class Error(val throwable: Throwable):UserListState()
}