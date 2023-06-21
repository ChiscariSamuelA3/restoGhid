package com.example.licenta.ui.reviews

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.licenta.R
import com.example.licenta.databinding.FragmentReviewsBinding
import com.example.licenta.models.Review
import com.example.licenta.ui.restaurantdetails.RestaurantDetailsFragmentArgs
import com.example.licenta.ui.restaurantdetails.RestaurantDetailsViewModel
import com.example.licenta.ui.restaurantdetails.RestaurantDetailsViewModelFactory
import com.example.licenta.ui.restaurants.stateUpdate

class ReviewsFragment : Fragment() {
    private lateinit var binding: FragmentReviewsBinding

    private val viewModel: ReviewsViewModel by activityViewModels {
        ReviewsViewModelFactory(requireContext())
    }

    private val args: ReviewsFragmentArgs by navArgs()
    private var selectedScore: Double = 5.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_reviews, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val restaurantId = args.restaurantId
        val restaurantName = args.restaurantName

        binding.tvLocationName.text = restaurantName

        viewModel.loadRestaurantReviews(restaurantId)

        val adapter = ReviewsAdapter()
        binding.rvReviews.adapter = adapter

        val scores = resources.getStringArray(R.array.Score)
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, scores)
        binding.spinner.adapter = spinnerAdapter

        binding.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedScore = scores[position].toDouble()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.btnAddReview.setOnClickListener {
            viewModel.addReview(
                args.restaurantId,
                Review(content = binding.etReviewContent.text.toString(), score = selectedScore)
            )
        }

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

        viewModel.getNewReview().observe(viewLifecycleOwner) { review ->
            if (review != null) {
                viewModel.loadRestaurantReviews(args.restaurantId)
            }
        }
    }
}