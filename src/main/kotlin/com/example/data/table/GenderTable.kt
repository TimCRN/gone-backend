package com.example.data.table

import org.jetbrains.exposed.sql.Table

object GenderTable: Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name",32)
    val short = varchar("short",3)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}