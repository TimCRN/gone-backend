package com.example.data.model.request

@kotlinx.serialization.Serializable
data class PasswordChangeRequest(
    val old_password: String,
    val new_password: String
)
