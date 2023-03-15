package com.japaride.app.network.responses

data class User(
    val blocked: Boolean,
    val confirmed: Boolean,
    val country_of_origin: Any,
    val createdAt: String,
    val current_city: Any,
    val current_country: Any,
    val current_location: Any,
    val email: String,
    val id: Int,
    val is_user_also_a_driver: Any,
    val phone_number: String,
    val phone_number_as_driver: Any,
    val provider: String,
    val updatedAt: String,
    val username: String
)