package com.example.data.model.user

import com.example.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class NewUser(
    val id: Int? = null,
    val email: String,
    @Serializable(with = DateSerializer::class)
    val birthday: Date,
    val username: String,
    val password: String,
    val gender: Int,
    val country: Int,
    val premium: Int,
    val profilePicture: String? = null
)
