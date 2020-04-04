package ru.memebattle.feature.rating

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.memebattle.R

class RatingAdapter : RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {

    var ratings: List<Rating> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val ratingItemView = LayoutInflater.from(parent.context).inflate(R.layout.item_rating, parent, false)
        return RatingViewHolder(ratingItemView)
    }

    override fun getItemCount(): Int = ratings.size

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        holder.bind(ratings[position], position + 1)
    }

    class RatingViewHolder(ratingView: View) : RecyclerView.ViewHolder(ratingView) {

        private val playerNameTextView = ratingView.findViewById<TextView>(R.id.playerName)
        private val playerScoreTextView = ratingView.findViewById<TextView>(R.id.playerScore)
        private val placeTextView = ratingView.findViewById<TextView>(R.id.place)

        fun bind(rating: Rating, position: Int) {
            playerNameTextView.text = rating.playerName
            playerScoreTextView.text = rating.score.toString()
            placeTextView.text = "#${position}"
        }
    }
}