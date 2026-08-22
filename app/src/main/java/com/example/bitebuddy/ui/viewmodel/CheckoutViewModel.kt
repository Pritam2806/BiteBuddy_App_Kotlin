package com.example.bitebuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitebuddy.data.model.Address
import com.example.bitebuddy.data.model.Resource
import com.example.bitebuddy.data.model.UserProfile
import com.example.bitebuddy.data.repository.AuthRepository
import com.example.bitebuddy.data.repository.CartRepository
import com.example.bitebuddy.data.repository.OrderRepository
import com.example.bitebuddy.data.repository.RestaurantRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CheckoutViewModel(
    private val orderRepository: OrderRepository = OrderRepository(),
    private val restaurantRepository: RestaurantRepository = RestaurantRepository(),
    private val cartRepository: CartRepository = CartRepository,
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _orderPlacementState = MutableStateFlow<Resource<String>>(Resource.Idle)
    val orderPlacementState: StateFlow<Resource<String>> = _orderPlacementState.asStateFlow()

    private val _customAddress = MutableStateFlow<Address?>(null)
    val customAddress: StateFlow<Address?> = _customAddress.asStateFlow()

    val cartState = cartRepository.cartState

    val isRestaurantOpen: StateFlow<Boolean> = cartRepository.cartState
        .flatMapLatest { cart ->
            if (cart.restaurantId.isNotBlank()) {
                restaurantRepository.getRestaurantById(cart.restaurantId).map { it?.isOpen ?: true }
            } else {
                flowOf(true)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val userProfile: StateFlow<UserProfile?> = authRepository.authStateFlow
        .flatMapLatest { user ->
            if (user != null) {
                authRepository.observeCustomerProfile(user.uid)
            } else {
                flowOf(null)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun setCustomAddress(address: Address) {
        _customAddress.value = address
    }

    fun placeOrder(onSuccess: (String) -> Unit) {
        val cart = cartState.value
        if (cart.items.isEmpty()) {
            _orderPlacementState.value = Resource.Error("Cart is empty.")
            return
        }

        val deliveryAddress = _customAddress.value
            ?: userProfile.value?.address
            ?: Address()

        if (deliveryAddress.line1.isBlank() || deliveryAddress.city.isBlank()) {
            _orderPlacementState.value = Resource.Error("Please provide a complete delivery address.")
            return
        }

        viewModelScope.launch {
            _orderPlacementState.value = Resource.Loading
            val orderItems = cart.items.map { it.toOrderItem() }

            val result = orderRepository.placeOrder(
                restaurantId = cart.restaurantId,
                restaurantName = cart.restaurantName,
                items = orderItems,
                deliveryAddress = deliveryAddress,
                totalAmount = cart.total
            )

            result.fold(
                onSuccess = { orderId ->
                    _orderPlacementState.value = Resource.Success(orderId)
                    cartRepository.clearCart()
                    onSuccess(orderId)
                },
                onFailure = { error ->
                    _orderPlacementState.value = Resource.Error(
                        error.message ?: "Failed to place order. Please try again."
                    )
                }
            )
        }
    }

    fun resetPlacementState() {
        _orderPlacementState.value = Resource.Idle
    }
}

