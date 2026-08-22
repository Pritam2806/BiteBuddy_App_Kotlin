package com.example.bitebuddy.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

object FirebaseModule {

    val auth: FirebaseAuth by lazy { Firebase.auth }
    val firestore: FirebaseFirestore by lazy { Firebase.firestore }
    val functions: FirebaseFunctions by lazy { Firebase.functions }
    val storage: FirebaseStorage by lazy { Firebase.storage }
    val messaging: FirebaseMessaging by lazy { FirebaseMessaging.getInstance() }

    private var emulatorsConfigured = false

    fun setupEmulatorsIfDebug(emulatorHost: String = "10.0.2.2") {
        if (!emulatorsConfigured) {
            try {
                // To connect to local Firebase Emulators if needed
                // auth.useEmulator(emulatorHost, 9099)
                // firestore.useEmulator(emulatorHost, 8080)
                // functions.useEmulator(emulatorHost, 5001)
                // storage.useEmulator(emulatorHost, 9199)
                emulatorsConfigured = true
            } catch (_: Exception) {
                // Ignore if already initialized or live Firebase is used
            }
        }
    }
}

