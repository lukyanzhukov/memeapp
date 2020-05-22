package ru.memebattle.feature.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.memebattle.feature.onboarding.page.OnboardingPage
import ru.memebattle.feature.onboarding.page.OnboardingPageFragment

/** Адаптер для страниц ViewPager'а онбординга. */
class OnboardingAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val onComplete: () -> Unit
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = OnboardingPage.values().size

    override fun createFragment(position: Int): Fragment {
        val pageName = OnboardingPage.values()[position].name
        return OnboardingPageFragment.newInstance(pageName, onComplete)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.scrollToPosition(0)
    }
}
