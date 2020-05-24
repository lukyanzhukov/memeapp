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
    var gameModesModels: List<GameModeItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameModesViewHolder {
        val ratingItemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_rating_mode, parent, false)
        return GameModesViewHolder(ratingItemView)
    }

    override fun getItemCount(): Int = gameModesModels.size

    override fun onBindViewHolder(holder: GameModesViewHolder, position: Int) {
        holder.bind(gameModesModels[position], position)
    }

    inner class GameModesViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        private val modeButton = view.findViewById<TextView>(R.id.mode_btn)

        fun bind(gameModeItem: GameModeItem, position: Int) {
            if (selectedPosition == position) {
                modeButton.setBackgroundResource(gameModeItem.drawable)
            } else {
                modeButton.setBackgroundResource(R.drawable.bg_item_rating_mode)
            }
            modeButton.text = gameModeItem.name
            modeButton.setOnClickListener {
                if (selectedPosition == position) return@setOnClickListener
                onItemSelected?.invoke(gameModeItem.name)
                modeButton.setBackgroundResource(gameModeItem.drawable)
                oldSelectedPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(oldSelectedPosition)
            }
        }
    }
}