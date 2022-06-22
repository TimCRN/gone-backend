package com.example.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse (
    val success: Boolean,
    val message: String,
    val code: Int = 0
)