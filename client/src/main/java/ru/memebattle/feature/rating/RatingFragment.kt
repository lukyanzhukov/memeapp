package ru.memebattle.feature.rating

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_rating.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import ru.memebattle.R
import ru.memebattle.common.model.RatingModel
import ru.memebattle.core.BaseFragment
import ru.memebattle.core.api.AuthApi
import ru.memebattle.core.api.RatingApi
import ru.memebattle.core.utils.log

class RatingFragment : BaseFragment() {

    private val prefs: SharedPreferences = get()
    private val ratingApi: RatingApi = get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rating, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ratingAdapter = RatingAdapter()
        recycler_view.adapter = ratingAdapter

        launch {
            try {
                ratingAdapter.ratingModels = ratingApi.getRating()
            } catch (error: Throwable) {
                log(error.toString())
                error.printStackTrace()
            }
        }
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setSubtitleTextColor(Color.WHITE)
        colToolbar.setCollapsedTitleTextColor(Color.WHITE);
        colToolbar.setExpandedTitleColor(Color.WHITE);
        toolbar.title = "Рейтинг игроков"
    }

    override fun onDestroyView() {
        recycler_view.adapter = null
        super.onDestroyView()
    }
}