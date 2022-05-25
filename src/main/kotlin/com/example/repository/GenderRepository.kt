package com.example.repository

import com.example.data.DatabaseFactory.dbQuery
import com.example.data.model.gender.Gender
import com.example.data.model.gender.NewGender
import com.example.data.table.GenderTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class GenderRepository {
    suspend fun addGender(gender: NewGender): Gender {
        val newId: Int = dbQuery {
            GenderTable.insert { gt ->
                gt[name] = gender.name
                gt[short] = gender.short
            }
        } get GenderTable.id
        return findById(newId)!!
    }

    suspend fun findById(id: Int): Gender? = dbQuery {
        GenderTable.select { GenderTable.id.eq(id) }
            .firstOrNull()
            .let { rowToGender(it) }
    }

    private fun rowToGender(row: ResultRow?): Gender? {
        if(row == null){
            return null
        }
        return Gender(
            id = row[GenderTable.id],
            name = row[GenderTable.name],
            short = row[GenderTable.short]
        )
    }
}