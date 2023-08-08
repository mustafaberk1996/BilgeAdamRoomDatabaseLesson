package com.example.roomdatabaselesson2.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.roomdatabaselesson2.R
import com.example.roomdatabaselesson2.databinding.ActivityMainBinding
import com.example.roomdatabaselesson2.RoomDatabase
import com.example.roomdatabaselesson2.data.state.UserAddState
import com.example.roomdatabaselesson2.data.state.UserUpdateState
import com.example.roomdatabaselesson2.showSnackBar
import com.example.roomdatabaselesson2.showToast
import com.example.roomdatabaselesson2.ui.users.UsersActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    private lateinit var binding:ActivityMainBinding

    private val viewModel:MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        val updateProcess = intent.getBooleanExtra(UsersActivity.UPDATE_KEY,false)
        if (updateProcess){
            binding.btnSaveUser.text = "Update"
            val id = intent.getIntExtra(UsersActivity.UPDATE_USER_ID_KEY,-1)
            if (id != -1) viewModel.getUserById(id,RoomDatabase.getDatabase(this))
        }


        observeUserAddState()
        observeUserState()
        observeUserUpdateState()

        binding.btnSaveUser.setOnClickListener {

            viewModel.saveOrUpdate(RoomDatabase.getDatabase(this),
                binding.etName.text.toString(),
                binding.etSurname.text.toString(),
                binding.etAge.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )

        }

        binding.btnUsers.setOnClickListener {
            startActivity(
                Intent(this, UsersActivity::class.java)
            )
        }


    }

    private fun observeUserUpdateState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.userUpdateState.collect{
                    when(it){
                        is UserUpdateState.Idle->{}
                        is UserUpdateState.Success->{
                            showToast(getString(R.string.update_user_success))
                            startActivity(
                                Intent(this@MainActivity,UsersActivity::class.java)
                            )
                        }
                        is UserUpdateState.Error->{
                            showToast(getString(R.string.upps_something_went_wrong))
                        }
                    }
                }
            }
        }
    }

    private fun observeUserState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.userState.collect{
                    it?.let {user->
                        with(user){
                            binding.etEmail.setText(email)
                            binding.etAge.setText(age.toString())
                            binding.etPassword.setText(password)
                            binding.etName.setText(name)
                            binding.etSurname.setText(surName)
                        }
                    }
                }
            }
        }
    }

    private fun observeUserAddState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.userAddState.collect{
                    when(it){
                        UserAddState.Idle->{}
                        UserAddState.Loading->{
                            binding.btnSaveUser.isVisible = false
                            binding.progressBar.isVisible = true
                        }
                        UserAddState.Success->{
                            binding.btnSaveUser.isVisible = true
                            binding.progressBar.isVisible = false
                            binding.etName.text.clear()
                            binding.etSurname.text.clear()
                            binding.etAge.text.clear()
                            binding.etEmail.text.clear()
                            binding.etPassword.text.clear()

                            showSnackBar(binding.btnSaveUser, getString(R.string.user_saving_success))
                        }
                        is UserAddState.Error->{
                            binding.btnSaveUser.isVisible = true
                            binding.progressBar.isVisible = false
                            showSnackBar(binding.btnSaveUser, getString(R.string.upps_something_went_wrong))
                        }
                    }
                }
            }
        }
    }


}