package com.japaride.app.network.responses

data class RegistrationResponse(
    val jwt: String,
    val user: User
)