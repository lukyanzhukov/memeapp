package ru.memebattle.feature.onboarding

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_onboarding.*
import ru.memebattle.R
import ru.memebattle.feature.onboarding.page.OnboardingPage

/**
 * Экран онбординга.
 */
class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private var adapter: OnboardingAdapter? = null

    private val viewPagerChangeCallBack: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                page_indicator.selection = position
            }
        }

    private var onComplete: (() -> Unit)? = {
        findNavController().navigate(R.id.action_onboardingFragment_to_mainFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.statusBarColor = Color.WHITE
        close_button.setOnClickListener {
            onComplete?.invoke()
        }
        adapter = onComplete?.let { OnboardingAdapter(childFragmentManager, lifecycle, it) }
        page_indicator.count = OnboardingPage.values().size
        pager.adapter = adapter
        pager.registerOnPageChangeCallback(viewPagerChangeCallBack)
    }

    override fun onStop() {
        pager.unregisterOnPageChangeCallback(viewPagerChangeCallBack)
        onComplete = null
        super.onStop()
    }
}
