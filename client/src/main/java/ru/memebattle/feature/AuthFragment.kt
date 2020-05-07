package ru.memebattle.feature

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import client.common.feature.auth.AuthResult
import client.common.feature.auth.AuthViewModel
import client.common.feature.localization.LocalizationViewModel
import kotlinx.android.synthetic.main.fragment_auth.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.common.feature.auth.AuthValidator
import ru.memebattle.common.feature.localization.Localization
import ru.memebattle.core.utils.toast

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val authViewModel: AuthViewModel by viewModel()
    private val localizationViewModel: LocalizationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().window.statusBarColor = resources.getColor(R.color.authStatusBarColor)
        initView()
    }

    private fun initView() {
        localizationViewModel.locale.platform.observe(viewLifecycleOwner) { locale ->
            signUpButton.text = locale[Localization.AUTH_SIGN_UP_BUTTON_TEXT]
            signInButton.text = locale[Localization.AUTH_SIGN_IN_BUTTON_TEXT]
            loginTextInputLayout.hint = locale[Localization.AUTH_LOGIN_HINT_TEXT]
            passwordTextInputLayout.hint = locale[Localization.AUTH_PASSWORD_HINT_TEXT]

            signUpButton.setOnClickListener {
                loginTextInputLayout.isErrorEnabled = false
                passwordTextInputLayout.isErrorEnabled = false

                if (!isFieldsValid(
                        loginNotValidError = locale.getValue(Localization.AUTH_LOGIN_NOT_VALID_FIELD_ERROR),
                        passwordNotValidError = locale.getValue(Localization.AUTH_PASSWORD_NOT_VALID_FIELD_ERROR)
                    )) return@setOnClickListener

                authViewModel.register(
                    loginInput.text.toString(),
                    passwordInput.text.toString()
                )
            }
        }

        authViewModel.loading.platform.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                showProgress()
            } else {
                hideProgress()
            }
        }

        authViewModel.authResult.platform.observe(viewLifecycleOwner) { authResult ->
            when (authResult) {
                AuthResult.Success -> findNavController()
                    .navigate(R.id.action_authFragment_to_mainFragment)
                is AuthResult.Fail.InvalidPassword -> loginTextInputLayout.error =
                    authResult.message
                is AuthResult.Fail.InvalidLogin -> passwordTextInputLayout.error =
                    authResult.message
                is AuthResult.Fail.UserNotFound -> toast(authResult.message)
                is AuthResult.Fail.UserAlreadyRegistered -> toast(authResult.message)
                is AuthResult.Fail.NetworkError -> toast(authResult.message)
                is AuthResult.Fail.EmptyPassword -> passwordTextInputLayout.error =
                    authResult.message
            }
        }

        signInButton.setOnClickListener {
            loginTextInputLayout.isErrorEnabled = false
            passwordTextInputLayout.isErrorEnabled = false

            authViewModel.auth(
                loginInput.text.toString(),
                passwordInput.text.toString()
            )
        }
    }

    private fun isFieldsValid(loginNotValidError: String, passwordNotValidError: String): Boolean {
        val isLoginValid = if (AuthValidator.isLoginValid(loginInput.text.toString())) {
            true
        } else {
            loginTextInputLayout.error = loginNotValidError
            false
        }
        val isPasswordValid = if (AuthValidator.isPasswordValid(passwordInput.text.toString())) {
            true
        } else {
            passwordTextInputLayout.error = passwordNotValidError
            false
        }
        return isLoginValid && isPasswordValid
    }

    private fun showProgress() {
        progressBar.isVisible = true
        auth_view.isVisible = false
    }

    private fun hideProgress() {
        progressBar.isVisible = false
        auth_view.isVisible = true
    }
}