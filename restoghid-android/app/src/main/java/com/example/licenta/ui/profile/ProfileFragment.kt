package com.example.licenta.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.licenta.R
import com.example.licenta.databinding.FragmentProfileBinding
import com.example.licenta.ui.restaurants.RestaurantsFragmentDirections
import com.example.licenta.ui.restaurants.stateUpdate

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by activityViewModels {
        ProfileViewModelFactory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_profile, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stateUpdate = false

        viewModel.loadUserData()

        val adapter = ProfileAdapter(viewModel)
        binding.rvBookings.adapter = adapter

        viewModel.getUserData().observe(viewLifecycleOwner) { userData ->
            if (userData != null) {
                adapter.setBookings(userData.bookings)
            }
        }

        binding.ibUserLogout.setOnClickListener {
            viewModel.clearPreferences()

            val action = ProfileFragmentDirections.actionProfileFragmentToAuthFragment()

            view.findNavController().navigate(action)
        }
    }
}