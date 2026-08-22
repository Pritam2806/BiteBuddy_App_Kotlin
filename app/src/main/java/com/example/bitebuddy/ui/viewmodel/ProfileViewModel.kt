package com.example.bitebuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitebuddy.data.model.Address
import com.example.bitebuddy.data.model.Resource
import com.example.bitebuddy.data.model.UserProfile
import com.example.bitebuddy.data.repository.AuthRepository
import com.example.bitebuddy.data.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _updateState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val updateState: StateFlow<Resource<Unit>> = _updateState.asStateFlow()

    val currentUserId: String?
        get() = authRepository.currentUser?.uid

    val userProfile: StateFlow<UserProfile?> = authRepository.authStateFlow
        .flatMapLatest { user ->
            if (user != null) {
                authRepository.observeCustomerProfile(user.uid)
            } else {
                flowOf(null)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun updateAddress(line1: String, city: String, state: String, postalCode: String) {
        val uid = currentUserId ?: return
        viewModelScope.launch {
            _updateState.value = Resource.Loading
            val address = Address(line1.trim(), city.trim(), state.trim(), postalCode.trim())
            val result = userRepository.updateAddress(uid, address)
            _updateState.value = result.fold(
                onSuccess = { Resource.Success(Unit) },
                onFailure = { Resource.Error(it.message ?: "Failed to update address.") }
            )
        }
    }

    fun updateProfile(
        name: String,
        mobile: String,
        profileImageUrl: String? = null,
        onComplete: () -> Unit = {}
    ) {
        val uid = currentUserId ?: return
        viewModelScope.launch {
            _updateState.value = Resource.Loading
            val result = userRepository.updateProfile(uid, name.trim(), mobile.trim(), profileImageUrl)
            _updateState.value = result.fold(
                onSuccess = {
                    onComplete()
                    Resource.Success(Unit)
                },
                onFailure = { Resource.Error(it.message ?: "Failed to update profile.") }
            )
        }
    }

    fun resetUpdateState() {
        _updateState.value = Resource.Idle
    }

    fun logout() {
        authRepository.logout()
    }
}

