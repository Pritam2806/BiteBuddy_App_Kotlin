package com.example.bitebuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bitebuddy.data.model.Order
import com.example.bitebuddy.data.model.OrderStatus
import com.example.bitebuddy.data.model.Resource
import com.example.bitebuddy.ui.components.BiteBuddyCard
import com.example.bitebuddy.ui.components.BiteBuddyPrimaryButton
import com.example.bitebuddy.ui.components.BiteBuddyTopBar
import com.example.bitebuddy.ui.components.ErrorBanner
import com.example.bitebuddy.ui.components.LoadingView
import com.example.bitebuddy.ui.theme.AccentRed
import com.example.bitebuddy.ui.theme.DarkBackground
import com.example.bitebuddy.ui.theme.DarkInput
import com.example.bitebuddy.ui.theme.DarkSurface
import com.example.bitebuddy.ui.theme.DarkSurfaceElevated
import com.example.bitebuddy.ui.theme.OnPrimaryYellow
import com.example.bitebuddy.ui.theme.PrimaryYellow
import com.example.bitebuddy.ui.theme.StatusAccepted
import com.example.bitebuddy.ui.theme.StatusOutForDelivery
import com.example.bitebuddy.ui.theme.StatusPlaced
import com.example.bitebuddy.ui.theme.TextSecondary
import com.example.bitebuddy.ui.theme.TextWhite
import com.example.bitebuddy.ui.viewmodel.OrderTrackingViewModel

