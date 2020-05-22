package ru.memebattle.feature.onboarding.page

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_onboarding_page.*
import ru.memebattle.R

/**
 * Фрагмент одной страницы ViewPager'а онбодинга.
 */
class OnboardingPageFragment : Fragment(R.layout.fragment_onboarding_page) {

    private lateinit var page: OnboardingPage
    private lateinit var onComplete: () -> Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(EXTRA_PAGE_NAME)?.let { pageName ->
            page = OnboardingPage.valueOf(pageName)
            initPage(page)
        }
    }

    private fun initPage(page: OnboardingPage) {
        title.text = getString(page.titleRes)
        description.text = getString(page.descriptionRes)
        page_banner.setImageResource(page.backgroundRes)
        complete_button.isVisible = page.isLastPage
        complete_button.setOnClickListener { onComplete.invoke() }
    }

    companion object {
        private const val EXTRA_PAGE_NAME = "extra_page_name"
        /** Создаёт инстанс страницы ViewPager'а, соответствующий названию страницы [pageName]. */
        fun newInstance(pageName: String, onComplete: () -> Unit): OnboardingPageFragment {
            return OnboardingPageFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_PAGE_NAME, pageName)
                }
                this.onComplete = onComplete
            }
        }
    }
}
