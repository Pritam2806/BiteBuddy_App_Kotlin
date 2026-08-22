package com.example.bitebuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bitebuddy.data.model.Resource
import com.example.bitebuddy.ui.components.BiteBuddyPrimaryButton
import com.example.bitebuddy.ui.components.BiteBuddyTextField
import com.example.bitebuddy.ui.components.BiteBuddyTopBar
import com.example.bitebuddy.ui.components.ErrorBanner
import com.example.bitebuddy.ui.theme.DarkBackground
import com.example.bitebuddy.ui.theme.DarkSurfaceElevated
import com.example.bitebuddy.ui.theme.StatusAccepted
import com.example.bitebuddy.ui.theme.TextSecondary
import com.example.bitebuddy.ui.theme.TextWhite
import com.example.bitebuddy.ui.viewmodel.ProfileViewModel

@Composable
fun AddressManagementScreen(
    profileViewModel: ProfileViewModel,
    onBackClick: () -> Unit
) {
    val userProfile by profileViewModel.userProfile.collectAsState()
    val updateState by profileViewModel.updateState.collectAsState()

    var line1 by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var isInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(userProfile) {
        if (!isInitialized && userProfile != null) {
            val addr = userProfile?.address
            if (addr != null) {
                line1 = addr.line1
                city = addr.city
                state = addr.state
                postalCode = addr.postalCode
                isInitialized = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            BiteBuddyTopBar(
                title = "Delivery Address",
                onBackClick = onBackClick
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Default Delivery Location",
                    color = TextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "This address will be automatically selected when ordering food.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (updateState is Resource.Error) {
                    ErrorBanner(message = (updateState as Resource.Error).message)
                    Spacer(modifier = Modifier.height(16.dp))
                } else if (updateState is Resource.Success) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(StatusAccepted.copy(alpha = 0.15f))
                            .padding(14.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Address updated successfully!",
                            color = StatusAccepted,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                BiteBuddyTextField(
                    value = line1,
                    onValueChange = { line1 = it },
                    placeholder = "House No., Building, Street area",
                    label = "Address Line",
                    leadingIcon = Icons.Default.Home,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        BiteBuddyTextField(
                            value = city,
                            onValueChange = { city = it },
                            placeholder = "City",
                            label = "City",
                            leadingIcon = Icons.Default.LocationCity,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                        )
                    }

                    Spacer(modifier = Modifier.padding(horizontal = 6.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        BiteBuddyTextField(
                            value = postalCode,
                            onValueChange = { postalCode = it },
                            placeholder = "Postal Code",
                            label = "PIN Code",
                            leadingIcon = Icons.Default.PinDrop,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                BiteBuddyTextField(
                    value = state,
                    onValueChange = { state = it },
                    placeholder = "State",
                    label = "State",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
            }

            // Bottom Save Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceElevated)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                BiteBuddyPrimaryButton(
                    text = "Save Address",
                    onClick = {
                        profileViewModel.updateAddress(line1, city, state, postalCode)
                    },
                    isLoading = updateState is Resource.Loading
                )
            }
        }
    }
}

