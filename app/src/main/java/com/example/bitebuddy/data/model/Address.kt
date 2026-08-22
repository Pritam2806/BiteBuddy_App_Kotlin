package com.example.bitebuddy.data.model

data class Address(
    val line1: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: String = ""
) {
    fun toFormattedString(): String {
        val parts = listOf(line1, city, state, postalCode).filter { it.isNotBlank() }
        return if (parts.isNotEmpty()) parts.joinToString(", ") else "No address provided"
    }
}

