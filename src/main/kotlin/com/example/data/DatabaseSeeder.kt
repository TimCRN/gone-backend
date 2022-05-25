package com.example.data

import com.example.data.model.gender.NewGender
import com.example.repository.GenderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseSeeder {

    suspend fun seedGenders(genders: GenderRepository) {
        // Check if standard values are already present
        if(genders.findById(1) == null || genders.findById(2) == null) {
            genders.addGender(
                NewGender(
                    "MALE",
                    "M"
                )
            )
            genders.addGender(
                NewGender(
                    "FEMALE",
                    "F"
                )
            )
        }
    }

    suspend fun seedPremiums(premiums: PremiumRepository)
}