package com.example.licenta.ui.reviews

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta.R
import com.example.licenta.models.Review

class ReviewsAdapter() :
    RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>() {
    private var reviews: List<Review> = ArrayList()

    fun setReviews(reviews: List<Review>) {
        this.reviews = reviews

        Log.d("_reviews", "here")

        // refresh recycler view for new data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        holder.bindReview(reviews[position])
    }

    override fun getItemCount() = reviews.size

    class ReviewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindReview(review: Review) {
            val tvReviewUsername: TextView = itemView.findViewById(R.id.tv_review_username)
            val tvReviewContent: TextView = itemView.findViewById(R.id.tv_review_content)
            val tvReviewScore: TextView = itemView.findViewById(R.id.tv_review_score)
            val tvReviewDate: TextView = itemView.findViewById(R.id.tv_review_date)
            val cvReply: CardView = itemView.findViewById(R.id.cv_reply)
            val tvReply: TextView = itemView.findViewById(R.id.tv_reply)

            if (review.reply != null) {
                cvReply.visibility = View.VISIBLE
                tvReply.text = review.reply
            } else {
                cvReply.visibility = View.GONE
            }

            tvReviewUsername.text = review.username
            tvReviewContent.text = review.content
            tvReviewScore.text = review.score.toString()
            tvReviewDate.text = review.date
        }
    }
}