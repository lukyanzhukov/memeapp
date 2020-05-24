package ru.memebattle.feature.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.memebattle.R
import ru.memebattle.common.dto.game.GameModeModel

class GameModeAdapter(private val onGameModeClick: (String) -> Unit) :
    RecyclerView.Adapter<GameModeAdapter.GameModeViewHolder>() {

    var gameModeModels: List<GameModeModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameModeViewHolder {
        val gameModeItemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_game_mode, parent, false)
        return GameModeViewHolder(gameModeItemView)
    }

    override fun getItemCount(): Int = gameModeModels.size

    override fun onBindViewHolder(holderMode: GameModeViewHolder, position: Int) {
        holderMode.bind(gameModeModels[position])
    }

    inner class GameModeViewHolder(private val ratingView: View) :
        RecyclerView.ViewHolder(ratingView) {

        private val layoutView = ratingView.findViewById<LinearLayout>(R.id.item_view)
        private val title = ratingView.findViewById<TextView>(R.id.item_title)
        private val banner = ratingView.findViewById<ImageView>(R.id.item_banner)

        fun bind(gameModeModel: GameModeModel) {
            layoutView.setOnClickListener { onGameModeClick(gameModeModel.name) }
            title.text = gameModeModel.name

            Glide.with(banner.context)
                .load(gameModeModel.imageUrl)
                .placeholder(getDrawable(banner.context, R.drawable.wait_image))
                .into(banner)
        }
    }
}