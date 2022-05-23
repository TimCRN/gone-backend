package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val success: Boolean,
    val token: String
)