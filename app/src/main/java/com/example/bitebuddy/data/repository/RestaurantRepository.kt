package com.example.bitebuddy.data.repository

import com.example.bitebuddy.data.firebase.FirebaseModule
import com.example.bitebuddy.data.model.Restaurant
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RestaurantRepository(
    private val firestore: FirebaseFirestore = FirebaseModule.firestore
) {

    fun getActiveRestaurants(): Flow<List<Restaurant>> = callbackFlow {
        val query = firestore.collection("restaurants")
            .whereEqualTo("status", "active")

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val restaurants = snapshot?.documents?.mapNotNull { doc ->
                val r = doc.toObject(Restaurant::class.java)
                val isOpen = doc.getBoolean("isOpen") ?: doc.getBoolean("open") ?: r?.isOpen ?: true
                val imageUrl = doc.getString("imageUrl")
                    ?: doc.getString("image")
                    ?: doc.getString("photoUrl")
                    ?: doc.getString("photoURL")
                    ?: doc.getString("coverImage")
                    ?: doc.getString("coverImageUrl")
                    ?: doc.getString("coverUrl")
                    ?: doc.getString("bannerUrl")
                    ?: doc.getString("bannerImage")
                    ?: doc.getString("restaurantPhoto")
                    ?: doc.getString("restaurantImage")
                    ?: doc.getString("logoUrl")
                    ?: doc.getString("logo")
                    ?: doc.getString("picture")
                    ?: doc.getString("avatar")
                    ?: r?.imageUrl
                    ?: ""
                r?.copy(
                    restaurantId = doc.id,
                    isOpen = isOpen,
                    imageUrl = imageUrl
                )
            } ?: emptyList()

            trySend(restaurants)
        }
        awaitClose { listener.remove() }
    }

    fun getRestaurantById(restaurantId: String): Flow<Restaurant?> = callbackFlow {
        val docRef = firestore.collection("restaurants").document(restaurantId)
        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val r = snapshot?.toObject(Restaurant::class.java)
            val isOpen = snapshot?.getBoolean("isOpen") ?: snapshot?.getBoolean("open") ?: r?.isOpen ?: true
            val imageUrl = snapshot?.getString("imageUrl")
                ?: snapshot?.getString("image")
                ?: snapshot?.getString("photoUrl")
                ?: snapshot?.getString("photoURL")
                ?: snapshot?.getString("coverImage")
                ?: snapshot?.getString("coverImageUrl")
                ?: snapshot?.getString("coverUrl")
                ?: snapshot?.getString("bannerUrl")
                ?: snapshot?.getString("bannerImage")
                ?: snapshot?.getString("restaurantPhoto")
                ?: snapshot?.getString("restaurantImage")
                ?: snapshot?.getString("logoUrl")
                ?: snapshot?.getString("logo")
                ?: snapshot?.getString("picture")
                ?: snapshot?.getString("avatar")
                ?: r?.imageUrl
                ?: ""
            val restaurant = r?.copy(
                restaurantId = snapshot.id,
                isOpen = isOpen,
                imageUrl = imageUrl
            )
            trySend(restaurant)
        }
        awaitClose { listener.remove() }
    }
}

