package com.example.bitebuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitebuddy.data.model.Resource
import com.example.bitebuddy.data.model.Restaurant
import com.example.bitebuddy.data.model.UserProfile
import com.example.bitebuddy.data.repository.AuthRepository
import com.example.bitebuddy.data.repository.RestaurantRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val restaurantRepository: RestaurantRepository = RestaurantRepository(),
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _restaurantsState = MutableStateFlow<Resource<List<Restaurant>>>(Resource.Loading)
    val restaurantsState: StateFlow<Resource<List<Restaurant>>> = _restaurantsState.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val categories = listOf("All", "Pizza", "Burgers", "Italian", "Sides", "Drinks")

    private var restaurantsJob: Job? = null

    val userProfile: StateFlow<UserProfile?> = authRepository.authStateFlow
        .flatMapLatest { user ->
            if (user != null) {
                authRepository.observeCustomerProfile(user.uid)
            } else {
                flowOf(null)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            authRepository.authStateFlow.collect { user ->
                if (user != null) {
                    loadRestaurants()
                } else {
                    restaurantsJob?.cancel()
                    _restaurantsState.value = Resource.Idle
                }
            }
        }
    }

    fun loadRestaurants() {
        restaurantsJob?.cancel()
        if (authRepository.currentUser == null) {
            _restaurantsState.value = Resource.Idle
            return
        }

        restaurantsJob = viewModelScope.launch {
            _restaurantsState.value = Resource.Loading
            restaurantRepository.getActiveRestaurants()
                .catch { e ->
                    // If user is currently null (e.g. logging out), do not set permission denied error
                    if (authRepository.currentUser != null) {
                        _restaurantsState.value = Resource.Error(e.message ?: "Failed to load restaurants")
                    } else {
                        _restaurantsState.value = Resource.Idle
                    }
                }
                .collect { list ->
                    _restaurantsState.value = Resource.Success(list)
                }
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    override fun onCleared() {
        super.onCleared()
        restaurantsJob?.cancel()
    }
}

