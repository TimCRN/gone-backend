package com.example.data.model.activity

import com.example.DateSerializer
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class NewActivity (
    val name: String,
    @Serializable(with = DateSerializer::class)
    val date: Date
)