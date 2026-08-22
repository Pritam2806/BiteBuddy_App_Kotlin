package com.example.bitebuddy.data.repository

import com.example.bitebuddy.data.model.MenuItem
import com.example.bitebuddy.data.model.OrderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CartItem(
    val menuItem: MenuItem,
    val quantity: Int,
    val selectedSize: String = "Medium",
    val priceAtOrderTime: Double = menuItem.price
) {
    val subtotal: Double
        get() = priceAtOrderTime * quantity

    fun toOrderItem(): OrderItem = OrderItem(
        itemId = menuItem.itemId,
        itemName = menuItem.name,
        priceAtOrderTime = priceAtOrderTime,
        quantity = quantity,
        subtotal = subtotal,
        selectedSize = selectedSize
    )
}

data class CartState(
    val restaurantId: String = "",
    val restaurantName: String = "",
    val items: List<CartItem> = emptyList(),
    val deliveryFee: Double = 0.0 // Free delivery for MVP
) {
    val itemCount: Int
        get() = items.sumOf { it.quantity }

    val subtotal: Double
        get() = items.sumOf { it.subtotal }

    val total: Double
        get() = subtotal + deliveryFee

    val isEmpty: Boolean
        get() = items.isEmpty()

    fun getFormattedTotal(): String = "₹${"%.2f".format(total)}"
    fun getFormattedSubtotal(): String = "₹${"%.2f".format(subtotal)}"
}

sealed interface AddItemResult {
    data object Success : AddItemResult
    data class Conflict(
        val existingRestaurantName: String,
        val newRestaurantName: String,
        val restaurantId: String,
        val item: MenuItem,
        val quantity: Int,
        val size: String
    ) : AddItemResult
}

object CartRepository {

    private val _cartState = MutableStateFlow(CartState())
    val cartState: StateFlow<CartState> = _cartState.asStateFlow()

    fun addItem(
        restaurantId: String,
        restaurantName: String,
        item: MenuItem,
        quantity: Int = 1,
        selectedSize: String = "Medium"
    ): AddItemResult {
        val current = _cartState.value

        // Enforce Single-Restaurant Rule
        if (current.restaurantId.isNotBlank() &&
            current.restaurantId != restaurantId &&
            current.items.isNotEmpty()
        ) {
            return AddItemResult.Conflict(
                existingRestaurantName = current.restaurantName,
                newRestaurantName = restaurantName,
                restaurantId = restaurantId,
                item = item,
                quantity = quantity,
                size = selectedSize
            )
        }

        _cartState.update { state ->
            val existingIndex = state.items.indexOfFirst {
                it.menuItem.itemId == item.itemId && it.selectedSize == selectedSize
            }
            val newItems = state.items.toMutableList()

            if (existingIndex != -1) {
                val existing = newItems[existingIndex]
                newItems[existingIndex] = existing.copy(quantity = existing.quantity + quantity)
            } else {
                newItems.add(
                    CartItem(
                        menuItem = item,
                        quantity = quantity,
                        selectedSize = selectedSize,
                        priceAtOrderTime = item.price
                    )
                )
            }

            state.copy(
                restaurantId = restaurantId,
                restaurantName = restaurantName,
                items = newItems
            )
        }

        return AddItemResult.Success
    }

    fun forceReplaceCartWithItem(
        restaurantId: String,
        restaurantName: String,
        item: MenuItem,
        quantity: Int = 1,
        selectedSize: String = "Medium"
    ) {
        _cartState.value = CartState(
            restaurantId = restaurantId,
            restaurantName = restaurantName,
            items = listOf(
                CartItem(
                    menuItem = item,
                    quantity = quantity,
                    selectedSize = selectedSize,
                    priceAtOrderTime = item.price
                )
            )
        )
    }

    fun updateQuantity(itemId: String, size: String, newQuantity: Int) {
        _cartState.update { state ->
            if (newQuantity <= 0) {
                val updatedItems = state.items.filterNot {
                    it.menuItem.itemId == itemId && it.selectedSize == size
                }
                if (updatedItems.isEmpty()) {
                    CartState()
                } else {
                    state.copy(items = updatedItems)
                }
            } else {
                val updatedItems = state.items.map {
                    if (it.menuItem.itemId == itemId && it.selectedSize == size) {
                        it.copy(quantity = newQuantity)
                    } else {
                        it
                    }
                }
                state.copy(items = updatedItems)
            }
        }
    }

    fun removeItem(itemId: String, size: String) {
        updateQuantity(itemId, size, 0)
    }

    fun clearCart() {
        _cartState.value = CartState()
    }
}

