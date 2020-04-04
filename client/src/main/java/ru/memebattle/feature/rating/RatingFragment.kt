package ru.memebattle.feature.rating

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_rating.*
import ru.memebattle.R
import ru.memebattle.common.model.RatingModel

class RatingFragment : Fragment(R.layout.fragment_rating) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ratingAdapter = RatingAdapter()
        recycler_view.adapter = ratingAdapter

        ratingAdapter.ratingModels = listOf(
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1),
            RatingModel("Den", 100, 1)
        )
        toolbar.title = "Рейтинг игроков"
    }

    override fun onDestroyView() {
        recycler_view.adapter = null
        super.onDestroyView()
    }
}