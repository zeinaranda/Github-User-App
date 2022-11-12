package com.dicoding.picodiploma.testgithub2.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.testgithub2.R
import com.dicoding.picodiploma.testgithub2.modal.UserResponse
import com.dicoding.picodiploma.testgithub2.adapter.ListUserAdapter
import com.dicoding.picodiploma.testgithub2.databinding.ActivityMainBinding
import com.dicoding.picodiploma.testgithub2.ui.detail.DetailUserActivity
import com.dicoding.picodiploma.testgithub2.ui.favorite.FavoriteUserActivity
import com.dicoding.picodiploma.testgithub2.ui.setting.SettingActivity
import com.dicoding.picodiploma.testgithub2.ui.setting.SettingPreferences


class MainActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: ActivityMainBinding
    var userAdapter: ListUserAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, MainViewModelFactory(pref))[MainViewModel::class.java]
        mainViewModel.getThemeSettings().observe(this@MainActivity,{isDarkModeActive ->
            if (isDarkModeActive) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        })

        mainViewModel.user.observe(this, { user ->
            userAdapter = user?.let { ListUserAdapter(it) }
            showRecyclerList()
        })
        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })
        supportActionBar?.title = "List Users"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.getSearch(query)
                mainViewModel.searchUser.observe(this@MainActivity, { searchUserResponse ->
                    userAdapter = searchUserResponse?.let { ListUserAdapter(it) }
                    showRecyclerList()
                })
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                Intent(this, FavoriteUserActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.setting -> {
                Intent(this, SettingActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRecyclerList() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUsers.layoutManager = GridLayoutManager(this@MainActivity, 2)
        } else {
            binding.rvUsers.layoutManager = LinearLayoutManager(this@MainActivity)
        }
        binding.rvUsers.adapter = userAdapter
        userAdapter?.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserResponse) {
                val intentToDetail = Intent(this@MainActivity, DetailUserActivity::class.java)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_ID,data.id)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_AVATAR,data.avatarUrl)
                startActivity(intentToDetail)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
