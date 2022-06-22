package com.example.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val email: String,
    val username: String,
    val tag: Int,
    val birthday: String,
    val gender: String,
    val country: String,
    val premium: String,
    val image_id: String
)
