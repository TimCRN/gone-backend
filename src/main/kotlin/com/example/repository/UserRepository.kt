package com.example.repository

import com.example.ErrorObject
import com.example.data.model.user.NewUser
import com.example.data.model.user.User
import com.example.data.table.UserTable
import com.example.data.DatabaseFactory.dbQuery
import com.example.data.model.request.LoginRequest
import com.example.data.model.request.RegisterRequest
import com.example.data.model.user.Account
import com.example.data.model.user.UserPicture
import com.example.data.table.CountryTable
import com.example.data.table.GenderTable
import com.example.data.table.PremiumTable
import com.example.services.hash
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class UserRepository {
    suspend fun addUser(user: NewUser): User {
        dbQuery {
            UserTable.insert { ut ->
                ut[email] = user.email
                ut[username] = user.username
                ut[birthday] = LocalDate.parse(user.birthday, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                ut[password] = hash(user.password)
                ut[gender] = user.gender
                ut[country] = user.country
                ut[premium] = user.premium
                ut[created] = LocalDateTime.now()
                ut[tag] = user.tag
                ut[activity] = 1
            }
        }
        return getUserByEmail(user.email)!!
    }

    suspend fun getAll() = dbQuery {
        UserTable.selectAll().map {
            rowToUser(it)
        }
    }

    suspend fun checkTagAvailability(username: String, tag: Int): Boolean = !dbQuery {
        UserTable.select { UserTable.tag.eq(tag) and UserTable.username.eq(username) }
            .any()
    }

    suspend fun getUserByEmail(email: String): User? = dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    suspend fun getAccountByEmail(email: String): Account? = dbQuery {
        UserTable
            .innerJoin(GenderTable)
            .innerJoin(CountryTable)
            .innerJoin(PremiumTable)
            .select { UserTable.email.eq(email) }
            .map { rowToAccount(it) }
            .singleOrNull()
    }

    suspend fun checkIfEmailExists(email: String): Boolean = dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .any()
    }

    suspend fun changeEmail(oldEmail: String, newEmail: String) = dbQuery {
        UserTable.update({UserTable.email eq oldEmail}) {
            it[email] = newEmail
        }
    }

    suspend fun changeProfilePicture(email: String, pictureId: UUID) = dbQuery {
        UserTable.update({UserTable.email eq  email}) {
            it[image] = pictureId
        }
    }

    suspend fun getProfilePictureId(email: String): String? {
        val result = dbQuery {
            UserTable.select {UserTable.email.eq(email)}
                .map { rowToPicture(it) }
                .singleOrNull()
        }
        return result?.profilePicture
    }

    fun validatePassword(user: User, password: String): Boolean {
        return user.password == hash(password)
    }
    suspend fun validateRegistration(registerRequest: RegisterRequest): Pair<Boolean, ErrorObject?> {
        if(checkIfEmailExists(registerRequest.email)){
            return false to ErrorObject(
                HttpStatusCode.Conflict,
                "Email is already present in database",
                101
            )
        }
        try {
            LocalDate.parse(registerRequest.birthday, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        }
        catch(e:Exception){
            return false to ErrorObject(
                HttpStatusCode.InternalServerError,
                "Invalid date format, should be 'dd-MM-yyyy'",
                102
            )
        }
        return true to null
    }

    suspend fun validateLogin(loginRequest: LoginRequest): Pair<Boolean, ErrorObject?> {
        val user = getUserByEmail(loginRequest.email)
            ?: return false to ErrorObject(
                HttpStatusCode.BadRequest,
                "Email is not in database",
                201
            )
        if(!validatePassword(user,loginRequest.password)){
            return false to ErrorObject(
                HttpStatusCode.BadRequest,
                "Password is incorrect",
                202
            )
        }
        return true to null
    }

    suspend fun changeUsername(user: User, username: String, tag: Int) = dbQuery {
        UserTable.update({ UserTable.email.eq(user.email) }) {
            it[UserTable.username] = username
            it[UserTable.tag] = tag
        }
    }

    suspend fun changePassword(user: User, password: String) = dbQuery {
        UserTable.update({ UserTable.email.eq(user.email) }) {
            it[UserTable.password] = hash(password)
        }
    }

    private fun rowToUser(row: ResultRow?): User? {
        if(row == null){
            return null
        }
        return User(
            id = row[UserTable.id],
            email = row[UserTable.email],
            username = row[UserTable.username],
            tag = row[UserTable.tag],
            password = row[UserTable.password]
        )
    }

    private fun rowToPicture(row: ResultRow?): UserPicture? {
        if(row == null){
            return null
        }
        return UserPicture(
            profilePicture = row[UserTable.image].toString()
        )
    }

    private fun rowToAccount(row: ResultRow?): Account? {
        if(row == null){
            return null
        }
        return Account(
            email = row[UserTable.email],
            username = row[UserTable.username],
            tag = row[UserTable.tag],
            birthday = row[UserTable.birthday].format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
            gender = row[GenderTable.name],
            country = row[CountryTable.name],
            premium = row[PremiumTable.title],
            image_id = row[UserTable.image]?.toString() ?: run { "None" }
        )
    }
}