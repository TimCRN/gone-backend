package com.gone.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse (
    val success: Boolean,
    val message: String,
    val code: Int = 0
)