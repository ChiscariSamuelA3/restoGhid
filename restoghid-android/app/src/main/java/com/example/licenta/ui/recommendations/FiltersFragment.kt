package com.example.licenta.ui.recommendations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.licenta.R
import com.example.licenta.databinding.FragmentFiltersBinding
import com.example.licenta.ui.auth.onboarding.skipped
import com.example.licenta.ui.restaurants.stateUpdate

class FiltersFragment : Fragment() {
    private lateinit var binding: FragmentFiltersBinding

    private val viewModel: FiltersViewModel by activityViewModels {
        FiltersViewModelFactory(requireContext())
    }

    private var fullServiceSelected: Boolean = false
    private var fastFoodSelected: Boolean = false
    private var pubSelected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_filters,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFullService.setOnClickListener {
            if (binding.btnFullService.currentTextColor == ContextCompat.getColor(
                    requireContext(),
                    R.color.text_secondary
                )
            ) {
                binding.btnFullService.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.text_selected
                    )
                )
                fullServiceSelected = true
            } else {
                binding.btnFullService.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.text_secondary
                    )
                )
                fullServiceSelected = false
            }
        }

        binding.btnFastFood.setOnClickListener {
            if (binding.btnFastFood.currentTextColor == ContextCompat.getColor(
                    requireContext(),
                    R.color.text_secondary
                )
            ) {
                binding.btnFastFood.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.text_selected
                    )
                )
                fastFoodSelected = true
            } else {
                binding.btnFastFood.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.text_secondary
                    )
                )
                fastFoodSelected = false
            }
        }

        binding.btnPub.setOnClickListener {
            if (binding.btnPub.currentTextColor == ContextCompat.getColor(
                    requireContext(),
                    R.color.text_secondary
                )
            ) {
                binding.btnPub.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.text_selected
                    )
                )
                pubSelected = true
            } else {
                binding.btnPub.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.text_secondary
                    )
                )
                pubSelected = false
            }
        }

        binding.btnNext.setOnClickListener {
            stateUpdate = true

            val action =
                FiltersFragmentDirections.actionFiltersToRestaurants(fullServiceSelected, fastFoodSelected, pubSelected, 0)
            view.findNavController().navigate(action)
        }

        if(skipped) {
            binding.tvRecommendations.visibility = View.GONE
            binding.tvOr.visibility = View.GONE
        }
        else {
            binding.tvRecommendations.visibility = View.VISIBLE
            binding.tvOr.visibility = View.VISIBLE
        }

        binding.tvRecommendations.setOnClickListener {
            stateUpdate = true

            val action =
                FiltersFragmentDirections.actionFiltersToRestaurants(type = 1)
            view.findNavController().navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()

        fullServiceSelected = false
        fastFoodSelected = false
        pubSelected = false
    }
}