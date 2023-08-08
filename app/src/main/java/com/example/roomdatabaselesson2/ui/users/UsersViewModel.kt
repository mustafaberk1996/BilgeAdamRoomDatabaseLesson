package com.example.roomdatabaselesson2.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdatabaselesson2.RoomDatabase
import com.example.roomdatabaselesson2.data.AppDatabase
import com.example.roomdatabaselesson2.data.entitiy.User
import com.example.roomdatabaselesson2.data.state.AdapterState
import com.example.roomdatabaselesson2.data.state.UserListState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsersViewModel:ViewModel() {



    private val _userListState:MutableStateFlow<UserListState> = MutableStateFlow(UserListState.Idle)
    val userListState:StateFlow<UserListState> = _userListState

    private val _adapterState:MutableStateFlow<AdapterState> = MutableStateFlow(AdapterState.Idle)
    val adapterState:StateFlow<AdapterState> = _adapterState


    fun getAllUsers(appDatabase:AppDatabase){
        viewModelScope.launch {
            runCatching {
                _userListState.value = UserListState.Loading
                ///delay(2000)
                val users = appDatabase.userDao().getAll()
                _userListState.value = if (users.isEmpty()) UserListState.Empty else UserListState.Result(users.toMutableList())
            }.onFailure {
                _userListState.value = UserListState.Error(it)
            }

        }
    }

    fun removeUser(appDatabase: AppDatabase, user: User) {
        viewModelScope.launch {
            if (_userListState.value is UserListState.Result){
                val index = (_userListState.value as UserListState.Result).users.indexOf(user)
                 (_userListState.value as UserListState.Result).users.remove(user)
                appDatabase.userDao().delete(user)
                _adapterState.value = AdapterState.Removed(index)
            }

        }
    }


}