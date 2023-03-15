package com.japaride.app.network.responses.login

data class LoginResponse(
    val jwt: String,
    val user: User
)