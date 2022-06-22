package com.example.data

import com.example.data.model.activity.Activity
import com.example.data.model.activity.NewActivity
import com.example.data.model.gender.NewGender
import com.example.data.model.premium.NewPremium
import com.example.data.model.user.NewUser
import com.example.repository.ActivityRepository
import com.example.repository.GenderRepository
import com.example.repository.PremiumRepository
import com.example.repository.UserRepository
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Except
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.util.*

object DatabaseSeeder {

    suspend fun seedDatabase(
        genders: GenderRepository,
        premiums: PremiumRepository,
        activities: ActivityRepository,
        users: UserRepository
    ){
        seedGenders(genders)
        seedPremiums(premiums)
        seedActivities(activities)
        seedUsers(users)
    }
    private suspend fun seedGenders(genders: GenderRepository) {
        try {
            genders.addGender(NewGender(
                "MALE",
                "M"
            ))
            genders.addGender(NewGender(
                "FEMALE",
                "F"
            ))
        } catch (e:Exception) {}
    }

    private suspend fun seedPremiums(premiums: PremiumRepository) {
        try {
            premiums.addPremium(NewPremium(
                "DEFAULT",
                "The default premium",
                0.0
            ))
        } catch (e:Exception) {}
    }

    private suspend fun seedActivities(activities: ActivityRepository){
        try{
            activities.addActivity(NewActivity(
                "Active",
                Date()
            ))
        } catch (e:Exception) {}
    }

    private suspend fun seedUsers(users: UserRepository) {
        try {
            users.addUser(
                NewUser(
                    dotenv()["PGADMIN_EMAIL"],
                    "15-06-1990",
                    "default",
                    dotenv()["PGADMIN_PASSWORD"],
                    1,
                    1,
                    1,
                    1
                )
            )
        } catch (e: Exception) {}
    }
}