package com.example.bitebuddy.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.bitebuddy.MainActivity
import com.example.bitebuddy.R
import com.example.bitebuddy.data.firebase.FirebaseModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class BiteBuddyMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        try {
            FirebaseFirestore.getInstance().collection("users").document(uid)
                .update("fcmToken", token)
        } catch (_: Exception) {}
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val orderId = remoteMessage.data["orderId"] ?: ""
        val status = remoteMessage.data["status"] ?: ""
        val title = remoteMessage.notification?.title
            ?: if (status == "ACCEPTED") "Order Accepted" else if (status == "OUT_FOR_DELIVERY") "Order Out for Delivery" else "Order Update"
        val body = remoteMessage.notification?.body
            ?: if (status == "ACCEPTED") "Your order has been accepted by the restaurant." else if (status == "OUT_FOR_DELIVERY") "Your order is out for delivery." else "Your order status has been updated."

        showNotification(title, body, orderId)
    }

    private fun showNotification(title: String, body: String, orderId: String) {
        val channelId = "bitebuddy_order_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Order Status Updates",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for order acceptance and delivery tracking."
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            if (orderId.isNotBlank()) {
                putExtra("EXTRA_ORDER_ID", orderId)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            orderId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(orderId.hashCode().takeIf { it != 0 } ?: 1001, notification)
    }
}

