package com.example.data.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime

object UserTable: Table() {
    val id = integer("id")
        .autoIncrement()
    val username = varchar("username", 32)
    val tag = integer("tag")
    val email = varchar("email", 64)
        .uniqueIndex()
    val birthday = date("birthday")
    val password = varchar("password", 512)
    val gender = integer("gender")
        .references(GenderTable.id)
    val country = integer("country")
        .references(CountryTable.id)
    val premium = integer("premium")
        .references(PremiumTable.id)
    val activity = integer("activity")
        .references(ActivityTable.id)
    val image = uuid("image")
        .nullable()
    val created = datetime("created_at")
    val updated_at = datetime("updated_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)

    init {
        uniqueIndex(username,tag)
    }
}