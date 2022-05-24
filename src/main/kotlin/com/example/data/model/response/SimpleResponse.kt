package com.example.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SimpleResponse (
    val success: Boolean,
    val message: String
)