package com.app.cementfactory.response

import com.app.cementfactory.entity.User

data class LoginResponse (
    val success: Boolean? = null,
    val data: User? = null,
    val token: String? = null
)