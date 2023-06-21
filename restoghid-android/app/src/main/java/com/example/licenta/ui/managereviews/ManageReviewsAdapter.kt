package com.example.licenta.ui.managereviews

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta.R
import com.example.licenta.models.Review

class ManageReviewsAdapter(private val viewModel: ManageReviewsViewModel) :
    RecyclerView.Adapter<ManageReviewsAdapter.ManageReviewsViewHolder>() {
    private var reviews: List<Review> = ArrayList()

    fun setReviews(reviews: List<Review>) {
        this.reviews = reviews

        Log.d("_reviews", "here")

        // refresh recycler view for new data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageReviewsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ManageReviewsViewHolder(view, viewModel)
    }

    override fun onBindViewHolder(holder: ManageReviewsViewHolder, position: Int) {
        holder.bindReview(reviews[position])
    }

    override fun getItemCount() = reviews.size

    class ManageReviewsViewHolder(itemView: View, private val viewModel: ManageReviewsViewModel) :
        RecyclerView.ViewHolder(itemView) {
        fun bindReview(review: Review) {
            val tvReviewUsername: TextView = itemView.findViewById(R.id.tv_review_username)
            val tvReviewContent: TextView = itemView.findViewById(R.id.tv_review_content)
            val tvReviewScore: TextView = itemView.findViewById(R.id.tv_review_score)
            val tvReviewDate: TextView = itemView.findViewById(R.id.tv_review_date)
            val cvReply: CardView = itemView.findViewById(R.id.cv_reply)
            val tvReply: TextView = itemView.findViewById(R.id.tv_reply)
            val etReply: EditText = itemView.findViewById(R.id.et_review_reply)
            val btnReply: Button = itemView.findViewById(R.id.btn_review_reply)

            if (review.reply != null) {
                cvReply.visibility = View.VISIBLE
                tvReply.text = review.reply

                etReply.visibility = View.GONE
                btnReply.visibility = View.GONE
            } else {
                cvReply.visibility = View.GONE

                etReply.visibility = View.VISIBLE
                btnReply.visibility = View.VISIBLE

                btnReply.setOnClickListener {
                    tvReply.text = etReply.text.toString()

                    etReply.visibility = View.GONE
                    btnReply.visibility = View.GONE

                    viewModel.updateReview(review.id!!, etReply.text.toString())
                }
            }

            tvReviewUsername.text = review.username
            tvReviewContent.text = review.content
            tvReviewScore.text = review.score.toString()
            tvReviewDate.text = review.date
        }
    }
}