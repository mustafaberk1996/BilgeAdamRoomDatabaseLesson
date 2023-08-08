package com.example.roomdatabaselesson2.ui.users

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabaselesson2.R
import com.example.roomdatabaselesson2.RoomDatabase
import com.example.roomdatabaselesson2.data.entitiy.User
import com.example.roomdatabaselesson2.data.state.AdapterState
import com.example.roomdatabaselesson2.data.state.UserListState
import com.example.roomdatabaselesson2.databinding.ActivityUsersBinding
import com.example.roomdatabaselesson2.showSnackBar
import com.example.roomdatabaselesson2.ui.adapter.UsersAdapter
import com.example.roomdatabaselesson2.ui.main.MainActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UsersActivity : AppCompatActivity() {

    private lateinit var binding:ActivityUsersBinding
    private val viewModel:UsersViewModel by viewModels()

    private var adapter: UsersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeUserListState()
        observeAdapterState()



        viewModel.getAllUsers(RoomDatabase.getDatabase(this))
        binding.btnUserAddScreen.setOnClickListener {
            finish()
        }

    }

    private fun observeAdapterState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.adapterState.collect{
                    when(it){
                        is AdapterState.Idle->{}
                        is AdapterState.Removed->{
                            adapter?.notifyItemRemoved(it.index)
                        }
                        else-> {

                        }
                    }
                }
            }
        }
    }

    private fun observeUserListState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.userListState.collect{
                    when(it){
                        is UserListState.Idle->{}
                        is UserListState.Loading->{

                        }
                        is UserListState.Result->{
                            binding.btnUserAddScreen.isVisible = false

                            adapter = UsersAdapter(this@UsersActivity, it.users ,this@UsersActivity::onClick){user->
                                //remove islemi
                                viewModel.removeUser(RoomDatabase.getDatabase(this@UsersActivity), user)
                            }
                            binding.rvUsers.adapter = adapter

                        }
                        is UserListState.Empty->{
                            showSnackBar(binding.rvUsers,"Hic kullanici yok!")
                            binding.btnUserAddScreen.isVisible = true

                        }
                        is UserListState.Error->{

                        }
                    }
                }
            }
        }
    }

    companion object{
        const val UPDATE_KEY="update_key"
        const val UPDATE_USER_ID_KEY="update_user_id_key"
    }

    private fun onClick(user: User){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(UPDATE_KEY, true)
        intent.putExtra(UPDATE_USER_ID_KEY, user.id)
        startActivity(intent)
    }

}