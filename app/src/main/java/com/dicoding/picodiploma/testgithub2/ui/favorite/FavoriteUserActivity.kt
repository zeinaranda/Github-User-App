package com.dicoding.picodiploma.testgithub2.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.testgithub2.modal.UserResponse
import com.dicoding.picodiploma.testgithub2.adapter.ListUserAdapter
import com.dicoding.picodiploma.testgithub2.database.FavoriteUser
import com.dicoding.picodiploma.testgithub2.databinding.ActivityFavoriteUserBinding
import com.dicoding.picodiploma.testgithub2.ui.detail.DetailUserActivity
import java.util.ArrayList

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding

    private val list = ArrayList<UserResponse>()
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: ListUserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "My Favorite Users"

        adapter = ListUserAdapter(list)
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.adapter = adapter
        viewModel.getFavoriteUser()?.observe(this, {
            if (it!=null){
                val list = favList(it)
                adapter.setList(list)
            }
        })
        showRecyclerList()
    }

    private fun favList(users: List<FavoriteUser>): ArrayList<UserResponse> {
        val listUsers = ArrayList<UserResponse>()
        for (user in users){
            val favUser = UserResponse(
                user.id,
                null,
                user.avatar_url,
                null,
                null,
                null,
                null,
                null,
                null,
                user.login,
                null
            )
            listUsers.add(favUser)
        }
        return listUsers
    }

    private fun showRecyclerList() {
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserResponse) {
                val intentToDetail = Intent(this@FavoriteUserActivity, DetailUserActivity::class.java)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatarUrl)
                startActivity(intentToDetail)
            }
        })
    }
}