package com.app.cementfactory.response

import com.app.cementfactory.entity.User


data class CurrentUserResponse (
    val success: Boolean? = null,
    val data: Array<User>? = null,
)