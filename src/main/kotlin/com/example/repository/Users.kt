package com.example.repository

import com.example.data.model.User
import com.example.data.table.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.insert

class UserRepository {
    suspend fun addUser(user:User) {
        dbQuery {
            UserTable.insert { ut ->
                ut[UserTable.email] = user.email
                ut[UserTable.password] = user.password
            }
        }
    }
}