package com.example.repository

import com.example.data.model.user.NewUser
import com.example.data.model.user.User
import com.example.data.table.UserTable
import com.example.data.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.time.LocalDateTime
import java.time.ZoneId

class UserRepository {
    suspend fun addUser(user: NewUser): User {
        dbQuery {
            UserTable.insert { ut ->
                ut[email] = user.email
                ut[username] = user.username
                ut[birthday] = user.birthday
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                ut[password] = user.password
                ut[gender] = user.gender
                ut[country] = user.country
                ut[premium] = user.premium
                ut[created] = LocalDateTime.now()

            }
        }
        return findByEmail(user.email)!!
    }

    suspend fun findByEmail(email: String): User? = dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    suspend fun checkIfEmailExists(email: String): Boolean = dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .any()
    }

    private fun rowToUser(row: ResultRow?): User? {
        if(row == null){
            return null
        }
        return User(
            id = row[UserTable.id],
            email = row[UserTable.email],
            username = row[UserTable.username],
            password = row[UserTable.password]
        )
    }
}