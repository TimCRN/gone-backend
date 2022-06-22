package com.example.data.table

import org.jetbrains.exposed.sql.Table

object CountryTable: Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name",64)
    val short = varchar("short",4)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}