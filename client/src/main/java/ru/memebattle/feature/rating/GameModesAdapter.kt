package ru.memebattle.feature.rating

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.memebattle.R

class GameModesAdapter : RecyclerView.Adapter<GameModesAdapter.GameModesViewHolder>() {

    var onItemSelected: ((name: String) -> Unit)? = null
    var selectedPosition: Int = 0
    var oldSelectedPosition: Int = 0
    var ratingGameModesModels: List<RatingGameModeItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameModesViewHolder {
        val ratingItemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rating_mode, parent, false)
        return GameModesViewHolder(ratingItemView)
    }

    override fun getItemCount(): Int = ratingGameModesModels.size

    override fun onBindViewHolder(holder: GameModesViewHolder, position: Int) {
        holder.bind(ratingGameModesModels[position], position)
    }

    inner class GameModesViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        private val modeButton = view.findViewById<TextView>(R.id.mode_btn)

        fun bind(ratingGameModeItem: RatingGameModeItem, position: Int) {
            if (selectedPosition == position) {
                modeButton.setBackgroundResource(ratingGameModeItem.drawable)
            } else {
                modeButton.setBackgroundResource(R.drawable.bg_item_rating_mode)
            }
            modeButton.text = ratingGameModeItem.name
            modeButton.setOnClickListener {
                if (selectedPosition == position) return@setOnClickListener
                onItemSelected?.invoke(ratingGameModeItem.name)
                modeButton.setBackgroundResource(ratingGameModeItem.drawable)
                oldSelectedPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(oldSelectedPosition)
            }
        }
    }
}