package com.example.bitebuddy.data.repository

import com.example.bitebuddy.data.firebase.FirebaseModule
import com.example.bitebuddy.data.model.Address
import com.example.bitebuddy.data.model.UserProfile
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseModule.auth,
    private val firestore: FirebaseFirestore = FirebaseModule.firestore
) {

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    val isUserLoggedIn: Boolean
        get() = auth.currentUser != null

    val authStateFlow: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun registerCustomer(
        name: String,
        email: String,
        mobile: String,
        password: String,
        address: Address
    ): Result<UserProfile> {
        return runCatching {
            val authResult = auth.createUserWithEmailAndPassword(email.trim(), password).await()
            val uid = authResult.user?.uid ?: throw IllegalStateException("Failed to retrieve user ID.")

            var token: String? = null
            try {
                token = FirebaseModule.messaging.token.await()
            } catch (_: Exception) {}

            val profile = UserProfile(
                userId = uid,
                name = name.trim(),
                email = email.trim(),
                mobile = mobile.trim(),
                role = "customer",
                address = address,
                fcmToken = token,
                createdAt = Timestamp.now(),
                updatedAt = Timestamp.now()
            )

            firestore.collection("users").document(uid).set(profile).await()
            profile
        }
    }

    suspend fun loginCustomer(email: String, password: String): Result<UserProfile> {
        return runCatching {
            val authResult = auth.signInWithEmailAndPassword(email.trim(), password).await()
            val uid = authResult.user?.uid ?: throw IllegalStateException("Failed to sign in.")

            val docSnapshot = firestore.collection("users").document(uid).get().await()
            val profile = docSnapshot.toObject(UserProfile::class.java)
                ?: UserProfile(userId = uid, email = email, role = "customer")

            // Sync FCM token
            try {
                val token = FirebaseModule.messaging.token.await()
                if (token != null && token != profile.fcmToken) {
                    firestore.collection("users").document(uid).update("fcmToken", token).await()
                }
            } catch (_: Exception) {}

            profile
        }
    }

    fun observeCustomerProfile(userId: String): Flow<UserProfile?> = callbackFlow {
        val listener = firestore.collection("users").document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val profile = snapshot?.toObject(UserProfile::class.java)
                val profileImage = snapshot?.getString("profileImageUrl")
                    ?: snapshot?.getString("imageUrl")
                    ?: snapshot?.getString("photoUrl")
                    ?: snapshot?.getString("image")
                    ?: profile?.profileImageUrl
                    ?: ""
                val mappedProfile = profile?.copy(
                    userId = snapshot?.id ?: userId,
                    profileImageUrl = profileImage
                )
                trySend(mappedProfile)
            }
        awaitClose { listener.remove() }
    }

    suspend fun updateDeliveryAddress(userId: String, address: Address): Result<Unit> {
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

    fun logout() {
        auth.signOut()
        CartRepository.clearCart()
    }
}

