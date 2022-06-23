package com.gone.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SimpleResponse (
    val success: Boolean,
    val message: String
)