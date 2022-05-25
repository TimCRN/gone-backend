package com.example.data.model.gender

import kotlinx.serialization.Serializable

@Serializable
data class NewGender(
    val name: String,
    val short: String
)
