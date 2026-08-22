package com.example.bitebuddy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bitebuddy.ui.theme.AccentRed
import com.example.bitebuddy.ui.theme.DarkSurface
import com.example.bitebuddy.ui.theme.PrimaryYellow
import com.example.bitebuddy.ui.theme.StatusAccepted
import com.example.bitebuddy.ui.theme.StatusOutForDelivery
import com.example.bitebuddy.ui.theme.StatusPlaced
import com.example.bitebuddy.ui.theme.TextWhite

@Composable
fun OrderStatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (label, bgColor, textColor) = when (status.uppercase()) {
        "PLACED" -> Triple("Order Placed", StatusPlaced.copy(alpha = 0.15f), StatusPlaced)
        "ACCEPTED" -> Triple("Accepted", StatusAccepted.copy(alpha = 0.15f), StatusAccepted)
        "OUT_FOR_DELIVERY" -> Triple("Out for Delivery", StatusOutForDelivery.copy(alpha = 0.15f), StatusOutForDelivery)
        "DELIVERED" -> Triple("Delivered", StatusAccepted.copy(alpha = 0.15f), StatusAccepted)
        "CANCELLED" -> Triple("Cancelled", AccentRed.copy(alpha = 0.15f), AccentRed)
        else -> Triple(status, DarkSurface, TextWhite)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = textColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    message: String = "Loading..."
) {
    Box(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(
                color = PrimaryYellow,
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.5.dp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                color = TextWhite,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ErrorBanner(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AccentRed.copy(alpha = 0.15f))
            .padding(14.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = message,
            color = AccentRed,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

