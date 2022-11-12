package com.dicoding.picodiploma.testgithub2.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.testgithub2.ui.detail.DetailUserActivity
import com.dicoding.picodiploma.testgithub2.modal.UserResponse
import com.dicoding.picodiploma.testgithub2.databinding.ItemListRowBinding

class ListUserAdapter(private val listUser: ArrayList<UserResponse>) : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setList(users : ArrayList<UserResponse>){
        listUser.clear()
        listUser.addAll(users)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) : ViewHolder {
        val view =
            ItemListRowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        listUser[position].let { viewHolder.bind(it) }

        viewHolder.itemView.setOnClickListener {
            val intent = Intent(viewHolder.itemView.context, DetailUserActivity::class.java)
            viewHolder.itemView.context.startActivity(intent)
        }
        viewHolder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUser[viewHolder.adapterPosition])
        }
    }

    override fun getItemCount() = listUser.size

    class ViewHolder(private var binding: ItemListRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listUser: UserResponse) {
            binding.tvItemUsername.text = listUser.login
            Glide.with(binding.root)
                .load(listUser.avatarUrl)
                .circleCrop()
                .into(binding.imgItemPhoto)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserResponse)
    }
}