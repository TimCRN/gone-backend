package com.example.data.table

import org.jetbrains.exposed.sql.Table

object LanguageTable: Table() {
    val id = integer("id").autoIncrement()
    val language = varchar("language",64)
    val short = varchar("short",3)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}