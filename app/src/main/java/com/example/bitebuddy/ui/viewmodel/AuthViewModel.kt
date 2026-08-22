package com.example.bitebuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitebuddy.data.model.Address
import com.example.bitebuddy.data.model.Resource
import com.example.bitebuddy.data.model.UserProfile
import com.example.bitebuddy.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow<Resource<UserProfile>>(Resource.Idle)
    val authState: StateFlow<Resource<UserProfile>> = _authState.asStateFlow()

    val isUserLoggedIn: Boolean
        get() = authRepository.isUserLoggedIn

    val currentUserId: String?
        get() = authRepository.currentUser?.uid

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = Resource.Error("Please enter email and password.")
            return
        }

        viewModelScope.launch {
            _authState.value = Resource.Loading
            val result = authRepository.loginCustomer(email, password)
            _authState.value = result.fold(
                onSuccess = { Resource.Success(it) },
                onFailure = { Resource.Error(it.message ?: "Authentication failed.") }
            )
        }
    }

    fun register(
        name: String,
        email: String,
        mobile: String,
        password: String,
        line1: String,
        city: String,
        state: String,
        postalCode: String
    ) {
        if (name.isBlank() || email.isBlank() || mobile.isBlank() || password.isBlank()) {
            _authState.value = Resource.Error("Please fill in all required fields.")
            return
        }
        if (password.length < 6) {
            _authState.value = Resource.Error("Password must be at least 6 characters.")
            return
        }

        viewModelScope.launch {
            _authState.value = Resource.Loading
            val address = Address(
                line1 = line1.trim(),
                city = city.trim(),
                state = state.trim(),
                postalCode = postalCode.trim()
            )
            val result = authRepository.registerCustomer(name, email, mobile, password, address)
            _authState.value = result.fold(
                onSuccess = { Resource.Success(it) },
                onFailure = { Resource.Error(it.message ?: "Registration failed.") }
            )
        }
    }

    fun resetState() {
        _authState.value = Resource.Idle
    }

    fun logout() {
        authRepository.logout()
        _authState.value = Resource.Idle
    }
}

