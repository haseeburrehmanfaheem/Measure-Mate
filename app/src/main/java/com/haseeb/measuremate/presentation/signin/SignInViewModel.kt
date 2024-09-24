package com.haseeb.measuremate.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haseeb.measuremate.domain.repository.AuthRepository
import com.haseeb.measuremate.presentation.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onEvent(event : SignInEvent){
        when(event){
            SignInEvent.SignInAnonymously -> {
                signInAnonymously()
            }
            is SignInEvent.SignInWithGoogle -> {

            }
        }
    }

    private fun signInAnonymously(){
        viewModelScope.launch {
            _state.update {
                it.copy(isAnonymousSignInButtonLoading = true)
            }
            authRepository.SignInAnonymously().onSuccess {
                _uiEvent.send(UiEvent.ShowSnackbar("Signed in anonymously"))


            }.onFailure {
                _uiEvent.send(UiEvent.ShowSnackbar("Failed to sign in anonymously ${it.message}"))
            }
            _state.update {
                it.copy(isAnonymousSignInButtonLoading = false)
            }
        }
    }
}