package com.example.roomdatabaselesson2.data.state

sealed class UserUpdateState{
    object Idle:UserUpdateState()
    object Success:UserUpdateState()
    class Error(val throwable: Throwable):UserUpdateState()
}
