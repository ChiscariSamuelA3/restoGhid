package com.example.licenta.ui.managebookings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.licenta.R
import com.example.licenta.databinding.FragmentManageBookingsBinding

class ManageBookingsFragment : Fragment() {
    private lateinit var binding: FragmentManageBookingsBinding
    private val viewModel: ManageBookingsViewModel by activityViewModels {
        ManageBookingsViewModelFactory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_manage_bookings,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadRestaurantDetailsByManager()

        val adapter = ManageBookingsAdapter(viewModel)
        binding.rvBookings.adapter = adapter

        viewModel.getRestaurantDetails().observe(viewLifecycleOwner) { restaurantDetails ->
            if (restaurantDetails != null) {
                binding.tvRestaurantName.text = restaurantDetails.name
                adapter.setBookings(restaurantDetails.bookings)
            }
        }
    }
}