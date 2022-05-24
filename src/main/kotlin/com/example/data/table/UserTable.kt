package com.example.data.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime

object UserTable: Table() {
    val id = integer("id")
        .autoIncrement()
    val username = varchar("username", 32)
    val email = varchar("email", 64)
    val birthday = date("birthday")
    val password = varchar("password", 512)
    val gender = integer("gender")
        .references(GenderTable.id)
    val country = integer("country")
    val premium = integer("premium")
        .references(PremiumTable.id)
    val created = datetime("created")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}