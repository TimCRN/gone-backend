package com.example.data.model.gender

@kotlinx.serialization.Serializable
data class Gender(
    val id: Int,
    val name: String,
    val short: String
)
