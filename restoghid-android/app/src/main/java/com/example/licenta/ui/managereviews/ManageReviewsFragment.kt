package com.example.licenta.ui.managereviews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.licenta.R
import com.example.licenta.databinding.FragmentManageReviewsBinding

class ManageReviewsFragment : Fragment() {
    private lateinit var binding: FragmentManageReviewsBinding
    private val viewModel: ManageReviewsViewModel by activityViewModels {
        ManageReviewsViewModelFactory(requireContext())
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
            R.layout.fragment_manage_reviews,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadRestaurantReviews()

        val adapter = ManageReviewsAdapter(viewModel)
        binding.rvReviews.adapter = adapter

        viewModel.getErrorLiveData().observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getReviews().observe(viewLifecycleOwner) { reviews ->
            if (reviews != null) {
                adapter.setReviews(reviews)
            }
        }
    }
}