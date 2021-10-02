package com.app.cementfactory.api

import com.app.cementfactory.entity.User
import com.app.cementfactory.response.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface IUserApi {

    @POST("user/register")
    suspend fun register(
        @Body user: User
    ): Response<UserResponse>

    @FormUrlEncoded
    @POST("user/login")
    suspend fun letMeLogin(
        @Field("username") username: String,
        @Field("password") password: String

    ): Response<LoginResponse>

    @Multipart
    @POST("uploads/{id}")
    suspend fun uploadImage(
        @Path("id") id: String,
        @Part file: MultipartBody.Part
    ): Response<ImageResponse>

    @Multipart
    @POST("/updateImage/{id}")
    suspend fun updateImage(
        @Path("id") id: String,
        @Part file: MultipartBody.Part
    ): Response<ImageResponse>

    @PUT("profile/update/{id}")
    suspend fun updatePassword(
        @Path("id") id: String,
        @Body user: User
    ): Response<UserResponse>

    @PUT("profile/update/{id}")
    suspend fun updateProfile(
        @Path("id") id: String,
        @Body user: User
    ): Response<UserResponse>

    @GET("getProfile/{id}")
    suspend fun getProfile(
        @Path("id") id: String
    ): Response<UserResponse>

    @GET("/user/get/{id}")
    suspend fun getUser(
            @Path("id") id: String
    ): Response<CurrentUserResponse>
    @PUT("/goal/update/{id}")
    suspend fun updateGoal(
            @Path("id") id: String,
            @Body purpose: String
    ): Response<UserResponse>

}