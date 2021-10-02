package com.app.cementfactory.repository

import com.app.cementfactory.api.APIRequest
import com.app.cementfactory.api.IUserApi
import com.app.cementfactory.api.ServiceBuilder
import com.app.cementfactory.entity.User
import com.app.cementfactory.response.*
import okhttp3.MultipartBody

class UserRepo : APIRequest() {

    private val userAPI = ServiceBuilder.buildService(IUserApi::class.java);

    //add user
    suspend fun register(user: User): UserResponse {
        return apiRequest {
            userAPI.register(user);
        }
    }

    suspend fun letMeLogin(username: String, password: String): LoginResponse {
        return apiRequest {
            userAPI.letMeLogin(username, password)
        }
    }

    //images
    suspend fun uploadImage(id: String, body: MultipartBody.Part): ImageResponse {
        return apiRequest {
            userAPI.uploadImage(id, body)
        }
    }

    //images update
    suspend fun updateImage(id: String, body: MultipartBody.Part): ImageResponse {
        return apiRequest {
            userAPI.updateImage(id, body)
        }
    }

    suspend fun updatePassword(id: String, user: User): UserResponse {
        return apiRequest {
            userAPI.updatePassword(id, user)
        }
    }

    suspend fun updateProfile(id: String, user: User): UserResponse {
        return apiRequest {
            userAPI.updateProfile(id, user)
        }
    }

    suspend fun getProfile(id: String): UserResponse {
        return apiRequest {
            userAPI.getProfile(id)
        }
    }
    suspend fun getUser(id: String): CurrentUserResponse {
        return apiRequest {
            userAPI.getUser(id)
        }
    }
    suspend fun updateGoal(id:String, purpose : String) : UserResponse {
        return apiRequest {
            userAPI.updateGoal(id,purpose)
        }
    }

}