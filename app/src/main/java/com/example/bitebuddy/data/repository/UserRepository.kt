package com.example.bitebuddy.data.repository

import com.example.bitebuddy.data.firebase.FirebaseModule
import com.example.bitebuddy.data.model.Address
import com.example.bitebuddy.data.model.UserProfile
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val firestore: FirebaseFirestore = FirebaseModule.firestore
) {

    suspend fun updateAddress(userId: String, address: Address): Result<Unit> {
        return runCatching {
            firestore.collection("users").document(userId)
                .update(
                    mapOf(
                        "address" to address,
                        "updatedAt" to Timestamp.now()
                    )
                ).await()
        }
    }

    suspend fun updateProfile(
        userId: String,
        name: String,
        mobile: String,
        profileImageUrl: String? = null
    ): Result<Unit> {
        return runCatching {
            val updates = mutableMapOf<String, Any>(
                "name" to name.trim(),
                "mobile" to mobile.trim(),
                "updatedAt" to Timestamp.now()
            )
            if (!profileImageUrl.isNullOrBlank()) {
                updates["profileImageUrl"] = profileImageUrl
                updates["imageUrl"] = profileImageUrl
            }
            firestore.collection("users").document(userId)
                .update(updates)
                .await()
        }
    }

    suspend fun updateFcmToken(userId: String, token: String): Result<Unit> {
        return runCatching {
            firestore.collection("users").document(userId)
                .update("fcmToken", token)
                .await()
        }
    }
}

