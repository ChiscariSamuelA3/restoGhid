package com.example.licenta.ui.manageinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.licenta.R
import com.example.licenta.databinding.FragmentManageInfoBinding
import com.example.licenta.dto.RestaurantUpdateDTO

class ManageInfoFragment : Fragment() {
    private lateinit var binding: FragmentManageInfoBinding
    private val viewModel: ManageInfoViewModel by activityViewModels {
        ManageInfoViewModelFactory(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_manage_info,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUpdate.setOnClickListener {
            val restaurantDTO = RestaurantUpdateDTO(
                binding.etName.text.toString(),
                binding.etDescription.text.toString(),
                binding.etAddress.text.toString(),
                binding.etPhone.text.toString(),
                binding.etSeats.text.toString().toInt()
            )
            viewModel.updateRestaurant(restaurantDTO)
        }

        viewModel.getRestaurantDetails().observe(viewLifecycleOwner) { restaurantDetails ->
            if (restaurantDetails != null) {
                Toast.makeText(requireContext(), "Restaurant actualizat!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}