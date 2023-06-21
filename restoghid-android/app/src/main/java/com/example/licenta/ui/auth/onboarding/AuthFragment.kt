package com.example.licenta.ui.auth.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.licenta.R
import com.example.licenta.databinding.FragmentAuthBinding

var skipped = false

class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding

    private val viewModel: AuthViewModel by activityViewModels {
        AuthViewModelFactory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.clearPreferences()

        if(viewModel.checkLoginState()) {
            val action = AuthFragmentDirections.actionAuthFragmentToRecommendationsFragment()
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_auth, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSkip.setOnClickListener {
            skipped = true

            val action = AuthFragmentDirections.actionAuthFragmentToRecommendationsFragment()
            view.findNavController().navigate(action)
        }

        binding.btnLogin.setOnClickListener {
            val action = AuthFragmentDirections.actionAuthFragmentToLoginFragment()
            view.findNavController().navigate(action)
        }

        binding.btnSignup.setOnClickListener {
            val action = AuthFragmentDirections.actionAuthFragmentToRegisterFragment()
            view.findNavController().navigate(action)
        }
    }
}