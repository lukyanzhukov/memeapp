package ru.memebattle.feature

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.ext.android.get
import ru.memebattle.PREFS_TOKEN
import ru.memebattle.R
import ru.memebattle.core.utils.putString

class SettingsFragment : Fragment() {

    companion object {
        private const val SET_ALARM_CODE = 100
    }

    private val sharedPreferences: SharedPreferences = get()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signOut.setOnClickListener {
            sharedPreferences.putString(PREFS_TOKEN, null)
            Navigation.findNavController(requireActivity(), R.id.host_global)
                .navigate(R.id.action_settingsFragment_to_authFragment)
        }
        memebattle_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://vk.com/memsbattle")
            intent.resolveActivity(checkNotNull(context).packageManager)?.let {
                checkNotNull(context).startActivity(intent)
            }
        }
    }
}