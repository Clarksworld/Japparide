package com.japaride.app.network.api.auth_api

import com.japaride.app.network.models.registration_model.RegistrationModel
import com.japaride.app.network.models.login_model.LoginModel
import com.japaride.app.network.models.user_location_model.UserLocationModel
import com.japaride.app.network.responses.RegistrationResponse
import com.japaride.app.network.responses.location_update_response.LocationUpdateResponse
import com.japaride.app.network.responses.login.LoginResponse
import retrofit2.http.*

interface AuthApi {


    @POST("api/auth/local/register")
    suspend fun registerUser(
        @Body registrationModel: RegistrationModel
    ): RegistrationResponse


    @POST("api/auth/local")
    suspend fun userLogin(
        @Body loginModel: LoginModel
    ): LoginResponse

    @PUT("api/users/")
    suspend fun locationUpdate(
        @Header("id") id: Int, @Body locationModel: UserLocationModel
    ): LocationUpdateResponse


}