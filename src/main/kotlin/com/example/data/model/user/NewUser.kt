package com.example.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class NewUser(
    val email: String,
    val birthday: String,
    val username: String,
    val password: String,
    val tag: Int,
    val gender: Int,
    val country: Int,
    val premium: Int,
    val profilePicture: String? = null
)
