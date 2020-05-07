package ru.memebattle.feature.rating

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.memebattle.R
import ru.memebattle.common.model.RatingModel

class RatingAdapter(
    private val currentPlayerName: String?
) : RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {

    var ratingModels: List<RatingModel> = emptyList()
        set(value) {
            field = value.sortedByDescending { it.score }
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        val ratingItemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rating, parent, false)
        return RatingViewHolder(ratingItemView)
    }

    override fun getItemCount(): Int = ratingModels.size

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        holder.bind(ratingModels[position], position + 1)
    }

    inner class RatingViewHolder(private val ratingView: View) :
        RecyclerView.ViewHolder(ratingView) {

        private val layoutView = ratingView.findViewById<ConstraintLayout>(R.id.item_view)
        private val playerNameTextView = ratingView.findViewById<TextView>(R.id.name)
        private val playerScoreTextView = ratingView.findViewById<TextView>(R.id.points)

        fun bind(ratingModel: RatingModel, position: Int) {
            playerNameTextView.text = "${position}.  ${ratingModel.playerName}".substringBefore('@')
            playerScoreTextView.text = ratingModel.score.toString()
            if (currentPlayerName == ratingModel.playerName) {
                layoutView.background =
                    ratingView.resources.getDrawable(R.drawable.bg_rating_current_user)
                playerNameTextView.setTextColor(Color.WHITE)
                return
            }
            layoutView.background =
                ratingView.resources.getDrawable(R.drawable.bg_rating_user)
            playerNameTextView.setTextColor(Color.BLACK)
        }
    }
}