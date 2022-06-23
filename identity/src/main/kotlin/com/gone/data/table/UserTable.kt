package com.gone.data.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object UserTable: Table() {
    val id = integer("id")
        .autoIncrement()
    val email = varchar("email", 64)
        .uniqueIndex()
    val password = varchar("password", 512)
    val updated_at = datetime("updated_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}