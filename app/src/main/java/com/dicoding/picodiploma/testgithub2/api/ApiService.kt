package com.dicoding.picodiploma.testgithub2.api

import com.dicoding.picodiploma.testgithub2.BuildConfig
import com.dicoding.picodiploma.testgithub2.modal.SearchResponse
import com.dicoding.picodiploma.testgithub2.modal.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    @Headers("Authorization: $API_TOKEN", "UserResponse-Agent: request")
    fun getUser(
    ): Call<ArrayList<UserResponse>>

    @GET("users/{username}")
    @Headers("Authorization: $API_TOKEN", "UserResponse-Agent: request")
    fun getDetailUser(
        @Path("username") username: String): Call<UserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: $API_TOKEN", "UserResponse-Agent: request")
    fun getFollower(
        @Path("username") username: String
    ): Call<ArrayList<UserResponse>>

    @GET("users/{username}/following")
    @Headers("Authorization: $API_TOKEN", "UserResponse-Agent: request")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<UserResponse>>

    @GET("search/users")
    @Headers("Authorization: $API_TOKEN", "UserResponse-Agent: request")
    fun getSearchUser (
        @Query("q") username: String): Call<SearchResponse>

    companion object {
        private const val API_TOKEN = BuildConfig.API_TOKEN
    }
}