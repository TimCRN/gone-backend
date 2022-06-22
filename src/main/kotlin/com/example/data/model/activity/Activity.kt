package com.example.data.model.activity

import com.example.DateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Activity(
    val id: Int,
    val name: String,
    @Serializable(with = DateSerializer::class)
    val date: LocalDateTime
)