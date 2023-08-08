package com.example.roomdatabaselesson2.data.state

sealed class UserAddState{
    object Idle: UserAddState()
    object Loading: UserAddState()
    object Success: UserAddState()
    class Error(val throwable: Throwable): UserAddState()
}
