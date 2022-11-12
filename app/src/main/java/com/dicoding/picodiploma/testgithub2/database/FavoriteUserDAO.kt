package com.dicoding.picodiploma.testgithub2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteUserDAO {
    @Insert
    fun insert (favoritUser: FavoriteUser)

    @Query("SELECT * from favorite_user")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT count(*) FROM favorite_user WHERE favorite_user.id = :id")
    fun checkFavoriteUser(id: Int): Int

    @Query("DELETE FROM favorite_user WHERE favorite_user.id = :id")
    fun deleteFavoriteUser(id: Int): Int

}