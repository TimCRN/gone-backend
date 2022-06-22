package com.example.repository

import com.example.data.DatabaseFactory.dbQuery
import com.example.data.model.premium.NewPremium
import com.example.data.model.premium.Premium
import com.example.data.table.PremiumTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class PremiumRepository {

    suspend fun addPremium(premium: NewPremium): Premium {
        val newId: Int = dbQuery {
            PremiumTable.insert { pt ->
                pt[title] = premium.title
                pt[description] = premium.description
                pt[price] = premium.price
            }
        } get PremiumTable.id
        return findById(newId)!!
    }

    suspend fun findById(id: Int): Premium? = dbQuery {
        PremiumTable.select { PremiumTable.id.eq(id) }
            .firstOrNull()
            .let { rowToPremium(it) }
    }

    private fun rowToPremium(row: ResultRow?): Premium? {
        if(row == null){
            return null
        }
        return Premium(
            id = row[PremiumTable.id],
            title = row[PremiumTable.title],
            description = row[PremiumTable.description],
            price = row[PremiumTable.price]
        )
    }
}