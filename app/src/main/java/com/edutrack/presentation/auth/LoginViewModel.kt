package com.edutrack.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edutrack.domain.models.AuthData
import com.edutrack.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult.asStateFlow()

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