package com.example.licenta.ui.restaurantdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.licenta.R
import com.example.licenta.databinding.FragmentRestaurantDetailsBinding
import com.example.licenta.ui.restaurants.stateUpdate
import com.squareup.picasso.Picasso

const val KEY_GOOGLE_MAPS_URL = "https://www.google.com/maps/dir/?api=1&destination="

class RestaurantDetailsFragment : Fragment() {
    private lateinit var binding: FragmentRestaurantDetailsBinding

    private var restaurantName = ""

    private val viewModel: RestaurantDetailsViewModel by activityViewModels {
        RestaurantDetailsViewModelFactory(requireContext())
    }

    private val args: RestaurantDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_restaurant_details,
                container,
                false
            )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stateUpdate = false

        val selectedMarkerId = args.markerId

        viewModel.loadRestaurantDetails(selectedMarkerId)

        val adapterCuisine = RestaurantDetailsAdapter(0)
        binding.rvCuisine.adapter = adapterCuisine

        val adapterDietary = RestaurantDetailsAdapter(1)
        binding.rvDietary.adapter = adapterDietary

        binding.ivExpandableCuisine.setOnClickListener {
            if (binding.cvCuisine.visibility == View.VISIBLE) {
                binding.cvCuisine.visibility = View.GONE
                binding.ivExpandableCuisine.setImageResource(R.drawable.ic_more)
            } else {
                binding.cvCuisine.visibility = View.VISIBLE
                binding.ivExpandableCuisine.setImageResource(R.drawable.ic_less)
            }
        }

        binding.ivExpandableDietary.setOnClickListener {
            if (binding.cvDietary.visibility == View.VISIBLE) {
                binding.cvDietary.visibility = View.GONE
                binding.ivExpandableDietary.setImageResource(R.drawable.ic_more)
            } else {
                binding.cvDietary.visibility = View.VISIBLE
                binding.ivExpandableDietary.setImageResource(R.drawable.ic_less)
            }
        }

        binding.tvReviews.setOnClickListener {
            val action =
                RestaurantDetailsFragmentDirections.actionRestaurantDetailsFragmentToReviewsFragment(
                    args.markerId,
                    restaurantName
                )
            view.findNavController().navigate(action)
        }

        viewModel.getErrorLiveData().observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getRestaurantDetails().observe(viewLifecycleOwner) { restaurant ->
            if (restaurant != null) {
                val imageUri = restaurant.image
                Picasso.get().load(imageUri).into(binding.imgRestaurant)

                adapterCuisine.setCuisineList(restaurant.cuisine)
                adapterDietary.setDietaryList(restaurant.dietaryRestrictions)

                restaurantName = restaurant.name

                binding.ivDirections.setOnClickListener {
                    val url =
                        "$KEY_GOOGLE_MAPS_URL${restaurant.latitude},${restaurant.longitude}"
                    val intent = Intent(Intent.ACTION_VIEW)

                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }

                binding.btnBooking.setOnClickListener {
                    val action =
                        RestaurantDetailsFragmentDirections.actionRestaurantDetailsFragmentToBookingFragment(
                            args.markerId
                        )
                    view.findNavController().navigate(action)
                }
            }
        }
    }
}