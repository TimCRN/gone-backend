package com.example.data.model.request

import com.example.DateSerializer
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class RegisterRequest (
    val email: String,
    val username: String,
    val birthday: String,
    val password: String,
    val gender: Int,
    val country: Int,
    val premium: Int,
    val profilePicture: String? = null,
)