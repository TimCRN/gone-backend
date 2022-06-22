package com.example.data.model.request

@kotlinx.serialization.Serializable
data class UsernameChangeRequest(
    val new_username: String,
    val password: String
)
