package com.japaride.app.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.japaride.app.MainActivity
import com.japaride.app.R
import com.japaride.app.databinding.FragmentLoginBinding
import com.japaride.app.network.api.Resource
import com.japaride.app.network.api.auth_api.AuthApi
import com.japaride.app.network.models.login_model.LoginModel
import com.japaride.app.repositories.auth_repo.AuthenticationRepository
import com.japaride.app.ui.auth.AuthViewModel
import com.japaride.app.ui.auth.AuthenticationActivity
import com.japaride.app.ui.base.BaseFragment
import com.japaride.app.visible

class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding,
        AuthenticationRepository>(){

    private val args: LoginFragmentArgs by navArgs()

    var email:String? = null
    var password:String? = null

    var errorMessage = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        viewModel.loginResponse.observe(viewLifecycleOwner){

            when(it){

                is Resource.Success -> {
                    binding.loginProgress.visible(false)

                    Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()

                    var userEmail = viewModel.saveUserId(it.value.user.id.toString()).toString()
                    var userPassword = viewModel.saveAuthToken(it.value.jwt).toString()


                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()


                }

                is Resource.Error -> {
                    binding.loginProgress.visible(false)

                    Toast.makeText(requireContext(), " $it", Toast.LENGTH_LONG).show()


                }

                is Resource.Loading -> {

                    binding.loginProgress.visible(true)

                }
            }
        }

        binding.loginButton.setOnClickListener {

            email = binding.enterEmailEd.text.toString().trim()
            password = binding.enterPasswordEd.text.toString().trim()

            if (email!!.isEmpty()){

                errorMessage = "User must enter a valid email address"
                errorBottomSheet(errorMessage!!)

            }else if (password!!.isEmpty()){

                errorMessage = "User must enter a their password"
                errorBottomSheet(errorMessage!!)

            }else{

                viewModel.userLogin(
                    LoginModel(
                        email!!,
                        password!!
                    )
                )

            }

        }

        binding.navLogin.setOnClickListener {

            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }


    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = AuthenticationRepository(
        remoteDataSource.auth(AuthApi::class.java), userPreference
    )


}