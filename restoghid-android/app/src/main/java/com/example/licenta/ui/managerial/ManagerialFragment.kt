package com.example.licenta.ui.managerial

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
import com.example.licenta.databinding.FragmentManagerialBinding

class ManagerialFragment : Fragment() {
    private lateinit var binding: FragmentManagerialBinding
    private val viewModel: ManagerialViewModel by activityViewModels {
        ManagerialViewModelFactory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_managerial, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ibUserLogout.setOnClickListener {
            viewModel.clearPreferences()
            val action = ManagerialFragmentDirections.actionManagerialFragmentToAuthFragment()
            view.findNavController().navigate(action)
        }

        binding.btnManageInfo.setOnClickListener {
            val action = ManagerialFragmentDirections.actionManagerialFragmentToManageInfoFragment()
            view.findNavController().navigate(action)
        }

        binding.btnManageBookings.setOnClickListener {
            val action =
                ManagerialFragmentDirections.actionManagerialFragmentToManageBookingsFragment()
            view.findNavController().navigate(action)
        }

        binding.btnManageReviews.setOnClickListener {
            val action =
                ManagerialFragmentDirections.actionManagerialFragmentToManageReviewsFragment()
            view.findNavController().navigate(action)
        }
    }

}