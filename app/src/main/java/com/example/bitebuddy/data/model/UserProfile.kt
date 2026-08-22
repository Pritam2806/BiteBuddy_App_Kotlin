package com.example.bitebuddy.data.model

import com.google.firebase.Timestamp

data class UserProfile(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val mobile: String = "",
    val profileImageUrl: String = "",
    val role: String = "customer",
    val address: Address = Address(),
    val fcmToken: String? = null,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)

