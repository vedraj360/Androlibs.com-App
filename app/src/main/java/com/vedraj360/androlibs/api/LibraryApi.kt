package com.vedraj360.androlibs.api

import com.vedraj360.androlibs.models.AndrolibModel
import retrofit2.http.GET
import retrofit2.http.Query

interface LibraryApi {
    @GET("libraries")
    suspend fun getLibraries(): AndrolibModel


    @GET("libraries")
    suspend fun getLibraryNext(
        @Query("next") next: String,
        @Query("limit") limit: Int

    ): AndrolibModel

    @GET("libraries")
    suspend fun getLibraryPrev(
        @Query("previous") previous: String,
        @Query("limit") limit: Int
    ): AndrolibModel


    @GET("search")
    suspend fun searchLibraries(
        @Query("term") query: String
    ): AndrolibModel
}