package com.example.bitebuddy.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bitebuddy.R
import com.example.bitebuddy.data.model.Resource
import com.example.bitebuddy.ui.components.BiteBuddyPrimaryButton
import com.example.bitebuddy.ui.components.BiteBuddyTextField
import com.example.bitebuddy.ui.components.ErrorBanner
import com.example.bitebuddy.ui.theme.DarkBackground
import com.example.bitebuddy.ui.theme.DarkInput
import com.example.bitebuddy.ui.theme.OnPrimaryYellow
import com.example.bitebuddy.ui.theme.PrimaryYellow
import com.example.bitebuddy.ui.theme.TextSecondary
import com.example.bitebuddy.ui.theme.TextWhite
import com.example.bitebuddy.ui.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var line1 by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var agreeTerms by remember { mutableStateOf(true) }

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is Resource.Success) {
            authViewModel.resetState()
            onRegisterSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            // App Logo
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "BiteBuddy Logo",
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Title & Subtitle
            Text(
                text = "Sign up",
                color = TextWhite,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Please register now to get started",
                color = TextSecondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (authState is Resource.Error) {
                ErrorBanner(message = (authState as Resource.Error).message)
                Spacer(modifier = Modifier.height(16.dp))
            }

            BiteBuddyTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = "Full name",
                label = "Full name",
                leadingIcon = Icons.Default.Person,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(14.dp))

            BiteBuddyTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "E-mail address",
                label = "E-mail address",
                leadingIcon = Icons.Default.Email,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            BiteBuddyTextField(
                value = mobile,
                onValueChange = { mobile = it },
                placeholder = "+91 9876543210",
                label = "Mobile number",
                leadingIcon = Icons.Default.Phone,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            BiteBuddyTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password (min 6 characters)",
                label = "Password",
                leadingIcon = Icons.Default.Lock,
                isPassword = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Delivery Address",
                color = PrimaryYellow,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            BiteBuddyTextField(
                value = line1,
                onValueChange = { line1 = it },
                placeholder = "House / Flat / Street name",
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
                        placeholder = "PIN Code",
                        label = "Postal Code",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
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

            Spacer(modifier = Modifier.height(18.dp))

            // Terms check
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = agreeTerms,
                    onCheckedChange = { agreeTerms = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = PrimaryYellow,
                        checkmarkColor = OnPrimaryYellow,
                        uncheckedColor = DarkInput
                    )
                )
                Text(
                    text = "I agree with Terms and Privacy Policy",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            BiteBuddyPrimaryButton(
                text = "Sign Up",
                onClick = {
                    authViewModel.register(
                        name = name,
                        email = email,
                        mobile = mobile,
                        password = password,
                        line1 = line1,
                        city = city,
                        state = state,
                        postalCode = postalCode
                    )
                },
                enabled = agreeTerms,
                isLoading = authState is Resource.Loading
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = "Sign in",
                    color = PrimaryYellow,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

