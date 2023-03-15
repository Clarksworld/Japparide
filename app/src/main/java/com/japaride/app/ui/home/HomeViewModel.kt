package com.japaride.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.japaride.app.network.api.Resource
import com.japaride.app.network.models.login_model.LoginModel
import com.japaride.app.network.models.registration_model.RegistrationModel
import com.japaride.app.network.models.user_location_model.UserLocationModel
import com.japaride.app.network.responses.location_update_response.LocationUpdateResponse
import com.japaride.app.repositories.auth_repo.AuthenticationRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AuthenticationRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _locationUpdateResponse: MutableLiveData<Resource<LocationUpdateResponse>> = MutableLiveData()
    val locationUpdateResponse: LiveData<Resource<LocationUpdateResponse>> get() = _locationUpdateResponse


    fun locationUpdate(
        id: Int,
        locationModel: UserLocationModel

    ) = viewModelScope.launch {

        _locationUpdateResponse.value = Resource.Loading
        _locationUpdateResponse.value = repository.locationUpdate(id, locationModel)

    }
}