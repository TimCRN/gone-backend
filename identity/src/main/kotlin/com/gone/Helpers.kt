package com.gone

import io.ktor.http.*

data class ErrorObject (
    val HttpStatusCode: HttpStatusCode,
    val Message: String,
    val Code: Int
)