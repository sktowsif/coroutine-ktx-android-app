package com.example.profiles.data

import retrofit2.http.GET

interface RemoteServerAPI {

    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("albums")
    suspend fun getAlbums(): List<Album>

}