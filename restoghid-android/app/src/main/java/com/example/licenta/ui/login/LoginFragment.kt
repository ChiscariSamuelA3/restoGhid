package com.example.licenta.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.licenta.R
import com.example.licenta.databinding.FragmentLoginBinding
import com.example.licenta.ui.auth.onboarding.skipped

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by activityViewModels {
        LoginViewModelFactory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLoginResponse().observe(viewLifecycleOwner) { response ->
            if (response != null && viewModel.checkLoginState()) {
                skipped = false
                if (!viewModel.checkIsManager()) {
                    val action =
                        LoginFragmentDirections.actionLoginFragmentToRecommendationsFragment()
                    view.findNavController().navigate(action)
                } else {
                    val action = LoginFragmentDirections.actionLoginFragmentToManagerialFragment()
                    view.findNavController().navigate(action)
                }
            }
        }

        binding.btnLoginSubmit.setOnClickListener {
            val username = binding.etLoginUsername.text.toString()
            val password = binding.etLoginPassword.text.toString()

            if (username.isEmpty()) {
                binding.tvEmailError.visibility = View.VISIBLE

                return@setOnClickListener
            }
            else {
                binding.tvEmailError.visibility = View.GONE
            }

            if (password.isEmpty()) {
                binding.tvPasswordError.visibility = View.VISIBLE

                return@setOnClickListener
            }
            else {
                binding.tvPasswordError.visibility = View.GONE
            }

            viewModel.login(username, password)
        }
    }
}