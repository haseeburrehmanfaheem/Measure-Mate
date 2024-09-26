package com.haseeb.measuremate.presentation.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haseeb.measuremate.domain.repository.AuthRepository
import com.haseeb.measuremate.domain.repository.DatabaseRepository
import com.haseeb.measuremate.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    private val _state = MutableStateFlow(DashboardState())
    val state = combine(
        _state,
        databaseRepository.getSignedInUser(),
//        databaseRepository.getAllBodyPartsWithLatestValue()
    ) { state, user ->
//        val activeBodyParts = bodyParts.filter { it.isActive }
        state.copy(
            user = user,
//            bodyParts = activeBodyParts
        )
    }.catch { e ->
        _uiEvent.send(UiEvent.ShowSnackbar(message = "Something went wrong. ${e.message}"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DashboardState()
    )

    fun onEvent(event: DashboardEvent) {
        when (event) {


            DashboardEvent.SignOut -> {
                signOut()
            }

            is DashboardEvent.AnonymousUserSignInWithGoogle -> {
                anonymousUserSignInWithGoogle(event.context)
            }
        }
    }

    private fun anonymousUserSignInWithGoogle(context: Context) {
        viewModelScope.launch {
            _state.update { it.copy(isSignInButtonLoading = true) }
            authRepository.anonymousUserSignInWithGoogle(context)
                .onSuccess {
                    databaseRepository.addUser()
                        .onSuccess {
                            _uiEvent
                            _uiEvent.send(UiEvent.ShowSnackbar(message = "Signed in successfully"))
                        }
                        .onFailure { e ->
                            _uiEvent.send(UiEvent.ShowSnackbar(message = "Couldn't add user. ${e.message}"))
                        }
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.ShowSnackbar(message = "Couldn't sign in. ${e.message}"))
                }
            _state.update { it.copy(isSignInButtonLoading = false) }
        }
    }

    private fun signOut() {
        viewModelScope.launch {

            authRepository.SignOut()
                .onSuccess {
                    _uiEvent.send(UiEvent.HideBottomSheet)

                    _uiEvent.send(UiEvent.ShowSnackbar(message = "Signed out successfully"))
                }
                .onFailure { e ->
                    _uiEvent.send(UiEvent.ShowSnackbar(message = "Couldn't signed out. ${e.message}"))
                    _uiEvent.send(UiEvent.HideBottomSheet)
                }

        }
    }
}