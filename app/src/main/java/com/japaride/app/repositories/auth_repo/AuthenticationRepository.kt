package com.japaride.app.repositories.auth_repo

import com.japaride.app.network.api.auth_api.AuthApi
import com.japaride.app.network.data.UserPreferences
import com.japaride.app.network.models.registration_model.RegistrationModel
import com.japaride.app.network.models.login_model.LoginModel
import com.japaride.app.network.models.user_location_model.UserLocationModel
import com.japaride.app.repositories.BaseRepository
import retrofit2.http.Body
import retrofit2.http.PUT

class AuthenticationRepository(
    private val api : AuthApi,
    private val userPreferences: UserPreferences

): BaseRepository(){

    suspend fun registerUser(

        registrationModel: RegistrationModel
    ) = safeApiCall{

        api.registerUser(registrationModel)

    }


    suspend fun userLogin(
        loginModel: LoginModel
    ) = safeApiCall {

        api.userLogin(loginModel)
    }


    suspend fun locationUpdate(

        id: Int,
      locationModel: UserLocationModel

    ) = safeApiCall {

        api.locationUpdate(id, locationModel)
    }

    suspend fun saveAuthToken(token: String){
        userPreferences.safeAuthToken(token)
    }


    suspend fun saveUserId(userId: String){

        userPreferences.saveUserId(userId)

    }

}