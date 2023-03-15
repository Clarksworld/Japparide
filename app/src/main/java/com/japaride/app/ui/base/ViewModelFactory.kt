package com.japaride.app.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.japaride.app.repositories.BaseRepository
import com.japaride.app.repositories.auth_repo.AuthenticationRepository
import com.japaride.app.ui.auth.AuthViewModel
import com.japaride.app.ui.home.HomeViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(
    private val repository: BaseRepository
): ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  return when {

            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repository as AuthenticationRepository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository as AuthenticationRepository) as T



            else -> throw IllegalArgumentException("ViewModelClass Not Fount")
        }
    }
}