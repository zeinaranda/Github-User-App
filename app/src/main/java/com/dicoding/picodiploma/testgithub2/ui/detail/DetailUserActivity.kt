package com.dicoding.picodiploma.testgithub2.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.testgithub2.R
import com.dicoding.picodiploma.testgithub2.adapter.SectionsPagerAdapter
import com.dicoding.picodiploma.testgithub2.modal.UserResponse
import com.dicoding.picodiploma.testgithub2.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDetail()
        viewModel.isLoading.observe(this, {
            showLoading(it)
        })
        supportActionBar?.title = "Detail User"
    }

private fun tabLayout(user: UserResponse) {
    val sectionsPagerAdapter = SectionsPagerAdapter(this)
    sectionsPagerAdapter.login = user.login
    val viewPager: ViewPager2 = findViewById(R.id.view_pager)
    viewPager.adapter = sectionsPagerAdapter
    val tabs: TabLayout = findViewById(R.id.tabs)
    TabLayoutMediator(tabs, viewPager) { tab, position ->
        tab.text = resources.getString(TAB_TITLES[position])
    }.attach()
    supportActionBar?.elevation = 0f
}

    private fun getDetail() {
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR)
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)

        viewModel = ViewModelProvider( this).get(DetailViewModel::class.java)
        if (username != null) {
            viewModel.setUsersDetails(username)
        }
        viewModel.detailUser.observe( this, {
            if (it != null) {

                with(binding) {
                    Glide.with(root)
                        .load(it.avatarUrl)
                        .circleCrop()
                        .into(binding.imgItemPhoto)
                    tvItemName.text = it.name
                    tvItemUsername.text = it.login
                    tvItemCompany.text = it.company
                    tvItemLocation.text = it.location
                    tvItemRepository.text = it.publicRepos.toString()
                    tvItemFollowers.text = it.followers.toString()
                    tvItemFollowing.text = it.following.toString()
                }
            }
                if (it != null) {
                    tabLayout(it)
                }
        })

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val check = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if (check != null) {
                    if (check > 0) {
                        binding.fabFavorite.isChecked = true
                        _isChecked = true
                    } else {
                        binding.fabFavorite.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }
        binding.fabFavorite.apply {
            setOnClickListener{
                _isChecked = !_isChecked
                if (_isChecked){
                    if (username != null && avatarUrl != null ) {
                        viewModel.addToFavorite(username,id,avatarUrl)
                        Toast.makeText(
                            this@DetailUserActivity,
                            "${username} telah ditambahkan ke User Favorite",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    viewModel.removeFromFavorite(id)
                    Toast.makeText(
                        this@DetailUserActivity,
                        "${username} telah dihapus dari User Favorite ",
                        Toast.LENGTH_LONG
                    ).show()
                }
                binding.fabFavorite.isChecked = _isChecked
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"
        const val EXTRA_USERNAME = "extra_username"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}