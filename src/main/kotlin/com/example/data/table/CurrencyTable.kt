package com.example.data.table

import org.jetbrains.exposed.sql.Table

object CurrencyTable: Table() {
    val id = integer("id").autoIncrement()
    val iso = varchar("iso",4)
    val symbol = varchar("symbol",3)
    val functional_unit = varchar("functional_unit",32)
    val exchange_rate_usd = decimal("exchange_rate_usd",12,5)
    val exchange_rate_eur = decimal("exchange_rate_eur",12,5)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}