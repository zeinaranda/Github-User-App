package com.dicoding.picodiploma.testgithub2.ui.detail

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.picodiploma.testgithub2.modal.UserResponse
import com.dicoding.picodiploma.testgithub2.api.ApiConfig
import com.dicoding.picodiploma.testgithub2.database.FavoriteDatabase
import com.dicoding.picodiploma.testgithub2.database.FavoriteUser
import com.dicoding.picodiploma.testgithub2.database.FavoriteUserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel (application: Application) : AndroidViewModel(application) {
    private val _userDetail = MutableLiveData<UserResponse?>()
    val detailUser: LiveData<UserResponse?> = _userDetail
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var userDAO: FavoriteUserDAO?
    private var FavDB: FavoriteDatabase?

    init {
        FavDB = FavoriteDatabase.getDatabase(application)
        userDAO = FavDB?.favoriteUserDAO()
    }

    fun setUsersDetails(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userDetail.value = response.body()

                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value=false
                Log.d("Failure", t.message.toString())
            }
        })
    }

    fun addToFavorite(username: String, id: Int, avatarurl: String){
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteUser(
                id,
                username,
                avatarurl,
            )
            userDAO?.insert(user)
        }
    }

    fun checkUser(id: Int) = userDAO?.checkFavoriteUser(id)

    fun removeFromFavorite(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDAO?.deleteFavoriteUser(id)
        }
    }
}