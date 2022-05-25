package com.example.data.model.premium

import kotlinx.serialization.Serializable

@Serializable
data class Premium (
    val id: Int,
    val title: String,
    val description: String,
    val price: Double
)