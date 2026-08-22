package com.example.bitebuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.bitebuddy.navigation.AppNavigation
import com.example.bitebuddy.ui.theme.BiteBuddyTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val initialOrderId = intent?.getStringExtra("EXTRA_ORDER_ID")

        setContent {
            BiteBuddyTheme {
                AppNavigation(initialOrderId = initialOrderId)
            }
        }
    }
}