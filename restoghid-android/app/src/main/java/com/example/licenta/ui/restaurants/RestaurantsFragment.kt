package com.example.licenta.ui.restaurants

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.licenta.BuildConfig
import com.example.licenta.R
import com.example.licenta.databinding.FragmentRestaurantsBinding
import com.example.licenta.models.Filter
import com.example.licenta.ui.auth.onboarding.skipped
import com.example.licenta.util.CustomArrayAdapter
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.*

typealias PendingMapOperationCallback = (googleMap: GoogleMap) -> Unit

var stateUpdate = true

@SuppressLint("MissingPermission")
class RestaurantsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentRestaurantsBinding

    private val viewModel: RestaurantsViewModel by activityViewModels {
        RestaurantsViewModelFactory(requireContext())
    }

    private val args: RestaurantsFragmentArgs by navArgs()

    private var shouldUpdateMapLocation = false
    private lateinit var mapFragment: SupportMapFragment
    private var googleMap: GoogleMap? = null
    private val pendingMapOperations = mutableListOf<PendingMapOperationCallback>()
    private var filtersItems = mutableListOf<Filter>()

    private lateinit var locationRequest: LocationRequest

    private var selectedMarkerId: String = ""

    private val locationSettingsCallback =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                viewModel.startDeviceLocationUpdates()

                displayCurrentLocation()
            }
        }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrElse(Manifest.permission.ACCESS_FINE_LOCATION) { false } -> {
                displayCustomLocation()
            }
            permissions.getOrElse(Manifest.permission.ACCESS_COARSE_LOCATION) { false } -> {
                displayCustomLocation()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fullServiceSelected = args.fullService
        val fastFoodSelected = args.fastFood
        val pubSelected = args.pub

        filtersItems.add(Filter("Vegetarian", isChecked = false, isCuisine = false))
        filtersItems.add(Filter("Gluten Free", isChecked = false, isCuisine = false))
        filtersItems.add(Filter("Vegan", isChecked = false, isCuisine = false))
        filtersItems.add(Filter("Halal", isChecked = false, isCuisine = false))

        viewModel.loadCuisines()

        createLocationRequest()

        if (args.type == 0) {
            if (!fullServiceSelected && !fastFoodSelected && !pubSelected) {
                viewModel.loadRestaurants(true, true, true)
            } else {
                viewModel.loadRestaurants(fullServiceSelected, fastFoodSelected, pubSelected)
            }
        } else if (args.type == 1) {
            viewModel.loadRestaurantsByRecommendations()
        }

        // viewModel.loadFilters()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_restaurants, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RestaurantsAdapter(viewModel)
        binding.filterRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.filterRecyclerView.adapter = adapter

        viewModel.filters.observe(viewLifecycleOwner) { filters ->
            if (filters != null) {
                adapter.setFilters(filters)
            }
        }

        viewModel.getCuisines().observe(viewLifecycleOwner) { cuisines ->
            if (cuisines != null) {
                cuisines.forEach { cuisine -> filtersItems.add(Filter(cuisine, false)) }
                viewModel.setFilters(filtersItems)
            }
        }

        binding.ibFilter.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }

        viewModel.getErrorLiveData().observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                if (errorMessage == "TOKEN_EXPIRED") {
                    viewModel.clearPreferences()
                    val action =
                        RestaurantsFragmentDirections.actionRestaurantsFragmentToAuthFragment()
                    view.findNavController().navigate(action)
                }
            }
        }

        viewModel.getRestaurants().observe(viewLifecycleOwner) { restaurants ->
            if (restaurants != null) {
                displayRestaurantsData(restaurants)

                if (adapter.getSelectedFilters().isNotEmpty()) {
                    viewModel.loadRestaurantsFiltered(adapter.getSelectedFilters(), restaurants)
                }

                binding.btnApply.setOnClickListener {
                    // print name of the checked filters
                    adapter.getSelectedFilters().forEach { filter ->
                        Log.d("FILTER", filter.name)
                    }

                    viewModel.loadRestaurantsFiltered(adapter.getSelectedFilters(), restaurants)
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                }
            } else {
                mapFragment =
                    childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }

        viewModel.getFilteredRestaurants().observe(viewLifecycleOwner) { restaurants ->
            if (restaurants != null) {
                if (restaurants.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Nu a fost găsit niciun restaurant cu filtrele selectate",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    displayRestaurantsData(restaurants)
                }
            }
        }

        viewModel.getRestaurantDetails().observe(viewLifecycleOwner) { restaurant ->
            if (restaurant != null) {
                setDistanceFromUserToRestaurant(restaurant)
            }
        }

        binding.tvUsername.text = viewModel.username

        binding.tvUsername.setOnClickListener {
            if (skipped) {
                val action =
                    RestaurantsFragmentDirections.actionRestaurantsFragmentToAuthFragment()
                view.findNavController().navigate(action)
            } else {
                val action =
                    RestaurantsFragmentDirections.actionRestaurantsFragmentToProfileFragment()
                view.findNavController().navigate(action)
            }
        }

        binding.ibPerson.setOnClickListener {
            if (skipped) {
                val action =
                    RestaurantsFragmentDirections.actionRestaurantsFragmentToAuthFragment()
                view.findNavController().navigate(action)
            } else {
                val action =
                    RestaurantsFragmentDirections.actionRestaurantsFragmentToProfileFragment()
                view.findNavController().navigate(action)
            }
        }

        binding.ibLocation.setOnClickListener {
            handleLocationState()
        }

        binding.tvRestaurantDetails.setOnClickListener {
            if (skipped) {
                val action =
                    RestaurantsFragmentDirections.actionRestaurantsFragmentToAuthFragment()
                view.findNavController().navigate(action)
            } else {
                val action =
                    RestaurantsFragmentDirections.actionRestaurantsFragmentToRestaurantDetailsFragment(
                        selectedMarkerId
                    )
                view.findNavController().navigate(action)
            }
        }

        binding.btnBooking.setOnClickListener {
            if (skipped) {
                val action =
                    RestaurantsFragmentDirections.actionRestaurantsFragmentToAuthFragment()
                view.findNavController().navigate(action)
            } else {
                val action =
                    RestaurantsFragmentDirections.actionRestaurantsFragmentToBookingFragment(
                        selectedMarkerId
                    )
                view.findNavController().navigate(action)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDistanceFromUserToRestaurant(restaurant: RestaurantItemViewModel) {
        var distanceUserRestaurant = "distanță necunoscută"

        if (hasPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) && viewModel.isLocationEnabled()
        ) {
            lifecycleScope.launch {
                val location = viewModel.awaitDeviceLocation()
                val deviceLocation =
                    Location(LocationManager.GPS_PROVIDER).apply {
                        latitude = location.latitude
                        longitude = location.longitude
                    }
                val restaurantLocation =
                    Location(LocationManager.GPS_PROVIDER).apply {
                        latitude = restaurant.latitude
                        longitude = restaurant.longitude
                    }

                distanceUserRestaurant =
                    deviceLocation.distanceTo(restaurantLocation).toInt().toString()
                binding.tvDistance.text = "$distanceUserRestaurant m distanță"
            }
        } else {
            binding.tvDistance.text = distanceUserRestaurant
        }
    }

    private val markersWithLabels = mutableListOf<Marker>()
    private val markersWithoutLabels = mutableListOf<Marker>()

    @SuppressLint("SetTextI18n")
    private fun displayRestaurantsData(restaurants: List<RestaurantItemViewModel>) {
        executeMapOperation { googleMap ->
            googleMap.clear()

            if (stateUpdate) {
                if (hasPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) && viewModel.isLocationEnabled()
                ) {
                    viewModel.startDeviceLocationUpdates()

//                    var distanceUserRestaurant = " - "

                    lifecycleScope.launch {
                        val location = viewModel.awaitDeviceLocation()
                        val deviceLocation =
                            Location(LocationManager.GPS_PROVIDER).apply {
                                latitude = location.latitude
                                longitude = location.longitude
                            }

                        val closestRestaurant = restaurants.minBy { restaurant ->
                            val restaurantLocation =
                                Location(LocationManager.GPS_PROVIDER).apply {
                                    latitude = restaurant.latitude
                                    longitude = restaurant.longitude
                                }

                            deviceLocation.distanceTo(restaurantLocation)
                        }

                        selectedMarkerId = closestRestaurant.id
                        viewModel.loadRestaurantDetails(selectedMarkerId)
                    }
                } else {
                    selectedMarkerId = restaurants[0].id
                    viewModel.loadRestaurantDetails(selectedMarkerId)
                }
            }

            restaurants.forEach { location ->
                val customMarker = getDrawableLocationType(location.subcategoryKey)
                val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(
                    getMarkerBitmapFromView(
                        customMarker,
                        location.name
                    )
                )

                val position = LatLng(location.latitude, location.longitude)

                val markerOptionsWithLabel = MarkerOptions()
                    .position(position)
                    .title(location.id)
                    .icon(bitmapDescriptor)
                val markerOptionsWithoutLabel = MarkerOptions()
                    .position(position)
                    .title(location.id)
                    .icon(BitmapDescriptorFactory.fromResource(getDrawableLocationType(location.subcategoryKey)))

                googleMap.addMarker(markerOptionsWithLabel)?.let { markersWithLabels.add(it) }
                googleMap.addMarker(markerOptionsWithoutLabel)
                    ?.let { markersWithoutLabels.add(it) }
            }

            markersWithLabels.forEach { it.isVisible = false }

            googleMap.setOnCameraIdleListener {
                val zoom = googleMap.cameraPosition.zoom

                if (zoom > 16.5f || restaurants.size < 10) {
                    markersWithLabels.forEach { it.isVisible = true }
                    markersWithoutLabels.forEach { it.isVisible = false }
                } else {
                    markersWithLabels.forEach { it.isVisible = false }
                    markersWithoutLabels.forEach { it.isVisible = true }
                }
            }

            val restaurantsCopy = restaurants.toMutableList()

            val adapter = CustomArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                restaurantsCopy
            )
            binding.acSearch.threshold = 2
            binding.acSearch.setAdapter(adapter)

            binding.acSearch.onItemClickListener =
                AdapterView.OnItemClickListener { parent, _, position, _ ->
                    // close the keyboard
                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view?.windowToken, 0)

                    binding.acSearch.clearFocus()

                    val restaurant = parent.getItemAtPosition(position) as RestaurantItemViewModel
                    selectedMarkerId = restaurant.id

                    // set the camera to the selected restaurant
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        LatLng(restaurant.latitude, restaurant.longitude),
                        19f
                    )

                    googleMap.animateCamera(cameraUpdate)

                    viewModel.loadRestaurantDetails(selectedMarkerId)
                }
        }
    }

    private fun getDrawableLocationType(subcategoryKey: String): Int {
        if (subcategoryKey == "fast_food")
            return R.drawable.ic_fast_food_marker
        else if (subcategoryKey == "cafe")
            return R.drawable.ic_cafe_marker
        return R.drawable.ic_sit_down_marker
    }

    private fun getMarkerBitmapFromView(
        drawableResId: Int,
        text: String
    ): Bitmap {
        val customMarkerView =
            (requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.custom_marker_layout,
                null
            )
        val markerImageView = customMarkerView.findViewById<ImageView>(R.id.marker_image)
        val markerTextView = customMarkerView.findViewById<TextView>(R.id.marker_text)

        markerImageView.setImageResource(drawableResId)
        markerTextView.text = text

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(
            0,
            0,
            customMarkerView.measuredWidth,
            customMarkerView.measuredHeight
        )

        // Create a bitmap from the view
        val bitmap = Bitmap.createBitmap(
            customMarkerView.measuredWidth,
            customMarkerView.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        customMarkerView.draw(canvas)

        return bitmap
    }


    private fun executeMapOperation(callback: PendingMapOperationCallback) {
        if (googleMap != null) {
            callback(googleMap!!)
        } else {
            pendingMapOperations.add(callback)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        pendingMapOperations.onEach {
            it(googleMap)
        }
        pendingMapOperations.clear()

        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.google_maps_custom_style
            )
        )

        if (stateUpdate) {
            displayDefaultLocation()

            if (hasPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                displayCustomLocation()
            } else {
                if (!viewModel.firstTimeLocationPermissions) {
                    handleLocationState()
                }
            }
        }


        googleMap.setOnMarkerClickListener { marker ->
            selectedMarkerId = marker.title!!

            viewModel.loadRestaurantDetails(selectedMarkerId)

            true
        }
    }

    private fun handleLocationState() {
        val checkPermissions = hasPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (checkPermissions) {
            if (viewModel.isLocationEnabled()) {
                viewModel.startDeviceLocationUpdates()

                displayCurrentLocation()
            } else {
                displayCustomLocation()
            }
        } else {
            viewModel.firstTimeLocationPermissions = true

            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
            ) {
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                })

                shouldUpdateMapLocation = true
            } else {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun displayCustomLocation() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(requireActivity())
        val enableLocation: Task<LocationSettingsResponse> =
            client.checkLocationSettings(builder.build())

        enableLocation.addOnSuccessListener {
            viewModel.startDeviceLocationUpdates()

            displayCurrentLocation()
        }

        enableLocation.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                locationSettingsCallback.launch(intentSenderRequest)
            }
        }
    }

    private fun displayDefaultLocation() {
        googleMap?.isMyLocationEnabled = false
        googleMap?.uiSettings?.isMyLocationButtonEnabled = false

        val defaultLocation = LatLng(47.17, 27.57)

        googleMap?.moveCamera(
            CameraUpdateFactory.newLatLngZoom(defaultLocation, 13f)
        )
    }

    private fun displayCurrentLocation() {
        lifecycleScope.launch {
            googleMap?.isMyLocationEnabled = true
            googleMap?.uiSettings?.isMyLocationButtonEnabled = false

            val location = viewModel.awaitDeviceLocation()

            val currentLocation = LatLng(location.latitude, location.longitude)
            googleMap?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(currentLocation, 13f)
            )
        }
    }

    private fun hasPermissions(vararg permissions: String): Boolean = permissions.any {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = Priority.PRIORITY_HIGH_ACCURACY
    }

    override fun onResume() {
        super.onResume()

        val fullServiceSelected = args.fullService
        val fastFoodSelected = args.fastFood
        val pubSelected = args.pub

        if (args.type == 0) {
            if (!fullServiceSelected && !fastFoodSelected && !pubSelected) {
                viewModel.loadRestaurants(true, true, true)
            } else {
                viewModel.loadRestaurants(fullServiceSelected, fastFoodSelected, pubSelected)
            }
        } else if (args.type == 1) {
            viewModel.loadRestaurantsByRecommendations()
        }

    }

    override fun onStop() {
        super.onStop()

        viewModel.stopDeviceLocationUpdates()
    }
}
