package com.example.bitebuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitebuddy.data.model.MenuItem
import com.example.bitebuddy.data.model.Resource
import com.example.bitebuddy.data.model.Restaurant
import com.example.bitebuddy.data.repository.AddItemResult
import com.example.bitebuddy.data.repository.CartRepository
import com.example.bitebuddy.data.repository.MenuRepository
import com.example.bitebuddy.data.repository.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class RestaurantViewModel(
    private val restaurantRepository: RestaurantRepository = RestaurantRepository(),
    private val menuRepository: MenuRepository = MenuRepository(),
    private val cartRepository: CartRepository = CartRepository
) : ViewModel() {

    private val _restaurantState = MutableStateFlow<Resource<Restaurant>>(Resource.Loading)
    val restaurantState: StateFlow<Resource<Restaurant>> = _restaurantState.asStateFlow()

    private val _menuItemsState = MutableStateFlow<Resource<List<MenuItem>>>(Resource.Loading)
    val menuItemsState: StateFlow<Resource<List<MenuItem>>> = _menuItemsState.asStateFlow()

    private val _selectedTab = MutableStateFlow("Menu")
    val selectedTab: StateFlow<String> = _selectedTab.asStateFlow()

    // For the Food Detail Modal / Sheet
    private val _selectedFoodItem = MutableStateFlow<MenuItem?>(null)
    val selectedFoodItem: StateFlow<MenuItem?> = _selectedFoodItem.asStateFlow()

    // For Single-Restaurant cart conflict dialog
    private val _cartConflict = MutableStateFlow<AddItemResult.Conflict?>(null)
    val cartConflict: StateFlow<AddItemResult.Conflict?> = _cartConflict.asStateFlow()

    val cartState = cartRepository.cartState

    fun loadRestaurantAndMenu(restaurantId: String) {
        viewModelScope.launch {
            _restaurantState.value = Resource.Loading
            restaurantRepository.getRestaurantById(restaurantId)
                .catch { e ->
                    _restaurantState.value = Resource.Error(e.message ?: "Failed to load restaurant details")
                }
                .collect { restaurant ->
                    if (restaurant != null) {
                        _restaurantState.value = Resource.Success(restaurant)
                    } else {
                        _restaurantState.value = Resource.Error("Restaurant not found.")
                    }
                }
        }

        viewModelScope.launch {
            _menuItemsState.value = Resource.Loading
            menuRepository.getMenuItems(restaurantId)
                .catch { e ->
                    _menuItemsState.value = Resource.Error(e.message ?: "Failed to load menu items")
                }
                .collect { items ->
                    _menuItemsState.value = Resource.Success(items)
                }
        }
    }

    fun selectTab(tab: String) {
        _selectedTab.value = tab
    }

    fun openFoodDetail(item: MenuItem) {
        _selectedFoodItem.value = item
    }

    fun closeFoodDetail() {
        _selectedFoodItem.value = null
    }

    fun addItemToCart(
        restaurantId: String,
        restaurantName: String,
        item: MenuItem,
        quantity: Int = 1,
        selectedSize: String = "Medium"
    ) {
        val result = cartRepository.addItem(
            restaurantId = restaurantId,
            restaurantName = restaurantName,
            item = item,
            quantity = quantity,
            selectedSize = selectedSize
        )

        if (result is AddItemResult.Conflict) {
            _cartConflict.value = result
        }
    }

    fun resolveConflictAndReplaceCart() {
        val conflict = _cartConflict.value ?: return
        cartRepository.forceReplaceCartWithItem(
            restaurantId = conflict.restaurantId,
            restaurantName = conflict.newRestaurantName,
            item = conflict.item,
            quantity = conflict.quantity,
            selectedSize = conflict.size
        )
        _cartConflict.value = null
        _selectedFoodItem.value = null
    }

    fun dismissConflict() {
        _cartConflict.value = null
    }
}

