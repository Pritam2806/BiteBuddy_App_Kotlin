package com.example.bitebuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitebuddy.data.model.Order
import com.example.bitebuddy.data.model.Resource
import com.example.bitebuddy.data.repository.AuthRepository
import com.example.bitebuddy.data.repository.OrderRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class OrderTrackingViewModel(
    private val orderRepository: OrderRepository = OrderRepository(),
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _activeOrderState = MutableStateFlow<Resource<Order?>>(Resource.Loading)
    val activeOrderState: StateFlow<Resource<Order?>> = _activeOrderState.asStateFlow()

    private val _orderHistoryState = MutableStateFlow<Resource<List<Order>>>(Resource.Loading)
    val orderHistoryState: StateFlow<Resource<List<Order>>> = _orderHistoryState.asStateFlow()

    private val _actionState = MutableStateFlow<Resource<Unit>>(Resource.Idle)
    val actionState: StateFlow<Resource<Unit>> = _actionState.asStateFlow()

    private var trackingJob: Job? = null
    private var historyJob: Job? = null

    fun startTracking(orderId: String) {
        trackingJob?.cancel()
        trackingJob = viewModelScope.launch {
            _activeOrderState.value = Resource.Loading
            orderRepository.listenToOrder(orderId)
                .catch {
                    _activeOrderState.value = Resource.Success(null)
                }
                .collect { order ->
                    _activeOrderState.value = Resource.Success(order)
                }
        }
    }

    fun cancelOrder(orderId: String, reason: String = "Cancelled by customer", onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            _actionState.value = Resource.Loading
            val result = orderRepository.cancelOrder(orderId, reason)
            result.fold(
                onSuccess = {
                    _actionState.value = Resource.Success(Unit)
                    onComplete()
                },
                onFailure = { error ->
                    _actionState.value = Resource.Error(error.message ?: "Failed to cancel order.")
                }
            )
        }
    }

    fun markOrderDelivered(orderId: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            _actionState.value = Resource.Loading
            val result = orderRepository.markOrderDelivered(orderId)
            result.fold(
                onSuccess = {
                    _actionState.value = Resource.Success(Unit)
                    onComplete()
                },
                onFailure = { error ->
                    _actionState.value = Resource.Error(error.message ?: "Failed to update delivery status.")
                }
            )
        }
    }

    fun loadOrderHistory() {
        val uid = authRepository.currentUser?.uid ?: return
        historyJob?.cancel()
        historyJob = viewModelScope.launch {
            _orderHistoryState.value = Resource.Loading
            orderRepository.listenToCustomerOrders(uid)
                .catch {
                    _orderHistoryState.value = Resource.Success(emptyList())
                }
                .collect { orders ->
                    _orderHistoryState.value = Resource.Success(orders)
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        trackingJob?.cancel()
        historyJob?.cancel()
    }
}

