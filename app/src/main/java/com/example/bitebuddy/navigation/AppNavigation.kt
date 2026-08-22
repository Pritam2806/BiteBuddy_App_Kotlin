package com.example.bitebuddy.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bitebuddy.ui.screens.ActiveOrderScreen
import com.example.bitebuddy.ui.screens.AddressManagementScreen
import com.example.bitebuddy.ui.screens.CartScreen
import com.example.bitebuddy.ui.screens.CheckoutScreen
import com.example.bitebuddy.ui.screens.HomeScreen
import com.example.bitebuddy.ui.screens.LoginScreen
import com.example.bitebuddy.ui.screens.OrderHistoryScreen
import com.example.bitebuddy.ui.screens.ProfileScreen
import com.example.bitebuddy.ui.screens.RegisterScreen
import com.example.bitebuddy.ui.screens.RestaurantDetailScreen
import com.example.bitebuddy.ui.screens.SplashScreen
import com.example.bitebuddy.ui.theme.DarkBackground
import com.example.bitebuddy.ui.theme.DarkSurfaceElevated
import com.example.bitebuddy.ui.theme.OnPrimaryYellow
import com.example.bitebuddy.ui.theme.PrimaryYellow
import com.example.bitebuddy.ui.theme.TextSecondary
import com.example.bitebuddy.ui.theme.TextWhite
import com.example.bitebuddy.ui.viewmodel.AuthViewModel
import com.example.bitebuddy.ui.viewmodel.CartViewModel
import com.example.bitebuddy.ui.viewmodel.CheckoutViewModel
import com.example.bitebuddy.ui.viewmodel.HomeViewModel
import com.example.bitebuddy.ui.viewmodel.OrderTrackingViewModel
import com.example.bitebuddy.ui.viewmodel.ProfileViewModel
import com.example.bitebuddy.ui.viewmodel.RestaurantViewModel

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object RestaurantDetail : Screen("restaurant/{restaurantId}") {
        fun createRoute(restaurantId: String) = "restaurant/$restaurantId"
    }
    data object Cart : Screen("cart")
    data object Checkout : Screen("checkout")
    data object ActiveOrder : Screen("order/{orderId}") {
        fun createRoute(orderId: String) = "order/$orderId"
    }
    data object Orders : Screen("orders")
    data object Profile : Screen("profile")
    data object AddressManagement : Screen("address_management")
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    initialOrderId: String? = null
) {
    val authViewModel: AuthViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val restaurantViewModel: RestaurantViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val checkoutViewModel: CheckoutViewModel = viewModel()
    val orderTrackingViewModel: OrderTrackingViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val cartState by cartViewModel.cartState.collectAsState()

    val bottomNavItems = listOf(
        BottomNavItem(
            route = Screen.Home.route,
            label = "Home",
            selectedIcon = Icons.Filled.Fastfood,
            unselectedIcon = Icons.Outlined.Fastfood
        ),
        BottomNavItem(
            route = Screen.Cart.route,
            label = "Cart",
            selectedIcon = Icons.Filled.ShoppingCart,
            unselectedIcon = Icons.Outlined.ShoppingCart
        ),
        BottomNavItem(
            route = Screen.Orders.route,
            label = "Orders",
            selectedIcon = Icons.Filled.ReceiptLong,
            unselectedIcon = Icons.Outlined.ReceiptLong
        ),
        BottomNavItem(
            route = Screen.Profile.route,
            label = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        )
    )

    val showBottomNav = currentRoute in listOf(
        Screen.Home.route,
        Screen.Cart.route,
        Screen.Orders.route,
        Screen.Profile.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomNav) {
                NavigationBar(
                    containerColor = DarkSurfaceElevated,
                    tonalElevation = 8.dp,
                    modifier = Modifier.height(68.dp)
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.route

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(Screen.Home.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                if (item.route == Screen.Cart.route && cartState.itemCount > 0) {
                                    BadgedBox(
                                        badge = {
                                            Badge(
                                                containerColor = PrimaryYellow,
                                                contentColor = OnPrimaryYellow
                                            ) {
                                                Text(
                                                    text = cartState.itemCount.toString(),
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = item.label,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.label,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = PrimaryYellow,
                                unselectedIconColor = TextSecondary,
                                selectedTextColor = PrimaryYellow,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBackground)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    isLoggedIn = authViewModel.isUserLoggedIn,
                    onNavigateToHome = {
                        if (!initialOrderId.isNullOrBlank()) {
                            navController.navigate(Screen.ActiveOrder.createRoute(initialOrderId)) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }
                    },
                    onNavigateToWelcome = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Login.route) {
                LoginScreen(
                    authViewModel = authViewModel,
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    authViewModel = authViewModel,
                    onNavigateToLogin = { navController.popBackStack() },
                    onRegisterSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    homeViewModel = homeViewModel,
                    onSelectRestaurant = { restaurantId ->
                        navController.navigate(Screen.RestaurantDetail.createRoute(restaurantId))
                    },
                    onNavigateToProfile = {
                        navController.navigate(Screen.Profile.route)
                    }
                )
            }

            composable(
                route = Screen.RestaurantDetail.route,
                arguments = listOf(navArgument("restaurantId") { type = NavType.StringType })
            ) { backStackEntry ->
                val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""
                RestaurantDetailScreen(
                    restaurantId = restaurantId,
                    restaurantViewModel = restaurantViewModel,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToCart = { navController.navigate(Screen.Cart.route) }
                )
            }

            composable(Screen.Cart.route) {
                CartScreen(
                    cartViewModel = cartViewModel,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToCheckout = { navController.navigate(Screen.Checkout.route) },
                    onBrowseRestaurants = { navController.navigate(Screen.Home.route) }
                )
            }

            composable(Screen.Checkout.route) {
                CheckoutScreen(
                    checkoutViewModel = checkoutViewModel,
                    onBackClick = { navController.popBackStack() },
                    onOrderPlacedSuccess = { orderId ->
                        navController.navigate(Screen.ActiveOrder.createRoute(orderId)) {
                            popUpTo(Screen.Home.route)
                        }
                    }
                )
            }

            composable(
                route = Screen.ActiveOrder.route,
                arguments = listOf(navArgument("orderId") { type = NavType.StringType })
            ) { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                ActiveOrderScreen(
                    orderId = orderId,
                    orderTrackingViewModel = orderTrackingViewModel,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Orders.route) {
                OrderHistoryScreen(
                    orderTrackingViewModel = orderTrackingViewModel,
                    onBackClick = { navController.navigate(Screen.Home.route) },
                    onTrackOrder = { orderId ->
                        navController.navigate(Screen.ActiveOrder.createRoute(orderId))
                    },
                    onBrowseRestaurants = { navController.navigate(Screen.Home.route) }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    profileViewModel = profileViewModel,
                    onBackClick = { navController.navigate(Screen.Home.route) },
                    onNavigateToAddress = { navController.navigate(Screen.AddressManagement.route) },
                    onNavigateToOrders = { navController.navigate(Screen.Orders.route) },
                    onLogoutSuccess = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.AddressManagement.route) {
                AddressManagementScreen(
                    profileViewModel = profileViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

