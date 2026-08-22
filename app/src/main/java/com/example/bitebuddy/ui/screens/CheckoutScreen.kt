package com.example.bitebuddy.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bitebuddy.data.model.Address
import com.example.bitebuddy.data.model.Resource
import com.example.bitebuddy.ui.components.BiteBuddyCard
import com.example.bitebuddy.ui.components.BiteBuddyPrimaryButton
import com.example.bitebuddy.ui.components.BiteBuddyTextField
import com.example.bitebuddy.ui.components.BiteBuddyTopBar
import com.example.bitebuddy.ui.components.ErrorBanner
import com.example.bitebuddy.ui.theme.DarkBackground
import com.example.bitebuddy.ui.theme.DarkInput
import com.example.bitebuddy.ui.theme.DarkSurface
import com.example.bitebuddy.ui.theme.DarkSurfaceElevated
import com.example.bitebuddy.ui.theme.PrimaryYellow
import com.example.bitebuddy.ui.theme.TextSecondary
import com.example.bitebuddy.ui.theme.TextWhite
import com.example.bitebuddy.ui.viewmodel.CheckoutViewModel

@Composable
fun CheckoutScreen(
    checkoutViewModel: CheckoutViewModel,
    onBackClick: () -> Unit,
    onOrderPlacedSuccess: (String) -> Unit
) {
    val cartState by checkoutViewModel.cartState.collectAsState()
    val isRestaurantOpen by checkoutViewModel.isRestaurantOpen.collectAsState()
    val userProfile by checkoutViewModel.userProfile.collectAsState()
    val customAddress by checkoutViewModel.customAddress.collectAsState()
    val orderState by checkoutViewModel.orderPlacementState.collectAsState()

    var showEditAddressDialog by remember { mutableStateOf(false) }

    val activeAddress = customAddress ?: userProfile?.address ?: Address()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            BiteBuddyTopBar(
                title = "Checkout",
                onBackClick = onBackClick
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Closed Store Banner
                if (!isRestaurantOpen) {
                    item {
                        ErrorBanner(message = "This restaurant is currently closed and not accepting orders.")
                    }
                }

                // Error Banner
                if (orderState is Resource.Error) {
                    item {
                        ErrorBanner(message = (orderState as Resource.Error).message)
                    }
                }

                // Delivery Address Card
                item {
                    BiteBuddyCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = DarkSurface
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = PrimaryYellow,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Delivery Address",
                                        color = TextWhite,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.clickable { showEditAddressDialog = true }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        tint = PrimaryYellow,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Change",
                                        color = PrimaryYellow,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = activeAddress.toFormattedString(),
                                color = TextSecondary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                // Order Items Summary
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
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = cartState.restaurantName,
                                    color = TextWhite,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            cartState.items.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${item.quantity}x  ${item.menuItem.name} (${item.selectedSize})",
                                        color = TextSecondary,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "₹${"%.2f".format(item.subtotal)}",
                                        color = TextWhite,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                // Payment Method Card
                item {
                    BiteBuddyCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = DarkSurface
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Payments,
                                contentDescription = null,
                                tint = PrimaryYellow,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Cash on Delivery",
                                    color = TextWhite,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Pay cash or UPI upon delivery handover",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                // Bill Summary
                item {
                    BiteBuddyCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = DarkSurface
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Payment Summary",
                                color = TextWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Items Subtotal", color = TextSecondary, fontSize = 13.sp)
                                Text(text = cartState.getFormattedSubtotal(), color = TextWhite, fontSize = 13.sp)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Delivery Charges", color = TextSecondary, fontSize = 13.sp)
                                Text(text = "Free", color = PrimaryYellow, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(DarkInput)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Grand Total",
                                    color = TextWhite,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = cartState.getFormattedTotal(),
                                    color = PrimaryYellow,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Place Order Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceElevated)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                BiteBuddyPrimaryButton(
                    text = if (!isRestaurantOpen) "Restaurant Closed" else "Confirm & Place Order",
                    enabled = isRestaurantOpen && orderState !is Resource.Loading,
                    onClick = {
                        checkoutViewModel.placeOrder { orderId ->
                            onOrderPlacedSuccess(orderId)
                        }
                    },
                    isLoading = orderState is Resource.Loading
                )
            }
        }

        // Edit Address Dialog
        if (showEditAddressDialog) {
            var tempLine1 by remember { mutableStateOf(activeAddress.line1) }
            var tempCity by remember { mutableStateOf(activeAddress.city) }
            var tempState by remember { mutableStateOf(activeAddress.state) }
            var tempPostal by remember { mutableStateOf(activeAddress.postalCode) }

            AlertDialog(
                onDismissRequest = { showEditAddressDialog = false },
                containerColor = DarkSurfaceElevated,
                title = {
                    Text(
                        text = "Edit Delivery Address",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        BiteBuddyTextField(
                            value = tempLine1,
                            onValueChange = { tempLine1 = it },
                            placeholder = "House / Flat / Street name",
                            label = "Address Line"
                        )
                        BiteBuddyTextField(
                            value = tempCity,
                            onValueChange = { tempCity = it },
                            placeholder = "City",
                            label = "City"
                        )
                        BiteBuddyTextField(
                            value = tempPostal,
                            onValueChange = { tempPostal = it },
                            placeholder = "Postal Code",
                            label = "PIN Code"
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            checkoutViewModel.setCustomAddress(
                                Address(
                                    line1 = tempLine1,
                                    city = tempCity,
                                    state = tempState,
                                    postalCode = tempPostal
                                )
                            )
                            showEditAddressDialog = false
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = PrimaryYellow)
                    ) {
                        Text(text = "Save", color = PrimaryYellow, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showEditAddressDialog = false },
                        colors = ButtonDefaults.textButtonColors(contentColor = PrimaryYellow)
                    ) {
                        Text(text = "Cancel", color = PrimaryYellow, fontWeight = FontWeight.SemiBold)
                    }
                }
            )
        }
    }
}

