package com.edutrack.presentation.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edutrack.domain.models.AuthData
import com.edutrack.domain.usecases.LoginUseCase
import com.edutrack.domain.usecases.GoogleLoginUseCase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val googleSignInClient: GoogleSignInClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult.asStateFlow()

    private val _googleSignInIntent = Channel<IntentEvent>(Channel.BUFFERED)
    val googleSignInIntent: Flow<IntentEvent> = _googleSignInIntent.receiveAsFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun login() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            _loginResult.value = null

            val result = loginUseCase(
                _uiState.value.email,
                _uiState.value.password
            )

            result.fold(
                onSuccess = { authData ->
                    _loginResult.value = LoginResult.Success(authData)
                },
                onFailure = { error ->
                    _loginResult.value = LoginResult.Error(error.message ?: "Login failed")
                }
            )

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }


    fun startGoogleSignIn() {
        viewModelScope.launch {
            val signInIntent = googleSignInClient.signInIntent
            _googleSignInIntent.send(IntentEvent(signInIntent))

        }
    }

    fun handleGoogleSignInResult(data: Intent?) {

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                if (data == null) {
                    _loginResult.value = LoginResult.Error("No data received from Google")
                    return@launch
                }

                val task = GoogleSignIn.getSignedInAccountFromIntent(data)

                val account = task.getResult(ApiException::class.java)

                val idToken = account.idToken

                if (idToken != null) {
                    loginWithGoogle(idToken)
                } else {
                    _loginResult.value = LoginResult.Error("Failed to get Google ID token")
                }
            } catch (e: ApiException) {
                _loginResult.value = when (e.statusCode) {
                    GoogleSignInStatusCodes.SIGN_IN_CANCELLED ->
                        LoginResult.Error("Sign-in cancelled by user")
                    else -> LoginResult.Error("Google Sign-In error: ${e.message}")
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Error("Error: ${e.message}")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun loginWithGoogle(googleToken: String) {
        viewModelScope.launch {
            _loginResult.value = null

            try {
                val result = googleLoginUseCase.invoke(googleToken)

                _loginResult.value = result.fold(
                    onSuccess = {
                        LoginResult.Success(it)
                    },
                    onFailure = {
                        LoginResult.Error(it.message ?: "Login failed")
                    }
                )
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Error("Network error: ${e.message}")
            }
        }
    }

    fun clearResult() {
        _loginResult.value = null
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false
)

sealed class LoginResult {
    data class Success(val authData: AuthData) : LoginResult()
    data class Error(val message: String) : LoginResult()
}

data class IntentEvent(val intent: android.content.Intent)