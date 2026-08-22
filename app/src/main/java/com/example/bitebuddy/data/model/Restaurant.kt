package com.example.bitebuddy.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

enum class RestaurantStatus {
    ACTIVE,
    INACTIVE;

    companion object {
        fun fromString(value: String): RestaurantStatus =
            if (value.equals("active", ignoreCase = true)) ACTIVE else INACTIVE
    }
}

data class Restaurant(
    val restaurantId: String = "",
    val name: String = "",
    val ownerId: String = "",
    val ownerName: String = "",
    val email: String = "",
    val mobile: String = "",
    val address: Address = Address(),
    val description: String = "",
    val imageUrl: String = "",
    val status: String = "active",
    @get:PropertyName("isOpen")
    val isOpen: Boolean = true,
    val rating: Double = 4.8,
    val deliveryTimeMinutes: Int = 25,
    val distanceKm: Double = 2.5,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
) {
    val isActive: Boolean
        get() = status.equals("active", ignoreCase = true)
}


