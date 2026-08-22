package com.example.bitebuddy.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bitebuddy.data.repository.CartItem
import com.example.bitebuddy.ui.components.BiteBuddyCard
import com.example.bitebuddy.ui.components.BiteBuddyPrimaryButton
import com.example.bitebuddy.ui.components.BiteBuddyTopBar
import com.example.bitebuddy.ui.components.QuantityStepper
import com.example.bitebuddy.ui.theme.AccentRed
import com.example.bitebuddy.ui.theme.DarkBackground
import com.example.bitebuddy.ui.theme.DarkInput
import com.example.bitebuddy.ui.theme.DarkSurface
import com.example.bitebuddy.ui.theme.DarkSurfaceElevated
import com.example.bitebuddy.ui.theme.PrimaryYellow
import com.example.bitebuddy.ui.theme.TextSecondary
import com.example.bitebuddy.ui.theme.TextWhite
import com.example.bitebuddy.ui.viewmodel.CartViewModel

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    onBackClick: () -> Unit,
    onNavigateToCheckout: () -> Unit,
    onBrowseRestaurants: () -> Unit
) {
    val cartState by cartViewModel.cartState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            BiteBuddyTopBar(
                title = "Order Review",
                onBackClick = onBackClick,
                actions = {
                    if (!cartState.isEmpty) {
                        IconButton(onClick = { cartViewModel.clearCart() }) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Clear cart",
                                tint = AccentRed,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            )

            if (cartState.isEmpty) {
                // Empty State
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RemoveShoppingCart,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Your Cart is Empty",
                        color = TextWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Add Delicious food items from your favourite restaurants.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    BiteBuddyPrimaryButton(
                        text = "Browse Restaurants",
                        onClick = onBrowseRestaurants,
                        modifier = Modifier.width(220.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Restaurant info tag
                    item {
                        BiteBuddyCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = DarkSurfaceElevated
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Restaurant,
                                    contentDescription = null,
                                    tint = PrimaryYellow,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Restaurant",
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                    Text(
                                        text = cartState.restaurantName,
                                        color = TextWhite,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Cart Items List
                    items(cartState.items) { item ->
                        CartItemRow(
                            cartItem = item,
                            onIncrement = {
                                cartViewModel.updateQuantity(
                                    item.menuItem.itemId,
                                    item.selectedSize,
                                    item.quantity + 1
                                )
                            },
                            onDecrement = {
                                cartViewModel.updateQuantity(
                                    item.menuItem.itemId,
                                    item.selectedSize,
                                    item.quantity - 1
                                )
                            },
                            onRemove = {
                                cartViewModel.removeItem(
                                    item.menuItem.itemId,
                                    item.selectedSize
                                )
                            }
                        )
                    }

                    // Payment Badge Card
                    item {
                        BiteBuddyCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = DarkSurface
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Payments,
                                    contentDescription = null,
                                    tint = PrimaryYellow,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Payment Method",
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "Cash on Delivery (COD)",
                                        color = TextWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Bill Breakdown
                    item {
                        BiteBuddyCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = DarkSurface
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Bill Details",
                                    color = TextWhite,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Item Total", color = TextSecondary, fontSize = 13.sp)
                                    Text(text = cartState.getFormattedSubtotal(), color = TextWhite, fontSize = 13.sp)
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Delivery Fee", color = TextSecondary, fontSize = 13.sp)
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
                                        text = "Total to Pay",
                                        color = TextWhite,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = cartState.getFormattedTotal(),
                                        color = PrimaryYellow,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                // Bottom Sticky Checkout Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkSurfaceElevated)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${cartState.itemCount} items",
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                            Text(
                                text = cartState.getFormattedTotal(),
                                color = TextWhite,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        BiteBuddyPrimaryButton(
                            text = "Checkout",
                            onClick = onNavigateToCheckout,
                            modifier = Modifier.width(160.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    BiteBuddyCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = DarkSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Food Image / Icon
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkInput),
                contentAlignment = Alignment.Center
            ) {
                com.example.bitebuddy.ui.components.ProductImageView(
                    imageUrl = cartItem.menuItem.imageUrl,
                    contentDescription = cartItem.menuItem.name,
                    modifier = Modifier.fillMaxSize(),
                    placeholderEmoji = "🍕",
                    emojiSize = 28
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cartItem.menuItem.name,
                    color = TextWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Text(
                    text = "${cartItem.selectedSize}  •  ₹${"%.2f".format(cartItem.priceAtOrderTime)}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "₹${"%.2f".format(cartItem.subtotal)}",
                    color = PrimaryYellow,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Stepper
            QuantityStepper(
                quantity = cartItem.quantity,
                onIncrement = onIncrement,
                onDecrement = onDecrement,
                buttonSize = 28.dp
            )
        }
    }
}

