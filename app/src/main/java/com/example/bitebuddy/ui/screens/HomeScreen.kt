@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bitebuddy.data.model.Resource
import com.example.bitebuddy.data.model.Restaurant
import com.example.bitebuddy.ui.components.BiteBuddyCard
import com.example.bitebuddy.ui.components.BiteBuddyTextField
import com.example.bitebuddy.ui.components.ErrorBanner
import com.example.bitebuddy.ui.components.LoadingView
import com.example.bitebuddy.ui.theme.CardBorder
import com.example.bitebuddy.ui.theme.DarkBackground
import com.example.bitebuddy.ui.theme.DarkInput
import com.example.bitebuddy.ui.theme.DarkInputBorder
import com.example.bitebuddy.ui.theme.DarkSurface
import com.example.bitebuddy.ui.theme.DarkSurfaceElevated
import com.example.bitebuddy.ui.theme.OnPrimaryYellow
import com.example.bitebuddy.ui.theme.PrimaryYellow
import com.example.bitebuddy.ui.theme.TextSecondary
import com.example.bitebuddy.ui.theme.TextWhite
import com.example.bitebuddy.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.delay

data class PromoItem(
    val tag: String,
    val title: String,
    val description: String,
    val icon: ImageVector
)

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onSelectRestaurant: (String) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val restaurantsState by homeViewModel.restaurantsState.collectAsState()
    val searchQuery by homeViewModel.searchQuery.collectAsState()
    val userProfile by homeViewModel.userProfile.collectAsState()

    val userName = userProfile?.name?.split(" ")?.firstOrNull() ?: "Foodie"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .statusBarsPadding()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Header: Greeting + Avatar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Hi, $userName",
                            color = TextWhite,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Welcome to Food App of NIT-HAMIRPUR",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(PrimaryYellow)
                            .clickable { onNavigateToProfile() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = OnPrimaryYellow,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }

            // Search Bar
            item {
                BiteBuddyTextField(
                    value = searchQuery,
                    onValueChange = { homeViewModel.updateSearchQuery(it) },
                    placeholder = "Search food or restaurant...",
                    leadingIcon = Icons.Default.Search
                )
            }

            // Moving Horizontal Promo Cards Banner (Auto-scrolling every 2 seconds)
            item {
                PromoCarousel()
            }

            // Section Title: Available Restaurants
            item {
                Text(
                    text = "Active Restaurants",
                    color = TextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Restaurant Cards (Full width, stacked vertically)
            when (val state = restaurantsState) {
                is Resource.Loading -> {
                    item {
                        LoadingView(message = "Finding open restaurants...")
                    }
                }
                is Resource.Error -> {
                    item {
                        ErrorBanner(message = state.message)
                    }
                }
                is Resource.Success -> {
                    val filteredList = state.data.filter { restaurant ->
                        searchQuery.isBlank() ||
                                restaurant.name.contains(searchQuery, ignoreCase = true) ||
                                restaurant.description.contains(searchQuery, ignoreCase = true)
                    }

                    if (filteredList.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No active restaurants found.",
                                    color = TextSecondary,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        items(filteredList, key = { it.restaurantId }) { restaurant ->
                            RestaurantCard(
                                restaurant = restaurant,
                                onClick = { onSelectRestaurant(restaurant.restaurantId) }
                            )
                        }
                    }
                }
                else -> {}
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun PromoCarousel(
    promoItems: List<PromoItem> = remember {
        listOf(
            PromoItem(
                tag = "Special Deal",
                title = "Special Deal",
                description = "50 Rs off on first order",
                icon = Icons.Default.LocalOffer
            ),
            PromoItem(
                tag = "Fast Delivery",
                title = "Fast Delivery",
                description = "Order delivered in 15 mins",
                icon = Icons.Default.DeliveryDining
            ),
            PromoItem(
                tag = "Late Night",
                title = "Late Night Orders",
                description = "Can order till 12:00 AM",
                icon = Icons.Default.AccessTime
            ),
            PromoItem(
                tag = "Bulk Orders",
                title = "Bulk orders Available",
                description = "Can order in bulk upon order",
                icon = Icons.Default.ShoppingBag
            ),
            PromoItem(
                tag = "Festivals",
                title = "Special Discounts",
                description = "Special 10% Discounts on Festivals",
                icon = Icons.Default.Celebration
            )
        )
    }
) {
    val pagerState = rememberPagerState(pageCount = { promoItems.size })

    // Automatically advance cards every 2 seconds
    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000L)
            if (promoItems.isNotEmpty()) {
                val nextPage = (pagerState.currentPage + 1) % promoItems.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 12.dp
        ) { page ->
            val item = promoItems[page]
            BiteBuddyCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                backgroundColor = DarkSurfaceElevated,
                borderColor = CardBorder
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(PrimaryYellow.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = item.tag,
                                color = PrimaryYellow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = item.title,
                            color = TextWhite,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = item.description,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            maxLines = 2
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(DarkInput),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = PrimaryYellow,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }
            }
        }

        // Pager indicator dots
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(promoItems.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .width(if (isSelected) 18.dp else 6.dp)
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) PrimaryYellow else DarkInputBorder)
                )
            }
        }
    }
}

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    onClick: () -> Unit
) {
    BiteBuddyCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        backgroundColor = DarkSurface,
        borderColor = CardBorder
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Restaurant Image / Placeholder thumbnail
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkInput),
                contentAlignment = Alignment.Center
            ) {
                if (restaurant.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = restaurant.imageUrl,
                        contentDescription = restaurant.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = null,
                        tint = PrimaryYellow,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Restaurant Info (Name + Description)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = restaurant.name,
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (restaurant.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = restaurant.description,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Right Arrow Button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(DarkInput),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "View restaurant",
                    tint = PrimaryYellow,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

