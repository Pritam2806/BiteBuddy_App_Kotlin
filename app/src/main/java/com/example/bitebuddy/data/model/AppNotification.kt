package com.example.bitebuddy.data.model

import com.google.firebase.Timestamp

data class AppNotification(
    val notificationId: String = "",
    val type: String = "ORDER_STATUS",
    val orderId: String = "",
    val status: String = "",
    val title: String = "",
    val message: String = "",
    val isRead: Boolean = false,
    val createdAt: Timestamp? = null
)

