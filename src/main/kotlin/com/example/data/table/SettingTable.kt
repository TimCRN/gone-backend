package com.example.data.table

import org.jetbrains.exposed.sql.Table

object SettingTable: Table() {
    val id = integer("id").autoIncrement()
    val user = integer("user")
        .references(UserTable.id)
    val language = integer("language")
        .references(LanguageTable.id)
    val currency = integer("currency")
        .references(CurrencyTable.id)
    val do_notification = bool("do_notification")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}