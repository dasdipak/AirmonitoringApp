package com.app.cementfactory.ui

import com.app.cementfactory.repository.UserRepo
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class LoginTest {

    @Test
    fun loginTest() = runBlocking {
        val repo = UserRepo()
        val response = repo.letMeLogin("arjun7", "arjun7")
        val expected = true
        val actual = response.success
        Assert.assertEquals(expected, actual)
    }
}