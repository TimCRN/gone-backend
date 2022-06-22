package com.example.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PictureUploadRequest(
    val image: String
)
