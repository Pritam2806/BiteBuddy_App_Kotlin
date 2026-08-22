package com.example.bitebuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.bitebuddy.data.repository.CartRepository
import com.example.bitebuddy.data.repository.CartState
import kotlinx.coroutines.flow.StateFlow

class CartViewModel(
    private val cartRepository: CartRepository = CartRepository
) : ViewModel() {

    val cartState: StateFlow<CartState> = cartRepository.cartState

    fun updateQuantity(itemId: String, size: String, newQuantity: Int) {
        cartRepository.updateQuantity(itemId, size, newQuantity)
    }

    fun removeItem(itemId: String, size: String) {
        cartRepository.removeItem(itemId, size)
    }

    fun clearCart() {
        cartRepository.clearCart()
    }
}