@Composable
fun ActiveOrderScreen(
    orderId: String,
    orderTrackingViewModel: OrderTrackingViewModel,
    onBackClick: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val activeOrderState by orderTrackingViewModel.activeOrderState.collectAsState()
    val actionState by orderTrackingViewModel.actionState.collectAsState()

    var showCancelDialog by remember { mutableStateOf(false) }
    var showDeliveryConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(orderId) {
        orderTrackingViewModel.startTracking(orderId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            BiteBuddyTopBar(
                title = "Live Order Tracking",
                onBackClick = onBackClick
            )

            when (val state = activeOrderState) {
                is Resource.Loading -> {
                    LoadingView(
                        message = "Fetching real-time order status...",
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterHorizontally)
                    )
                }
                is Resource.Error, is Resource.Idle -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "No Orders Yet",
                            color = TextWhite,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Your order history will appear here after placing orders.",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        BiteBuddyPrimaryButton(
                            text = "Browse Restaurants",
                            onClick = onNavigateToHome,
                            modifier = Modifier.width(200.dp)
                        )
                    }
                }
                is Resource.Success -> {
                    val order = state.data
                    if (order == null) {
                        // Order was deleted from web or not found -> show clean No Orders Yet empty state
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(DarkSurfaceElevated),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                                    contentDescription = null,
                                    tint = TextSecondary,
                                    modifier = Modifier.size(40.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = "No Orders Yet",
                                color = TextWhite,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Your order history will appear here after placing orders.",
                                color = TextSecondary,
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            BiteBuddyPrimaryButton(
                                text = "Browse Restaurants",
                                onClick = onNavigateToHome,
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Action error banner
                            if (actionState is Resource.Error) {
                                item {
                                    ErrorBanner(message = (actionState as Resource.Error).message)
                                }
                            }

                            // Live Status / Timeline Card
                            item {
                                OrderStatusTimelineCard(order = order)
                            }

                            // Restaurant & Ordered Items
                            item {
                                BiteBuddyCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    backgroundColor = DarkSurface
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Restaurant,
                                                contentDescription = null,
                                                tint = PrimaryYellow,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = order.restaurantName,
                                                color = TextWhite,
                                                fontSize = 17.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(14.dp))

                                        order.items.forEachIndexed { index, item ->
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "${item.quantity}x ${item.itemName}",
                                                    color = TextSecondary,
                                                    fontSize = 14.sp
                                                )
                                                Text(
                                                    text = "₹${"%.2f".format(item.subtotal)}",
                                                    color = TextWhite,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }
                                            if (index < order.items.size - 1) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp)
                                                .background(DarkInput)
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Total Amount",
                                                color = TextWhite,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = order.getFormattedTotal(),
                                                color = PrimaryYellow,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                        }
                                    }
                                }
                            }

                            // Delivery Address Card
                            item {
                                BiteBuddyCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    backgroundColor = DarkSurface
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.LocationOn,
                                                contentDescription = null,
                                                tint = PrimaryYellow,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Delivery Address",
                                                color = TextWhite,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = order.deliveryAddress.toFormattedString(),
                                            color = TextSecondary,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }

                            // Order Info Card
                            item {
                                BiteBuddyCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    backgroundColor = DarkSurface
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Order ID",
                                                color = TextSecondary,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                text = "#${order.orderId.takeLast(8).uppercase()}",
                                                color = TextWhite,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Payment Method",
                                                color = TextSecondary,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                text = "Cash on Delivery",
                                                color = TextWhite,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(80.dp))
                            }
                        }

                        // Bottom Action Area
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DarkSurfaceElevated)
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // 1. If OUT_FOR_DELIVERY: Customer can confirm delivery
                                if (order.orderStatus == OrderStatus.OUT_FOR_DELIVERY) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(StatusAccepted)
                                            .clickable(enabled = actionState !is Resource.Loading) {
                                                showDeliveryConfirmDialog = true
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = Color.Black,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "I Have Received My Order",
                                                color = Color.Black,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                // 2. If PLACED: Customer can cancel the order
                                if (order.orderStatus == OrderStatus.PLACED) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .border(1.dp, AccentRed.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                                            .background(AccentRed.copy(alpha = 0.1f))
                                            .clickable(enabled = actionState !is Resource.Loading) {
                                                showCancelDialog = true
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Cancel,
                                                contentDescription = null,
                                                tint = AccentRed,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Cancel Order",
                                                color = AccentRed,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                // Default Back to Home button
                                BiteBuddyPrimaryButton(
                                    text = "Back to Home",
                                    onClick = onNavigateToHome
                                )
                            }
                        }

                        // Cancel Order Confirmation Dialog
                        if (showCancelDialog) {
                            AlertDialog(
                                onDismissRequest = { showCancelDialog = false },
                                containerColor = DarkSurfaceElevated,
                                title = {
                                    Text(
                                        text = "Cancel Order?",
                                        color = TextWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                },
                                text = {
                                    Text(
                                        text = "Are you sure you want to cancel this order? This action cannot be undone.",
                                        color = TextSecondary,
                                        fontSize = 14.sp
                                    )
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            showCancelDialog = false
                                            orderTrackingViewModel.cancelOrder(
                                                orderId = order.orderId,
                                                reason = "Cancelled by customer"
                                            )
                                        },
                                        colors = ButtonDefaults.textButtonColors(contentColor = PrimaryYellow)
                                    ) {
                                        Text(text = "Yes, Cancel Order", color = PrimaryYellow, fontWeight = FontWeight.Bold)
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = { showCancelDialog = false },
                                        colors = ButtonDefaults.textButtonColors(contentColor = PrimaryYellow)
                                    ) {
                                        Text(text = "Keep Order", color = PrimaryYellow, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            )
                        }

                        // Delivery Confirmation Dialog
                        if (showDeliveryConfirmDialog) {
                            AlertDialog(
                                onDismissRequest = { showDeliveryConfirmDialog = false },
                                containerColor = DarkSurfaceElevated,
                                title = {
                                    Text(
                                        text = "Confirm Delivery",
                                        color = TextWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                },
                                text = {
                                    Text(
                                        text = "Have you received your food order and completed the payment with the delivery person?",
                                        color = TextSecondary,
                                        fontSize = 14.sp
                                    )
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            showDeliveryConfirmDialog = false
                                            orderTrackingViewModel.markOrderDelivered(order.orderId)
                                        },
                                        colors = ButtonDefaults.textButtonColors(contentColor = PrimaryYellow)
                                    ) {
                                        Text(text = "Confirm Received", color = PrimaryYellow, fontWeight = FontWeight.Bold)
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = { showDeliveryConfirmDialog = false },
                                        colors = ButtonDefaults.textButtonColors(contentColor = PrimaryYellow)
                                    ) {
                                        Text(text = "Not Yet", color = PrimaryYellow, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderStatusTimelineCard(order: Order) {
    val currentStatus = order.orderStatus

    if (currentStatus == OrderStatus.CANCELLED) {
        // Cancelled Banner Card
        BiteBuddyCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = DarkSurface
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Order Cancelled",
                        color = AccentRed,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(AccentRed.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "CANCELLED",
                            color = AccentRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = order.cancelReason?.ifBlank { "This order was cancelled." } ?: "This order was cancelled.",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
        return
    }

    val isPlacedActive = true
    val isAcceptedActive = currentStatus == OrderStatus.ACCEPTED || currentStatus == OrderStatus.OUT_FOR_DELIVERY || currentStatus == OrderStatus.DELIVERED
    val isDeliveryActive = currentStatus == OrderStatus.OUT_FOR_DELIVERY || currentStatus == OrderStatus.DELIVERED
    val isDeliveredActive = currentStatus == OrderStatus.DELIVERED

    val (currentTitle, currentDescription, currentColor) = when (currentStatus) {
        OrderStatus.PLACED -> Triple(
            "Order Placed",
            "Waiting for restaurant to accept your order... You can cancel if needed.",
            StatusPlaced
        )
        OrderStatus.ACCEPTED -> Triple(
            "Order Accepted",
            "The restaurant has accepted your order and is preparing the food.",
            StatusAccepted
        )
        OrderStatus.OUT_FOR_DELIVERY -> Triple(
            "Out for Delivery",
            "Your food is on the way! Mark as received once delivered.",
            StatusOutForDelivery
        )
        OrderStatus.DELIVERED -> Triple(
            "Order Delivered",
            "Order Completed. Enjoy your meal!",
            StatusAccepted
        )
        OrderStatus.CANCELLED -> Triple(
            "Order Cancelled",
            order.cancelReason ?: "This order was cancelled.",
            AccentRed
        )
    }

    BiteBuddyCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = DarkSurface
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentTitle,
                    color = currentColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(currentColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (currentStatus == OrderStatus.DELIVERED) "COMPLETED" else "LIVE",
                        color = currentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = currentDescription,
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 4-Step Progress Indicators
            TimelineStepItem(
                stepNumber = "1",
                title = "Order Placed",
                description = "Order submitted to the restaurant",
                isActive = isPlacedActive,
                isCompleted = isAcceptedActive,
                activeColor = StatusPlaced,
                isLast = false
            )

            TimelineStepItem(
                stepNumber = "2",
                title = "Order Accepted",
                description = "Restaurant accepted and is preparing food",
                isActive = isAcceptedActive,
                isCompleted = isDeliveryActive,
                activeColor = StatusAccepted,
                isLast = false
            )

            TimelineStepItem(
                stepNumber = "3",
                title = "Out for Delivery",
                description = "Order dispatched for delivery",
                isActive = isDeliveryActive,
                isCompleted = isDeliveredActive,
                activeColor = StatusOutForDelivery,
                isLast = false
            )

            TimelineStepItem(
                stepNumber = "4",
                title = "Delivered",
                description = "Food delivered to your doorstep",
                isActive = isDeliveredActive,
                isCompleted = isDeliveredActive,
                activeColor = StatusAccepted,
                isLast = true
            )
        }
    }
}

@Composable
fun TimelineStepItem(
    stepNumber: String,
    title: String,
    description: String,
    isActive: Boolean,
    isCompleted: Boolean,
    activeColor: Color,
    isLast: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCompleted -> StatusAccepted
                            isActive -> activeColor
                            else -> DarkInput
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = TextWhite,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text(
                        text = stepNumber,
                        color = if (isActive) TextWhite else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(36.dp)
                        .background(if (isCompleted) StatusAccepted else DarkInput)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 16.dp)) {
            Text(
                text = title,
                color = if (isActive || isCompleted) TextWhite else TextSecondary,
                fontSize = 14.sp,
                fontWeight = if (isActive || isCompleted) FontWeight.Bold else FontWeight.Normal
            )
            Text(
                text = description,
                color = TextSecondary,
                fontSize = 12.sp
            )
        }
    }
}


