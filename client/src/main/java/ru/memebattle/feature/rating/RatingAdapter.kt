package ru.memebattle.feature.rating

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.memebattle.R
import ru.memebattle.common.model.RatingModel

class RatingAdapter : RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {

    var ratingModels: List<RatingModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val ratingItemView = LayoutInflater.from(parent.context).inflate(R.layout.item_rating, parent, false)
        return RatingViewHolder(ratingItemView)
    }

    override fun getItemCount(): Int = ratingModels.size

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        holder.bind(ratingModels[position], position + 1)
    }

    class RatingViewHolder(ratingView: View) : RecyclerView.ViewHolder(ratingView) {

        private val playerNameTextView = ratingView.findViewById<TextView>(R.id.playerName)
        private val playerScoreTextView = ratingView.findViewById<TextView>(R.id.playerScore)
        private val placeTextView = ratingView.findViewById<TextView>(R.id.place)

        fun bind(ratingModel: RatingModel, position: Int) {
            playerNameTextView.text = ratingModel.playerName
            playerScoreTextView.text = ratingModel.score.toString()
            placeTextView.text = "#${position}"
        }
    }
}