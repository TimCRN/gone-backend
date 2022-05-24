package com.example.data.table

import org.jetbrains.exposed.sql.Table

object PremiumTable: Table() {
    val id = integer("id")
    val title = varchar("title",32)
    val description = text("description")
    val price = double("price")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}