package com.gone.repository

import com.gone.ErrorObject
import com.gone.data.DatabaseFactory.dbQuery
import com.gone.data.model.Identity
import com.gone.data.model.LoginRequest
import com.gone.data.table.UserTable
import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import java.time.format.DateTimeFormatter
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class IdentityRepository {

    private val hashKey = dotenv()["HASH_SECRET"].toByteArray()
    private val hmacKey = SecretKeySpec(hashKey,"HmacSHA1")

    suspend fun getIdentity(id: String): Identity? = dbQuery {
        UserTable.select { UserTable.email.eq(id) }
            .map {rowToIdentity(it)}
            .singleOrNull()
    }

    suspend fun validateLogin(loginRequest: LoginRequest): Pair<Boolean, ErrorObject?> {
        val identity = getIdentity(loginRequest.email)
            ?: return false to ErrorObject(
                HttpStatusCode.BadRequest,
                "Email is not in database",
                201
            )
        if(!validatePassword(identity,loginRequest.password)){
            return false to ErrorObject(
                HttpStatusCode.BadRequest,
                "Password is incorrect",
                202
            )
        }
        return true to null
    }

    private fun rowToIdentity(row: ResultRow?): Identity? {
        if(row == null){
            return null
        }
        return Identity(
            email = row[UserTable.email],
            password = row[UserTable.password],
            updated_at = row[UserTable.updated_at].format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        )
    }

    private fun validatePassword(identity: Identity, password: String): Boolean {
        return identity.password == hash(password)
    }

    private fun hash(password: String): String {
        val hmac = Mac.getInstance("HmacSHA1")
        hmac.init(hmacKey)
        return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
    }
}