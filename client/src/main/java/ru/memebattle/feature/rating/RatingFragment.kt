package ru.memebattle.feature.rating

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import io.ktor.client.HttpClient
import kotlinx.android.synthetic.main.fragment_rating.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import ru.memebattle.PREFS_EMAIL
import ru.memebattle.R
import ru.memebattle.core.utils.log

class RatingFragment : Fragment() {

    private val prefs: SharedPreferences = get()
    private val httpClient: HttpClient = get()

    override fun onResume() {
        super.onResume()
        shimmer_view_container.startShimmerAnimation();
    }

    override fun onPause() {
        shimmer_view_container.stopShimmerAnimation();
        super.onPause()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rating, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ratingAdapter = RatingAdapter(prefs.getString(PREFS_EMAIL, "")!!)
        recycler_view.adapter = ratingAdapter

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            try {
                //ratingAdapter.ratingModels = httpClient.getRating()
                shimmer_view_container.stopShimmerAnimation()
                shimmer_view_container.isVisible = false
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