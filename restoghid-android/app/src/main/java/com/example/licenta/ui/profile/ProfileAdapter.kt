package com.example.licenta.ui.profile

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta.R
import com.example.licenta.models.Booking
import com.example.licenta.ui.restaurantdetails.KEY_GOOGLE_MAPS_URL

class ProfileAdapter(private val viewModel: ProfileViewModel) :
    RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {
    private var bookings: List<Booking> = ArrayList()

    fun setBookings(bookings: List<Booking>) {
        this.bookings = bookings

        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        bookings = bookings.toMutableList().also {
            it.removeAt(position)
        }
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, bookings.size)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileAdapter.ProfileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            com.example.licenta.R.layout.item_booking,
            parent,
            false
        )

        return ProfileViewHolder(view, viewModel, this)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bindBooking(bookings[position])
    }

    override fun getItemCount() = bookings.size

    class ProfileViewHolder(
        itemView: View,
        private val viewModel: ProfileViewModel,
        private val profileAdapter: ProfileAdapter
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bindBooking(booking: Booking) {
            val tvBookingRestaurantName: TextView =
                itemView.findViewById(R.id.tv_booking_restaurant_name)
            val tvBookingDate: TextView = itemView.findViewById(R.id.tv_booking_date)
            val tvPhoneNumber: TextView = itemView.findViewById(R.id.tv_booking_phone)
            val tvBookingPersons: TextView = itemView.findViewById(R.id.tv_booking_seats)
            val tvBookingInterval: TextView = itemView.findViewById(R.id.tv_booking_interval)
            val btnBookingCancel: TextView = itemView.findViewById(R.id.btn_booking_cancel)
            val ivDirections: ImageView = itemView.findViewById(R.id.iv_directions_booking)

            tvBookingRestaurantName.text = booking.restaurantName
            tvBookingDate.text = booking.date
            tvBookingPersons.text =
                (booking.bookedSeats == 1).let { if (it) "${booking.bookedSeats} persoană" else "${booking.bookedSeats} persoane" }
                    .toString()
            tvPhoneNumber.text = booking.phoneNumber

            ivDirections.setOnClickListener {
                val url =
                    "$KEY_GOOGLE_MAPS_URL${booking.latitude.toDouble()},${booking.longitude.toDouble()}"
                val intent = Intent(Intent.ACTION_VIEW)

                intent.data = Uri.parse(url)
                itemView.context.startActivity(intent)
            }

            when (booking.mealType) {
                0 -> {
                    tvBookingInterval.text =
                        itemView.context.resources.getStringArray(R.array.morning_intervals)[booking.selectedInterval]
                }
                1 -> {
                    tvBookingInterval.text =
                        itemView.context.resources.getStringArray(R.array.afternoon_intervals)[booking.selectedInterval]
                }
                else -> {
                    tvBookingInterval.text =
                        itemView.context.resources.getStringArray(R.array.evening_intervals)[booking.selectedInterval]
                }
            }

            btnBookingCancel.setOnClickListener {
                viewModel.deleteBookingById(booking.id)
                profileAdapter.removeAt(adapterPosition)
            }
        }
    }
}