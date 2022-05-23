package com.example.repository

import com.example.data.model.NewUser
import com.example.data.model.User
import com.example.data.table.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserRepository {
    suspend fun addUser(user: NewUser): User {
        dbQuery {
            UserTable.insert { ut ->
                ut[email] = user.email
                ut[username] = user.username
                ut[password] = user.password
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