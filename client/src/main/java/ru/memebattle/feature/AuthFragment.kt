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
import ru.memebattle.common.feature.auth.AuthValidator
import ru.memebattle.core.utils.toast

class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val authViewModel: AuthViewModel by currentScope.viewModel(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    private fun initView() {
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
                AuthResult.Fail.InvalidPassword -> {

                    loginTextInputLayout.error = getString(R.string.auth_wrong_login_error_message)
                }
                AuthResult.Fail.InvalidLogin -> {
                    passwordTextInputLayout.error =
                        getString(R.string.auth_wrong_password_error_message)
                }
                AuthResult.Fail.UserNotFound ->
                    toast(getString(R.string.auth_not_user_error_message))
                AuthResult.Fail.UserAlreadyRegistered ->
                    toast(getString(R.string.auth_user_exist_error_message))
                AuthResult.Fail.NetworkError ->
                    toast(getString(R.string.auth_netword_error_message))
                AuthResult.Fail.EmptyPassword -> passwordTextInputLayout.error =
                    getString(R.string.auth_password_not_valid_field_error_message)
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

        signUpButton.setOnClickListener {
            loginTextInputLayout.isErrorEnabled = false
            passwordTextInputLayout.isErrorEnabled = false

            if (!isFieldsValid()) return@setOnClickListener

            authViewModel.register(
                loginInput.text.toString(),
                passwordInput.text.toString()
            )
        }
    }

    private fun isFieldsValid(): Boolean {
        val isLoginValid = if (AuthValidator.isLoginValid(loginInput.text.toString())) {
            true
        } else {
            loginTextInputLayout.error =
                getString(R.string.auth_login_not_valid_field_error_message)
            false
        }
        val isPasswordValid = if (AuthValidator.isPasswordValid(passwordInput.text.toString())) {
            true
        } else {
            passwordTextInputLayout.error =
                getString(R.string.auth_password_not_valid_field_error_message)
            false
        }
        return isLoginValid && isPasswordValid
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