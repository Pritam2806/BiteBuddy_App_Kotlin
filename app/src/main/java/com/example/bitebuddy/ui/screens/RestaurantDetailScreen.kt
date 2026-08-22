package com.example.bitebuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.bitebuddy.data.model.MenuItem
import com.example.bitebuddy.data.model.Resource
import com.example.bitebuddy.data.model.Restaurant
import com.example.bitebuddy.ui.components.BiteBuddyCard
import com.example.bitebuddy.ui.components.BiteBuddyPrimaryButton
import com.example.bitebuddy.ui.components.CategoryChip
import com.example.bitebuddy.ui.components.ErrorBanner
import com.example.bitebuddy.ui.components.LoadingView
import com.example.bitebuddy.ui.theme.AccentRed
import com.example.bitebuddy.ui.theme.DarkBackground
import com.example.bitebuddy.ui.theme.DarkInput
import com.example.bitebuddy.ui.theme.DarkSurface
import com.example.bitebuddy.ui.theme.DarkSurfaceElevated
import com.example.bitebuddy.ui.theme.OnPrimaryYellow
import com.example.bitebuddy.ui.theme.PrimaryYellow
import com.example.bitebuddy.ui.theme.StarGold
import com.example.bitebuddy.ui.theme.StatusAccepted
import com.example.bitebuddy.ui.theme.TextSecondary
import com.example.bitebuddy.ui.theme.TextWhite
import com.example.bitebuddy.ui.viewmodel.RestaurantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    restaurantId: String,
    restaurantViewModel: RestaurantViewModel,
    onBackClick: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val restaurantState by restaurantViewModel.restaurantState.collectAsState()
    val menuItemsState by restaurantViewModel.menuItemsState.collectAsState()
    val selectedTab by restaurantViewModel.selectedTab.collectAsState()
    val selectedFoodItem by restaurantViewModel.selectedFoodItem.collectAsState()
    val cartConflict by restaurantViewModel.cartConflict.collectAsState()
    val cartState by restaurantViewModel.cartState.collectAsState()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(restaurantId) {
        restaurantViewModel.loadRestaurantAndMenu(restaurantId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        when (val rState = restaurantState) {
            is Resource.Loading -> {
                LoadingView(
                    message = "Loading menu...",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is Resource.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    ErrorBanner(message = rState.message)
                    Spacer(modifier = Modifier.height(16.dp))
                    BiteBuddyPrimaryButton(
                        text = "Go Back",
                        onClick = onBackClick
                    )
                }
            }
            is Resource.Success -> {
                val restaurant = rState.data

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = if (!cartState.isEmpty) 90.dp else 24.dp)
                ) {
                    // Hero Image & Top Bar
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(230.dp)
                        ) {
                            if (restaurant.imageUrl.isNotBlank()) {
                                com.example.bitebuddy.ui.components.ProductImageView(
                                    imageUrl = restaurant.imageUrl,
                                    contentDescription = restaurant.name,
                                    modifier = Modifier.fillMaxSize(),
                                    placeholderEmoji = "🍽️",
                                    emojiSize = 48
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(DarkSurfaceElevated),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Restaurant,
                                        contentDescription = null,
                                        tint = PrimaryYellow,
                                        modifier = Modifier.size(64.dp)
                                    )
                                }
                            }

                            // Top Back Button
                            Box(
                                modifier = Modifier
                                    .statusBarsPadding()
                                    .padding(16.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(DarkBackground.copy(alpha = 0.8f))
                                    .clickable { onBackClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = TextWhite,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // Restaurant Info Card
                    item {
                        BiteBuddyCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(top = 12.dp),
                            backgroundColor = DarkSurface
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = restaurant.name,
                                    color = TextWhite,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                if (restaurant.description.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = restaurant.description,
                                        color = TextSecondary,
                                        fontSize = 13.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Metadata row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = PrimaryYellow,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = restaurant.address.city.ifBlank { "Hamirpur" },
                                        color = TextWhite,
                                        fontSize = 12.sp
                                    )

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = StarGold,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "%.1f".format(restaurant.rating),
                                        color = TextWhite,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background((if (restaurant.isOpen) StatusAccepted else AccentRed).copy(alpha = 0.2f))
                                            .padding(horizontal = 7.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (restaurant.isOpen) "Open" else "Closed",
                                            color = if (restaurant.isOpen) StatusAccepted else AccentRed,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Closed Store Notice Banner
                    if (!restaurant.isOpen) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(AccentRed.copy(alpha = 0.15f))
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = "This restaurant is currently closed and not accepting orders.",
                                    color = AccentRed,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Navigation Tabs (Menu / Info)
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CategoryChip(
                                title = "Menu",
                                isSelected = selectedTab == "Menu",
                                onClick = { restaurantViewModel.selectTab("Menu") }
                            )
                            CategoryChip(
                                title = "Info",
                                isSelected = selectedTab == "Info",
                                onClick = { restaurantViewModel.selectTab("Info") }
                            )
                        }
                    }

                    // Tab Content: Menu or Info
                    if (selectedTab == "Menu") {
                        when (val mState = menuItemsState) {
                            is Resource.Loading -> {
                                item {
                                    LoadingView(message = "Loading dishes...")
                                }
                            }
                            is Resource.Error -> {
                                item {
                                    ErrorBanner(
                                        message = mState.message,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                            is Resource.Success -> {
                                if (mState.data.isEmpty()) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(32.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "No items available in the menu yet.",
                                                color = TextSecondary,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }
                                } else {
                                    items(mState.data) { item ->
                                        MenuItemRowCard(
                                            item = item,
                                            isRestaurantOpen = restaurant.isOpen,
                                            onItemClick = {
                                                restaurantViewModel.openFoodDetail(item)
                                            },
                                            onAddClick = {
                                                if (restaurant.isOpen && item.isAvailable) {
                                                    restaurantViewModel.addItemToCart(
                                                        restaurantId = restaurant.restaurantId,
                                                        restaurantName = restaurant.name,
                                                        item = item
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                            else -> {}
                        }
                    } else {
                        // Info Tab
                        item {
                            RestaurantInfoSection(restaurant = restaurant)
                        }
                    }
                }

                // Sticky Bottom Cart Bar (if items present)
                if (!cartState.isEmpty) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(DarkSurfaceElevated)
                            .padding(horizontal = 20.dp, vertical = 14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Total Price (${cartState.itemCount} items)",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = cartState.getFormattedTotal(),
                                    color = TextWhite,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            BiteBuddyPrimaryButton(
                                text = "View Cart",
                                onClick = onNavigateToCart,
                                modifier = Modifier.width(150.dp)
                            )
                        }
                    }
                }
            }
            else -> {}
        }

        // Food Detail Sheet
        selectedFoodItem?.let { item ->
            FoodDetailSheet(
                item = item,
                sheetState = sheetState,
                onDismiss = { restaurantViewModel.closeFoodDetail() },
                onAddToCart = { foodItem, qty, size ->
                    val r = (restaurantState as? Resource.Success)?.data
                    if (r != null) {
                        restaurantViewModel.addItemToCart(
                            restaurantId = r.restaurantId,
                            restaurantName = r.name,
                            item = foodItem,
                            quantity = qty,
                            selectedSize = size
                        )
                        restaurantViewModel.closeFoodDetail()
                    }
                }
            )
        }

        // Single-Restaurant Conflict Dialog
        cartConflict?.let { conflict ->
            AlertDialog(
                onDismissRequest = { restaurantViewModel.dismissConflict() },
                containerColor = DarkSurfaceElevated,
                title = {
                    Text(
                        text = "Replace Cart Items?",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                text = {
                    Text(
                        text = "Your cart contains dishes from \"${conflict.existingRestaurantName}\". Would you like to clear your cart and start a new order from \"${conflict.newRestaurantName}\"?",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { restaurantViewModel.resolveConflictAndReplaceCart() },
                        colors = ButtonDefaults.textButtonColors(contentColor = PrimaryYellow)
                    ) {
                        Text(text = "Replace Cart", color = PrimaryYellow, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { restaurantViewModel.dismissConflict() },
                        colors = ButtonDefaults.textButtonColors(contentColor = PrimaryYellow)
                    ) {
                        Text(text = "Cancel", color = PrimaryYellow, fontWeight = FontWeight.SemiBold)
                    }
                }
            )
        }
    }
}

@Composable
fun MenuItemRowCard(
    item: MenuItem,
    isRestaurantOpen: Boolean = true,
    onItemClick: () -> Unit,
    onAddClick: () -> Unit
) {
    val canAdd = isRestaurantOpen && item.isAvailable

    BiteBuddyCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onItemClick() },
        backgroundColor = if (item.isAvailable) DarkSurface else DarkSurfaceElevated.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Food Thumbnail
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkInput),
                contentAlignment = Alignment.Center
            ) {
                com.example.bitebuddy.ui.components.ProductImageView(
                    imageUrl = item.imageUrl,
                    contentDescription = item.name,
                    modifier = Modifier.fillMaxSize(),
                    placeholderEmoji = "🍕",
                    emojiSize = 32
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    color = if (item.isAvailable) TextWhite else TextSecondary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (item.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.description,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.getFormattedPrice(),
                        color = if (item.isAvailable) PrimaryYellow else TextSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (!item.isAvailable) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Out of Stock",
                            color = AccentRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Add Button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (canAdd) PrimaryYellow else DarkInput)
                    .clickable(enabled = canAdd) { onAddClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add to cart",
                    tint = if (canAdd) OnPrimaryYellow else TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun RestaurantInfoSection(restaurant: Restaurant) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        BiteBuddyCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = DarkSurface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "About",
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = restaurant.description.ifBlank { "Authentic food freshly made to order." },
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Location & Contact",
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = restaurant.address.toFormattedString(),
                    color = TextSecondary,
                    fontSize = 13.sp
                )
                if (restaurant.mobile.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Phone: ${restaurant.mobile}",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

