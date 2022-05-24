package com.example.data.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object TripTable: Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title",32)
    val description = text("description")
    val created = datetime("created")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}