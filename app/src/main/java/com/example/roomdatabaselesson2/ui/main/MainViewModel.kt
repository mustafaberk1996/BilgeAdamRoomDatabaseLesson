package com.example.roomdatabaselesson2.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdatabaselesson2.data.AppDatabase
import com.example.roomdatabaselesson2.data.state.UserAddState
import com.example.roomdatabaselesson2.data.entitiy.User
import com.example.roomdatabaselesson2.data.state.UserUpdateState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {

    private val _userAddState:MutableStateFlow<UserAddState> = MutableStateFlow(UserAddState.Idle)
    val userAddState:StateFlow<UserAddState> = _userAddState

    private val _userState:MutableStateFlow<User?> = MutableStateFlow(null)
    val userState:StateFlow<User?> = _userState

    private val _userUpdateState:MutableStateFlow<UserUpdateState> = MutableStateFlow(UserUpdateState.Idle)
    val userUpdateState:StateFlow<UserUpdateState> = _userUpdateState


    fun saveOrUpdate(database: AppDatabase, name: String, surname: String, age: String, email: String, password: String) {

        if (_userState.value!=null){
            //update
            update(database,_userState.value!!, name, surname, age, email, password)
        }else{
            //insert islemi
            insert(database,name, surname, age, email, password)
        }


    }
    private fun update(database: AppDatabase,user: User, name: String, surname: String, age: String, email: String, password: String){
        viewModelScope.launch {
            runCatching {
                val updatedUser = user.copy(name = name, surName = surname, age = age.toInt(), email = email, password = password)
                database.userDao().update(updatedUser)
                _userUpdateState.value = UserUpdateState.Success
            }.onFailure {
                _userUpdateState.value = UserUpdateState.Error(it)
            }

        }

    }

    private fun insert(database: AppDatabase, name: String, surname: String, age: String, email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                _userAddState.value = UserAddState.Loading
                val user = User(name = name, surName = surname, age = age.toInt(), email = email, password = password)
                database.userDao().insert(user)
                //_userAddState.value = UserAddState.Success

                println("currentThere---> ${Thread.currentThread().name}")
                println("kayit basarili")
            }.onSuccess {
                _userAddState.value = UserAddState.Success
            }.onFailure {
                //hatayi ele al
                _userAddState.value = UserAddState.Error(it)
                println("bir sorun var: ${it.message}")
            }
        }
    }

    fun getUserById(id: Int, appDatabase: AppDatabase) {
        viewModelScope.launch {
            runCatching {
                val user = appDatabase.userDao().getUserById(id)
                _userState.value = user
            }
        }
    }


}