package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest (
    val email: String,
    val username: String,
    val password: String,
    val profilePicture: String? = null,
)