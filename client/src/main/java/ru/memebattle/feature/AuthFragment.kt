package ru.memebattle.feature

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import client.common.feature.auth.AuthResult
import client.common.feature.auth.AuthViewModel
import kotlinx.android.synthetic.main.fragment_auth.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.core.utils.toast

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val authViewModel: AuthViewModel by currentScope.viewModel(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.loading.platform.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                showProgress()
            } else {
                hideProgress()
            }
        }

        authViewModel.authResult.platform.observe(viewLifecycleOwner) { authResult ->
            when (authResult) {
                AuthResult.Success -> findNavController().navigate(R.id.action_authFragment_to_mainFragment)
                AuthResult.Fail.InvalidPassword -> emailTextInputLayout.error = "Заполните поле"
                AuthResult.Fail.InvalidLogin -> passwordTextInputLayout.error = "Неправильный пароль"
                AuthResult.Fail.UserNotFound -> toast("Вы не зарегистрированы")
                AuthResult.Fail.UserAlreadyRegistered -> toast("Вы уже зарегистрированы")
                AuthResult.Fail.NetworkError -> toast("Проблемы с сетью")
                AuthResult.Fail.EmptyPassword -> passwordTextInputLayout.error = "Заполните поле"
            }
        }

        signInButton.setOnClickListener {
            emailTextInputLayout.isErrorEnabled = false
            passwordTextInputLayout.isErrorEnabled = false

            authViewModel.auth(
                emailInput.text?.toString().orEmpty(),
                passwordInput.text?.toString().orEmpty()
            )
        }

        signUpButton.setOnClickListener {
            emailTextInputLayout.isErrorEnabled = false
            passwordTextInputLayout.isErrorEnabled = false

            authViewModel.register(
                emailInput.text?.toString().orEmpty(),
                passwordInput.text?.toString().orEmpty()
            )
        }
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
        signUpButton.isEnabled = false
        signInButton.isEnabled = false
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
        signUpButton.isEnabled = true
        signInButton.isEnabled = true
    }
}