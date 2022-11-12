package com.dicoding.picodiploma.testgithub2.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.picodiploma.testgithub2.database.FavoriteDatabase
import com.dicoding.picodiploma.testgithub2.database.FavoriteUser
import com.dicoding.picodiploma.testgithub2.database.FavoriteUserDAO

class FavoriteViewModel (application: Application) : AndroidViewModel(application) {
    private var userDAO: FavoriteUserDAO?
    private var FavDB: FavoriteDatabase?

    init {
        FavDB = FavoriteDatabase.getDatabase(application)
        userDAO = FavDB?.favoriteUserDAO()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return userDAO?.getAllFavoriteUser()
    }
}