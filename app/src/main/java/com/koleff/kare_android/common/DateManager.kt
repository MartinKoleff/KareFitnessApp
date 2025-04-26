package com.koleff.kare_android.common

import android.os.Build
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import kotlin.random.Random

object DateManager {


    fun getRandomDateInCurrentMonth(): Date {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val now = LocalDate.now()
            val startOfMonth = now.withDayOfMonth(1)
            val endOfMonth = now.withDayOfMonth(now.lengthOfMonth())

            val randomDay = Random.nextInt(startOfMonth.dayOfMonth, endOfMonth.dayOfMonth + 1)
            val randomDate = LocalDate.of(now.year, now.month, randomDay)

            // Optional: add a random time to the date
            val randomTime = LocalTime.of(Random.nextInt(0, 24), Random.nextInt(0, 60))
            val randomDateTime = LocalDateTime.of(randomDate, randomTime)

            return Date.from(randomDateTime.atZone(ZoneId.systemDefault()).toInstant())
        }
        return Date()
    }

    fun convertDateToLocalDate(date: Date): LocalDate {
        return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}