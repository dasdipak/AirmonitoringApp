package com.app.cementfactory.response

import com.app.cementfactory.entity.User

data class UserResponse (
    val success: Boolean? = null,
    val data: User? = null,
)