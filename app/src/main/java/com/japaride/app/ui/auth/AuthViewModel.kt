package com.japaride.app.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.japaride.app.network.api.Resource
import com.japaride.app.network.models.registration_model.RegistrationModel
import com.japaride.app.network.models.login_model.LoginModel
import com.japaride.app.network.models.user_location_model.UserLocationModel
import com.japaride.app.network.responses.RegistrationResponse
import com.japaride.app.network.responses.location_update_response.LocationUpdateResponse
import com.japaride.app.network.responses.login.LoginResponse
import com.japaride.app.repositories.auth_repo.AuthenticationRepository
import kotlinx.coroutines.launch

class AuthViewModel(private  val repository: AuthenticationRepository): ViewModel(){


    private val _registrationResponse: MutableLiveData<Resource<RegistrationResponse>> = MutableLiveData()
    val registrationResponse: LiveData<Resource<RegistrationResponse>> get() = _registrationResponse


    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>> get() = _loginResponse

    private val _locationUpdateResponse: MutableLiveData<Resource<LocationUpdateResponse>> = MutableLiveData()
    val locationUpdateResponse: LiveData<Resource<LocationUpdateResponse>> get() = _locationUpdateResponse


    fun registerUser(

        registrationModel: RegistrationModel

    ) = viewModelScope.launch {

        _registrationResponse.value = Resource.Loading
        _registrationResponse.value = repository.registerUser(registrationModel)

    }


     fun userLogin(
        loginModel: LoginModel
    ) = viewModelScope.launch {

         _loginResponse.value = Resource.Loading
         _loginResponse.value = repository.userLogin(loginModel)


     }

    fun locationUpdate(
        id: Int,
        locationModel: UserLocationModel

    ) = viewModelScope.launch {

    _locationUpdateResponse.value = Resource.Loading
        _locationUpdateResponse.value = repository.locationUpdate(id, locationModel)

    }
    fun saveAuthToken(token: String) =
        viewModelScope.launch{

            repository.saveAuthToken(token)
        }


    fun saveUserId(userId: String) = viewModelScope.launch{

        repository.saveUserId(userId)

    }

}