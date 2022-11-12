package com.dicoding.picodiploma.testgithub2.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.picodiploma.testgithub2.modal.SearchResponse
import com.dicoding.picodiploma.testgithub2.modal.UserResponse
import com.dicoding.picodiploma.testgithub2.api.ApiConfig
import com.dicoding.picodiploma.testgithub2.ui.setting.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel (private val pref: SettingPreferences): ViewModel() {
    private val _user = MutableLiveData<ArrayList<UserResponse>>()
    val user: LiveData<ArrayList<UserResponse>> = _user

    private val _searchUser = MutableLiveData<ArrayList<UserResponse>?>()
    val searchUser: LiveData<ArrayList<UserResponse>?> = _searchUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getUser()
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    private fun getUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser()
        client.enqueue(object : Callback<ArrayList<UserResponse>> {
            override fun onResponse(
                call: Call<ArrayList<UserResponse>>,
                response: Response<ArrayList<UserResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.value = response.body()

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<UserResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getSearch(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUser(username)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _searchUser.value = response.body()?.items
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}

