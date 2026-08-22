package com.example.bitebuddy.data.model

import com.google.firebase.Timestamp

enum class OrderStatus {
    PLACED,
    ACCEPTED,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED;

    companion object {
        fun fromString(value: String): OrderStatus = when (value.uppercase()) {
            "ACCEPTED" -> ACCEPTED
            "OUT_FOR_DELIVERY" -> OUT_FOR_DELIVERY
            "DELIVERED" -> DELIVERED
            "CANCELLED" -> CANCELLED
            else -> PLACED
        }
    }
}

data class OrderItem(
    val itemId: String = "",
    val itemName: String = "",
    val priceAtOrderTime: Double = 0.0,
    val quantity: Int = 0,
    val subtotal: Double = 0.0,
    val selectedSize: String = "Medium"
)

data class Order(
    val orderId: String = "",
    val customerId: String = "",
    val restaurantId: String = "",
    val restaurantName: String = "",
    val items: List<OrderItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val deliveryAddress: Address = Address(),
    val paymentMethod: String = "Cash on Delivery",
    val status: String = OrderStatus.PLACED.name,
    val cancelReason: String? = null,
    val cancelledAt: Timestamp? = null,
    val deliveredAt: Timestamp? = null,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
) {
    val orderStatus: OrderStatus
        get() = OrderStatus.fromString(status)

    fun getFormattedTotal(): String = "₹${"%.2f".format(totalAmount)}"
}

