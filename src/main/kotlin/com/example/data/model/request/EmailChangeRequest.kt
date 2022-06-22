package com.example.data.model.request

@kotlinx.serialization.Serializable
data class EmailChangeRequest(
    val new_email: String,
    val password: String
)
