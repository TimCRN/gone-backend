package com.example.data

import com.example.data.table.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import io.github.cdimascio.dotenv.dotenv

object DatabaseFactory {
    fun init() {
        val dotenv = dotenv()
        val config = HikariConfig()
        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = "jdbc:postgresql://${dotenv["DB_HOST"]}/${dotenv["DB_NAME"]}?user=${dotenv["DB_USER"]}&password=${dotenv["DB_PASSWORD"]}"
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        Database.connect(HikariDataSource(config))

        // TODO Add migrations
        transaction {
            SchemaUtils.create(ActivityTable)
            SchemaUtils.create(UserTable)
            SchemaUtils.create(TripTable)
            SchemaUtils.create(TeamUserTable)
            SchemaUtils.create(TeamTable)
            SchemaUtils.create(SettingTable)
            SchemaUtils.create(PremiumTable)
            SchemaUtils.create(LanguageTable)
            SchemaUtils.create(GenderTable)
            SchemaUtils.create(CurrencyTable)
            SchemaUtils.create(CountryTable)
        }
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}