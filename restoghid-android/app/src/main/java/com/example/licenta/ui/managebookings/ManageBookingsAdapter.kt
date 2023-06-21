package com.example.licenta.ui.managebookings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta.R
import com.example.licenta.models.Booking

class ManageBookingsAdapter(private val viewModel: ManageBookingsViewModel) :
    RecyclerView.Adapter<ManageBookingsAdapter.ManageBookingsViewHolder>() {
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
    ): ManageBookingsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_booking,
            parent,
            false
        )

        return ManageBookingsViewHolder(view, viewModel, this)
    }

    override fun onBindViewHolder(holder: ManageBookingsViewHolder, position: Int) {
        holder.bindBooking(bookings[position])
    }

    override fun getItemCount() = bookings.size

    class ManageBookingsViewHolder(
        itemView: View,
        private val viewModel: ManageBookingsViewModel,
        private val manageBookingsAdapter: ManageBookingsAdapter
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bindBooking(booking: Booking) {
            val tvBookingUsername: TextView =
                itemView.findViewById(R.id.tv_booking_restaurant_name)
            val tvBookingDate: TextView = itemView.findViewById(R.id.tv_booking_date)
            val tvPhoneNumber: TextView = itemView.findViewById(R.id.tv_booking_phone)
            val tvBookingPersons: TextView = itemView.findViewById(R.id.tv_booking_seats)
            val tvBookingInterval: TextView = itemView.findViewById(R.id.tv_booking_interval)
            val btnBookingCancel: TextView = itemView.findViewById(R.id.btn_booking_cancel)
            val ivDirectionsBooking: ImageView =
                itemView.findViewById(R.id.iv_directions_booking)

            ivDirectionsBooking.visibility = View.GONE

            tvBookingUsername.text = booking.username
            tvBookingDate.text = booking.date
            tvBookingPersons.text =
                (booking.bookedSeats == 1).let { if (it) "${booking.bookedSeats} persoană" else "${booking.bookedSeats} persoane" }
                    .toString()
            tvPhoneNumber.text = booking.userPhone

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
                manageBookingsAdapter.removeAt(adapterPosition)
            }
        }
    }
}