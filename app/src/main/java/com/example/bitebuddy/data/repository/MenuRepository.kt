package com.example.bitebuddy.data.repository

import com.example.bitebuddy.data.firebase.FirebaseModule
import com.example.bitebuddy.data.model.MenuItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MenuRepository(
    private val firestore: FirebaseFirestore = FirebaseModule.firestore
) {

    fun getMenuItems(restaurantId: String): Flow<List<MenuItem>> = callbackFlow {
        val query = firestore.collection("restaurants")
            .document(restaurantId)
            .collection("menuItems")

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val items = snapshot?.documents?.mapNotNull { doc ->
                val item = doc.toObject(MenuItem::class.java)
                val isAvailable = doc.getBoolean("isAvailable") ?: doc.getBoolean("available") ?: item?.isAvailable ?: true
                val imageUrl = doc.getString("imageUrl")
                    ?: doc.getString("image")
                    ?: doc.getString("photoUrl")
                    ?: doc.getString("photoURL")
                    ?: doc.getString("imgUrl")
                    ?: doc.getString("picture")
                    ?: item?.imageUrl
                    ?: ""
                item?.copy(
                    itemId = doc.id,
                    restaurantId = restaurantId,
                    isAvailable = isAvailable,
                    imageUrl = imageUrl
                )
            } ?: emptyList()

            trySend(items)
        }
        awaitClose { listener.remove() }
    }

    fun getMenuItem(restaurantId: String, itemId: String): Flow<MenuItem?> = callbackFlow {
        val docRef = firestore.collection("restaurants")
            .document(restaurantId)
            .collection("menuItems")
            .document(itemId)

        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val item = snapshot?.toObject(MenuItem::class.java)
            val isAvailable = snapshot?.getBoolean("isAvailable") ?: snapshot?.getBoolean("available") ?: item?.isAvailable ?: true
            val imageUrl = snapshot?.getString("imageUrl")
                ?: snapshot?.getString("image")
                ?: snapshot?.getString("photoUrl")
                ?: snapshot?.getString("photoURL")
                ?: snapshot?.getString("imgUrl")
                ?: snapshot?.getString("picture")
                ?: item?.imageUrl
                ?: ""
            val mappedItem = item?.copy(
                itemId = snapshot.id,
                restaurantId = restaurantId,
                isAvailable = isAvailable,
                imageUrl = imageUrl
            )
            trySend(mappedItem)
        }
        awaitClose { listener.remove() }
    }
}

