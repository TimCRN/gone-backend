package com.example.data.table

import org.jetbrains.exposed.sql.Table

object UserTable: Table() {
    val id = integer("id").autoIncrement()
    val email = varchar("email", 512)
    val username = varchar("username", 512)
    val password = varchar("password", 512)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}