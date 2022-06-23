package com.gone.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Identity(
    val email: String,
    val password: String,
    val updated_at: String
)
