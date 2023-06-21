package com.example.licenta.ui.booking

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.licenta.R
import com.example.licenta.databinding.FragmentBookingBinding
import com.example.licenta.models.Booking
import com.example.licenta.ui.restaurants.stateUpdate
import java.time.LocalDate

class BookingFragment : Fragment() {
    private lateinit var binding: FragmentBookingBinding
    private val viewModel: BookingViewModel by activityViewModels {
        BookingViewModelFactory(requireContext())
    }

    private var selectedSlotMorning: String = ""
    private var selectedSlotAfternoon: String = ""
    private var selectedSlotEvening: String = ""

    private var selectedNumberOfSeatsMorning: Int = 0
    private var selectedNumberOfSeatsAfternoon: Int = 0
    private var selectedNumberOfSeatsEvening: Int = 0

    private val args: BookingFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_booking, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stateUpdate = false

        val restaurantId = args.restaurantId

        viewModel.loadRestaurantDetails(restaurantId)

        setTheIntervalSpinner()

        viewModel.getErrorLiveData().observe(viewLifecycleOwner)
        { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.getRestaurantDetails().observe(viewLifecycleOwner)
        { restaurant ->
            if (restaurant != null) {
                val bookings = restaurant.bookings

                initIntervalAvailability(LocalDate.now(), bookings, restaurant.totalSlots)

                binding.datePicker.init(
                    binding.datePicker.year,
                    binding.datePicker.month,
                    binding.datePicker.dayOfMonth
                ) { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)

                    initIntervalAvailability(selectedDate, bookings, restaurant.totalSlots)

                    binding.availabilityMorning.spinnerTimeSlots.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                selectedSlotMorning =
                                    binding.availabilityMorning.spinnerTimeSlots.selectedItemPosition.toString()
                                setIntervalAvailability(
                                    selectedDate,
                                    bookings,
                                    restaurant.totalSlots,
                                    0,
                                    selectedSlotMorning
                                )
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                selectedSlotMorning = ""
                            }
                        }

                    binding.availabilityAfternoon.spinnerTimeSlots.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                selectedSlotAfternoon =
                                    binding.availabilityAfternoon.spinnerTimeSlots.selectedItemPosition.toString()
                                setIntervalAvailability(
                                    selectedDate,
                                    bookings,
                                    restaurant.totalSlots,
                                    1,
                                    selectedSlotAfternoon
                                )
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                selectedSlotAfternoon = ""
                            }
                        }

                    binding.availabilityEvening.spinnerTimeSlots.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                selectedSlotEvening =
                                    binding.availabilityEvening.spinnerTimeSlots.selectedItemPosition.toString()
                                setIntervalAvailability(
                                    selectedDate,
                                    bookings,
                                    restaurant.totalSlots,
                                    2,
                                    selectedSlotEvening
                                )
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                selectedSlotEvening = ""
                            }
                        }

                    binding.availabilityMorning.btnBook.setOnClickListener {
                        selectedSlotMorning =
                            binding.availabilityMorning.spinnerTimeSlots.selectedItemPosition.toString()
                        selectedNumberOfSeatsMorning =
                            binding.availabilityMorning.etSeats.text.toString().toIntOrNull() ?: 1

                        val userPhone = binding.etUserPhone.text.toString()

                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Confirmare rezervare")
                        builder.setMessage("Ești sigur că vrei să confirmi o masă de $selectedNumberOfSeatsMorning persoane la ${binding.availabilityMorning.spinnerTimeSlots.selectedItem}?")
                        builder.setPositiveButton("Da") { _, _ ->
                            viewModel.createBooking(
                                restaurantId,
                                selectedDate,
                                0,
                                restaurant.totalSlots,
                                selectedSlotMorning.toInt(),
                                selectedNumberOfSeatsMorning,
                                userPhone
                            )
                        }
                        builder.setNegativeButton("Nu") { dialog, _ ->
                            dialog.dismiss()
                        }

                        val alertDialog: AlertDialog = builder.create()
                        alertDialog.show()
                    }

                    binding.availabilityAfternoon.btnBook.setOnClickListener {
                        selectedSlotAfternoon =
                            binding.availabilityAfternoon.spinnerTimeSlots.selectedItemPosition.toString()
                        selectedNumberOfSeatsAfternoon =
                            binding.availabilityAfternoon.etSeats.text.toString().toIntOrNull() ?: 1
                        val userPhone = binding.etUserPhone.text.toString()

                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Confirmare rezervare")
                        builder.setMessage("Ești sigur că vrei să confirmi o masă de $selectedNumberOfSeatsAfternoon persoane la ${binding.availabilityAfternoon.spinnerTimeSlots.selectedItem}?")
                        builder.setPositiveButton("Da") { _, _ ->
                            viewModel.createBooking(
                                restaurantId,
                                selectedDate,
                                1,
                                restaurant.totalSlots,
                                selectedSlotAfternoon.toInt(),
                                selectedNumberOfSeatsAfternoon,
                                userPhone
                            )
                        }
                        builder.setNegativeButton("Nu") { dialog, _ ->
                            dialog.dismiss()
                        }

                        val alertDialog: AlertDialog = builder.create()
                        alertDialog.show()
                    }

                    binding.availabilityEvening.btnBook.setOnClickListener {
                        selectedSlotEvening =
                            binding.availabilityEvening.spinnerTimeSlots.selectedItemPosition.toString()
                        selectedNumberOfSeatsEvening =
                            binding.availabilityEvening.etSeats.text.toString().toIntOrNull() ?: 1
                        val userPhone = binding.etUserPhone.text.toString()

                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Confirmare rezervare")
                        builder.setMessage("Ești sigur că vrei să confirmi o masă de $selectedNumberOfSeatsEvening persoane la ${binding.availabilityEvening.spinnerTimeSlots.selectedItem}?")
                        builder.setPositiveButton("Da") { _, _ ->
                            viewModel.createBooking(
                                restaurantId,
                                selectedDate,
                                2,
                                restaurant.totalSlots,
                                selectedSlotEvening.toInt(),
                                selectedNumberOfSeatsEvening,
                                userPhone
                            )
                        }
                        builder.setNegativeButton("Nu") { dialog, _ ->
                            dialog.dismiss()
                        }

                        val alertDialog: AlertDialog = builder.create()
                        alertDialog.show()
                    }
                }
            }
        }

        viewModel.getBookingDetails().observe(viewLifecycleOwner)
        { booking ->
            if (booking != null) {
                Log.d("_BookingFragment", "Booking details")

                viewModel.refreshBookingData()

                val action =
                    BookingFragmentDirections.actionBookingFragmentToProfileFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun setIntervalAvailability(
        selectedDate: LocalDate,
        bookings: MutableList<Booking>,
        totalSlots: Int,
        mealType: Int,
        selectedSlot: String
    ) {
        val bookingsReserved = bookings.filter {
            it.date == selectedDate.toString() && it.mealType == mealType && it.selectedInterval == selectedSlot.toInt()
        }.sumOf { it.bookedSeats }

        when (mealType) {
            0 -> binding.availabilityMorning.availableSlots = totalSlots - bookingsReserved
            1 -> binding.availabilityAfternoon.availableSlots = totalSlots - bookingsReserved
            2 -> binding.availabilityEvening.availableSlots = totalSlots - bookingsReserved
        }
    }

    private fun initIntervalAvailability(
        date: LocalDate,
        bookings: List<Booking>,
        totalSlots: Int
    ) {
        binding.availabilityMorning.spinnerTimeSlots.setSelection(0)
        binding.availabilityAfternoon.spinnerTimeSlots.setSelection(0)
        binding.availabilityEvening.spinnerTimeSlots.setSelection(0)

        selectedSlotMorning = "0"
        selectedSlotAfternoon = "0"
        selectedSlotEvening = "0"

        val bookingsMorning = bookings.filter {
            it.date == date.toString() && it.mealType == 0 && it.selectedInterval == selectedSlotMorning.toInt()
        }.sumOf { it.bookedSeats }

        val bookingsAfternoon = bookings.filter {
            it.date == date.toString() && it.mealType == 1 && it.selectedInterval == selectedSlotAfternoon.toInt()
        }.sumOf { it.bookedSeats }

        val bookingsEvening = bookings.filter {
            it.date == date.toString() && it.mealType == 2 && it.selectedInterval == selectedSlotEvening.toInt()
        }.sumOf { it.bookedSeats }

        binding.availabilityMorning.availableSlots =
            totalSlots - bookingsMorning
        binding.availabilityAfternoon.availableSlots =
            totalSlots - bookingsAfternoon
        binding.availabilityEvening.availableSlots =
            totalSlots - bookingsEvening
    }

    private fun setTheIntervalSpinner() {
        val morningIntervals = resources.getStringArray(R.array.morning_intervals)
        val spinnerAdapterMorning =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, morningIntervals)
        binding.availabilityMorning.spinnerTimeSlots.adapter = spinnerAdapterMorning

        val afternoonIntervals = resources.getStringArray(R.array.afternoon_intervals)
        val spinnerAdapterAfternoon =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, afternoonIntervals)
        binding.availabilityAfternoon.spinnerTimeSlots.adapter = spinnerAdapterAfternoon


        val eveningIntervals = resources.getStringArray(R.array.evening_intervals)
        val spinnerAdapterEvening =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, eveningIntervals)
        binding.availabilityEvening.spinnerTimeSlots.adapter = spinnerAdapterEvening
    }

    override fun onResume() {
        super.onResume()

        Log.d("_BookingFragment", "onResume")

        viewModel.loadRestaurantDetails(args.restaurantId)
    }
}