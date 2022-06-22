package com.example.data.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object ActivityTable: Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name",32)
    val date = datetime("date")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}