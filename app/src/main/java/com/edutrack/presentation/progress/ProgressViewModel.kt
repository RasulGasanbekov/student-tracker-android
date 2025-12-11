package com.edutrack.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edutrack.domain.models.Subject
import com.edutrack.domain.models.User
import com.edutrack.domain.usecases.GetProgressUseCase
import com.edutrack.domain.usecases.GetUserUseCase
import com.edutrack.domain.usecases.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getProgressUseCase: GetProgressUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    fun loadData(token: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val userResult = getUserUseCase(token)
            val progressResult = getProgressUseCase(token)

            userResult.fold(
                onSuccess = { user ->
                    progressResult.fold(
                        onSuccess = { subjects ->
                            _uiState.value = ProgressUiState(
                                user = user,
                                subjects = subjects,
                                isLoading = false
                            )
                        },
                        onFailure = { error ->
                            _uiState.value = _uiState.value.copy(
                                user = user,
                                isLoading = false,
                                error = error.message
                            )
                        }
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase.invoke()
        }
    }
}

data class ProgressUiState(
    val user: User? = null,
    val subjects: List<Subject> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val totalScore: Int get() = subjects.sumOf { it.currentScore }
    val totalMaxScore: Int get() = subjects.sumOf { it.maxScore }
    val overallProgress: Float get() =
        if (totalMaxScore > 0) totalScore.toFloat() / totalMaxScore else 0f
}