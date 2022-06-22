package com.example.data.table

import com.example.data.table.GenderTable.autoIncrement
import com.example.data.table.GenderTable.uniqueIndex
import org.jetbrains.exposed.sql.Table

object PremiumTable: Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title",32).uniqueIndex()
    val description = text("description")
    val price = double("price")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}