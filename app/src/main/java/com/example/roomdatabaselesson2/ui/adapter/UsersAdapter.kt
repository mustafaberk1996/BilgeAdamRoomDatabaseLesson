package com.example.roomdatabaselesson2.ui.adapter


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabaselesson2.R
import com.example.roomdatabaselesson2.data.entitiy.User
import com.example.roomdatabaselesson2.databinding.UserListItemBinding


class UsersAdapter(private val context:Context, private val users:List<User>,
                   val onClick:(user:User)->Unit,
                   val onRemove:(user: User)->Unit):RecyclerView.Adapter<UsersAdapter.MyViewHolder>() {

    class MyViewHolder(binding: UserListItemBinding):RecyclerView.ViewHolder(binding.root){
        val tvFullName = binding.tvFullName
        val tvAge = binding.tvAge
        val tvDot = binding.tvDot
        val tvEmail = binding.tvEmail
        val tvLetter = binding.tvLetter
        val ivDelete = binding.ivDelete

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            UserListItemBinding.inflate(LayoutInflater.from(context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = users[position]

        with(user){
            holder.tvAge.text = age.toString()
            holder.tvEmail.text = email
            holder.tvFullName.text = fullName()
            holder.tvLetter.text = "${name?.first()?.toUpperCase()} ${surName?.first()?.toUpperCase()}"
        }


        val drawable = holder.tvLetter.background
        val wrappedDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(wrappedDrawable,Color.parseColor("#900C3F"))


        holder.ivDelete.setOnClickListener {
            onRemove(user)
        }

        holder.itemView.setOnClickListener {
            onClick(user)
        }

    }
}