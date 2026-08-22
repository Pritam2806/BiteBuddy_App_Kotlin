package com.example.bitebuddy.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class MenuItem(
    val itemId: String = "",
    val restaurantId: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    @get:PropertyName("isAvailable")
    val isAvailable: Boolean = true,
    val imageUrl: String = "",
    val category: String = "Popular",
    val prepTimeMinutes: Int = 20,
    val calories: Int = 650,
    val weightGrams: Int = 400,
    val availableSizes: List<String> = listOf("Small", "Medium", "Large"),
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
) {
    fun getFormattedPrice(): String = "₹${"%.2f".format(price)}"
}

