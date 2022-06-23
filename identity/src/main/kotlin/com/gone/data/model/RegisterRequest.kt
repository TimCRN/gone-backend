package com.gone.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest (
    val email: String,
    val username: String,
    val birthday: String,
    val password: String,
    val gender: Int,
    val country: Int,
    val premium: Int,
    val profilePicture: String? = null,
)