package com.example.licenta.ui.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.licenta.R
import com.example.licenta.databinding.FragmentRegisterBinding
import com.example.licenta.ui.restaurants.RestaurantsFragmentDirections

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private val viewModel: RegisterViewModel by activityViewModels {
        RegisterViewModelFactory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_register, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRegisterResponse().observe(viewLifecycleOwner) { response ->
            if (response != null) {
                Log.d("_USER", response.toString())
                val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                view.findNavController().navigate(action)
            }
        }

        binding.btnRegisterSubmit.setOnClickListener {
            val username = binding.etRegisterName.text.toString()
            val email = binding.etRegisterEmail.text.toString()
            val password = binding.etRegisterPassword.text.toString()
            val confirmPassword = binding.etRegisterPasswordConfirm.text.toString()

            // check if fields are empty
            if (username.isEmpty()) {
                binding.tvNameError.visibility = View.VISIBLE
                return@setOnClickListener
            } else {
                binding.tvNameError.visibility = View.GONE
            }

            if (email.isEmpty()) {
                binding.tvEmailError.visibility = View.VISIBLE
                return@setOnClickListener
            } else {
                binding.tvEmailError.visibility = View.GONE
            }

            if (password.isEmpty()) {
                binding.tvPasswordError.visibility = View.VISIBLE
                return@setOnClickListener
            } else {
                binding.tvPasswordError.visibility = View.GONE
            }

            if (confirmPassword.isEmpty()) {
                binding.tvPasswordError.visibility = View.VISIBLE
                return@setOnClickListener
            } else {
                binding.tvPasswordError.visibility = View.GONE
            }

            if (password != confirmPassword) {
                binding.tvPasswordError.visibility = View.VISIBLE
                return@setOnClickListener
            } else {
                binding.tvPasswordError.visibility = View.GONE
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tvEmailError.visibility = View.VISIBLE
                return@setOnClickListener
            } else {
                binding.tvEmailError.visibility = View.GONE
            }

            if (password.length < 6) {
                binding.tvPasswordError.visibility = View.VISIBLE
                return@setOnClickListener
            } else {
                binding.tvPasswordError.visibility = View.GONE
            }

            viewModel.register(username, email, password)
        }
    }

}