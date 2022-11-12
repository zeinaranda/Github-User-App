package com.dicoding.picodiploma.testgithub2.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_user")
data class FavoriteUser (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "username")
    val login: String,

    @ColumnInfo(name = "avatar")
    val avatar_url: String
): Serializable

