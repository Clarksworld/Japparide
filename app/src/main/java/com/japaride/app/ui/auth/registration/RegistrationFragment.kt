package com.japaride.app.ui.auth.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.japaride.app.R
import com.japaride.app.databinding.FragmentRegistrationBinding
import com.japaride.app.enable
import com.japaride.app.network.api.Resource
import com.japaride.app.network.api.auth_api.AuthApi
import com.japaride.app.network.models.registration_model.RegistrationModel
import com.japaride.app.repositories.auth_repo.AuthenticationRepository
import com.japaride.app.ui.auth.AuthViewModel
import com.japaride.app.ui.base.BaseFragment
import com.japaride.app.visible


class RegistrationFragment : BaseFragment<AuthViewModel, FragmentRegistrationBinding,
        AuthenticationRepository>(){

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()


        var fullName:String? = null
        var phoneNumber:String? = null
        var email:String? = null
        var password:String? = null

        var errorMessage:String? = null

        viewModel.registrationResponse.observe(viewLifecycleOwner){

            when(it){

                is Resource.Success ->{
                    binding.signUpProgress.visible(false)

                    val directions = RegistrationFragmentDirections.
                    actionRegistrationFragmentToLoginFragment(email!!)
                    findNavController().navigate(directions)

                    Toast.makeText(requireContext(), "Registration Successful", Toast.LENGTH_LONG).show()
                }

                is Resource.Error ->{

                    binding.signUpProgress.visible(false)
                    Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()



                }

                is Resource.Loading ->{

                    binding.signUpProgress.visible(true)




                }
            }
        }



        binding.enterPasswordEd.addTextChangedListener{


            binding.signUpButton.enable(
                it.toString().isNotEmpty())
        }

        binding.signUpButton.setOnClickListener {

            fullName = binding.enterFullNameEd.text.toString().trim()
            phoneNumber = binding.enterPhoneNumberEd.text.toString().trim()
            email = binding.enterEmailEd.text.toString().trim()
            password = binding.enterPasswordEd.text.toString().trim()

            if (email!!.isEmpty()){

                errorMessage = "User must enter a valid email address"
                errorBottomSheet(errorMessage!!)

            } else if (fullName!!.isEmpty()){

                errorMessage = "User must enter a valid name"
                errorBottomSheet(errorMessage!!)

            } else if (phoneNumber!!.isEmpty()){

                errorMessage = "User must enter a valid phone number"
                errorBottomSheet(errorMessage!!)

            }else if (password!!.isEmpty() || password!!.length < 8 ){

                errorMessage = "User must enter a valid password and password must be more than 8 characters"
                errorBottomSheet(errorMessage!!)

            }else{


                viewModel.registerUser(
                    RegistrationModel(
                        fullName!!,
                        email!!,
                        phoneNumber!!,
                        password!!
                    )
                )

            }




        }

        binding.navLogin.setOnClickListener {

            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegistrationBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = AuthenticationRepository(remoteDataSource.auth(AuthApi::class.java), userPreference)


}