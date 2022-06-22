package com.example.repository

import com.example.data.DatabaseFactory
import com.example.data.model.activity.Activity
import com.example.data.model.activity.NewActivity
import com.example.data.table.ActivityTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.time.ZoneId

class ActivityRepository {

    suspend fun addActivity(activity: NewActivity): Activity {
        val newId: Int = DatabaseFactory.dbQuery {
            ActivityTable.insert { at ->
                at[name] = activity.name
                at[date] = activity.date
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
            }
        } get ActivityTable.id
        return findById(newId)!!
    }

    suspend fun findById(id: Int): Activity? = DatabaseFactory.dbQuery {
        ActivityTable.select { ActivityTable.id.eq(id) }
            .firstOrNull()
            .let { rowToActivity(it) }
    }

    private fun rowToActivity(row: ResultRow?): Activity? {
        if(row == null){
            return null
        }
        return Activity(
            id = row[ActivityTable.id],
            name = row[ActivityTable.name],
            date = row[ActivityTable.date]
        )
    }
}