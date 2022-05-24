package com.example.data.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object TeamUserTable: Table() {
    val id = integer("id").autoIncrement()
    val team = integer("team")
        .references(TeamTable.id)
    val user = integer("user")
        .references(UserTable.id)
    val added = datetime("added")
    val is_leader = bool("is_leader")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}