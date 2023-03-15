package com.japaride.app.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.japaride.app.network.api.RemoteDataSource
import com.japaride.app.network.data.UserPreferences
import com.japaride.app.repositories.BaseRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseFragment<VM: ViewModel, B: ViewBinding, R: BaseRepository>: Fragment() {

    protected lateinit var binding: B
    protected lateinit var viewModel: VM
    protected val remoteDataSource = RemoteDataSource()
    protected lateinit var userPreference: UserPreferences


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        userPreference = UserPreferences(requireContext())

        binding = getFragmentBinding(inflater, container)
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())

        lifecycleScope.launch { userPreference.authToken.first() }

        return binding.root
    }


    abstract fun getViewModel(): Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    abstract fun getFragmentRepository(): R


    fun errorBottomSheet(errorMessage: String){

        val errorDialog = BottomSheetDialog(requireContext())
        errorDialog.setContentView(com.japaride.app.R.layout.error_layout)

        val errorTxt = errorDialog.findViewById<TextView>(com.japaride.app.R.id.input_message)
        errorTxt?.text = errorMessage

        val errorButton = errorDialog.findViewById<Button>(com.japaride.app.R.id.input_dismiss_message)

        errorButton?.setOnClickListener {

            errorDialog.dismiss()
        }

        errorDialog.show()


    }
}