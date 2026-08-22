package com.example.bitebuddy.data.repository

import com.example.bitebuddy.data.firebase.FirebaseModule
import com.example.bitebuddy.data.model.Address
import com.example.bitebuddy.data.model.Order
import com.example.bitebuddy.data.model.OrderItem
import com.example.bitebuddy.data.model.OrderStatus
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class OrderRepository(
    private val firestore: FirebaseFirestore = FirebaseModule.firestore,
    private val functions: FirebaseFunctions = FirebaseModule.functions,
    private val auth: FirebaseAuth = FirebaseModule.auth
) {

    suspend fun placeOrder(
        restaurantId: String,
        restaurantName: String,
        items: List<OrderItem>,
        deliveryAddress: Address,
        totalAmount: Double
    ): Result<String> {
        val currentUserId = auth.currentUser?.uid ?: return Result.failure(
            IllegalStateException("User is not authenticated.")
        )

        return runCatching {
            // Verify restaurant is open before placing order
            val restaurantDoc = firestore.collection("restaurants").document(restaurantId).get().await()
            val isOpen = restaurantDoc.getBoolean("isOpen") ?: restaurantDoc.getBoolean("open") ?: true
            if (!isOpen) {
                throw IllegalStateException("This restaurant is currently closed and not accepting orders.")
            }

            val orderRef = firestore.collection("orders").document()
            val orderId = orderRef.id

            val orderData = hashMapOf(
                "orderId" to orderId,
                "customerId" to currentUserId,
                "restaurantId" to restaurantId,
                "restaurantName" to restaurantName,
                "items" to items.map {
                    hashMapOf(
                        "itemId" to it.itemId,
                        "itemName" to it.itemName,
                        "priceAtOrderTime" to it.priceAtOrderTime,
                        "quantity" to it.quantity,
                        "subtotal" to it.subtotal,
                        "selectedSize" to it.selectedSize
                    )
                },
                "totalAmount" to totalAmount,
                "deliveryAddress" to hashMapOf(
                    "line1" to deliveryAddress.line1,
                    "city" to deliveryAddress.city,
                    "state" to deliveryAddress.state,
                    "postalCode" to deliveryAddress.postalCode
                ),
                "paymentMethod" to "Cash on Delivery",
                "status" to "PLACED",
                "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                "updatedAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )

            orderRef.set(orderData).await()
            orderId
        }
    }

    /**
     * Customer Order Cancellation
     * Can ONLY be called when order.status == "PLACED".
     */
    suspend fun cancelOrder(orderId: String, reason: String = "Cancelled by customer"): Result<Unit> = runCatching {
        val orderDoc = firestore.collection("orders").document(orderId).get().await()
        val status = orderDoc.getString("status") ?: OrderStatus.PLACED.name
        if (!status.equals(OrderStatus.PLACED.name, ignoreCase = true)) {
            throw IllegalStateException("Orders can only be cancelled before they are accepted by the restaurant.")
        }

        firestore.collection("orders").document(orderId).update(
            mapOf(
                "status" to OrderStatus.CANCELLED.name,
                "cancelReason" to reason,
                "cancelledAt" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                "updatedAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )
        ).await()
    }

    /**
     * Customer Delivery Confirmation
     * Called when order.status == "OUT_FOR_DELIVERY".
     */
    suspend fun markOrderDelivered(orderId: String): Result<Unit> = runCatching {
        val orderDoc = firestore.collection("orders").document(orderId).get().await()
        val status = orderDoc.getString("status") ?: ""
        if (!status.equals(OrderStatus.OUT_FOR_DELIVERY.name, ignoreCase = true)) {
            throw IllegalStateException("Only orders that are out for delivery can be marked as delivered.")
        }

        firestore.collection("orders").document(orderId).update(
            mapOf(
                "status" to OrderStatus.DELIVERED.name,
                "deliveredAt" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                "updatedAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
            )
        ).await()
    }

    /**
     * Real-time listener for the active order document.
     * Reactively emits when status changes to ACCEPTED, OUT_FOR_DELIVERY, DELIVERED, or CANCELLED.
     */
    fun listenToOrder(orderId: String): Flow<Order?> = callbackFlow {
        val docRef = firestore.collection("orders").document(orderId)
        val registration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // If deleted from website or permission denied on non-existent document, emit null
                trySend(null)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val order = snapshot.toObject(Order::class.java)?.copy(orderId = snapshot.id)
                trySend(order)
            } else {
                trySend(null)
            }
        }
        awaitClose { registration.remove() }
    }

    /**
     * Real-time listener for all orders belonging to the customer.
     */
    fun listenToCustomerOrders(customerId: String): Flow<List<Order>> = callbackFlow {
        val query = firestore.collection("orders")
            .whereEqualTo("customerId", customerId)

        val registration = query.addSnapshotListener { snapshots, error ->
            if (error != null) {
                trySend(emptyList())
                return@addSnapshotListener
            }
            val orders = snapshots?.documents?.mapNotNull { doc ->
                doc.toObject(Order::class.java)?.copy(orderId = doc.id)
            }?.sortedByDescending { it.createdAt } ?: emptyList()
            trySend(orders)
        }
        awaitClose { registration.remove() }
    }
}

