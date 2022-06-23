package com.example.services

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.github.cdimascio.dotenv.dotenv
import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private val hashKey = dotenv()["HASH_SECRET"].toByteArray()
private val hmacKey = SecretKeySpec(hashKey,"HmacSHA1")

fun hash(password: String): String {
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}

class JwtService {
    private val issuer = "GoneIdentity"
    private val jwtSecret = dotenv()["JWT_SECRET"]
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    val verifier:JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(identity: com.gone.data.model.Identity): String {
        return JWT.create()
            .withSubject("GoneAuth")
            .withIssuer(issuer)
            .withClaim("email",identity.email)
            .withClaim("updated_at",identity.updated_at)
            .sign(algorithm)
    }
}