package com.koleff.kare_android.common

import android.os.Build
import androidx.annotation.RequiresApi
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random

object DateUtils {

    private val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    private val displayFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat(
        "HH:mm", Locale.getDefault()
    ) //24-hour format / Use "hh:mm" for 12-hour format with AM/PM

    @RequiresApi(Build.VERSION_CODES.O)
    val dateComparator = Comparator<String> { date1, date2 ->
        when {
            date1 == "Today" && date2 != "Today" -> 1  // Today goes last
            date1 != "Today" && date2 == "Today" -> -1

            date1 == "Yesterday" && date2 != "Yesterday" -> 1 // Yesterday before today
            date1 != "Yesterday" && date2 == "Yesterday" -> -1

            else -> {
                val parsedDate1 = parseDisplayDate(date1)
                val parsedDate2 = parseDisplayDate(date2)

                when {
                    parsedDate1 == null && parsedDate2 == null -> 0
                    parsedDate1 == null -> 1
                    parsedDate2 == null -> -1
                    else -> parsedDate1.compareTo(parsedDate2)
                }
            }
        }
    }

    fun getYesterdayDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = calendar.time
        return yesterday
    }

    fun getTodayDate(): Date {
        return Date()
    }

    fun getRandomDate(): Date {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val startDate = LocalDate.of(2024, 1, 1)
            val endDate = LocalDate.of(2025, 12, 31)
            val daysBetween = ChronoUnit.DAYS.between(startDate, endDate)
            val randomDaysToAdd = Random.nextLong(daysBetween + 1)
            val randomLocalDate = startDate.plusDays(randomDaysToAdd)
            return Date.from(randomLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        } else {
            throw UnsupportedOperationException("Random date generation is not supported on this Android version.")
        }
    }

    fun isDateToday(date: Date): Boolean {
        val today = getTodayDate()
        return dateFormat.format(date) == dateFormat.format(today)
    }

    fun isDateYesterday(date: Date): Boolean {
        val yesterday = getYesterdayDate()
        return dateFormat.format(date) == dateFormat.format(yesterday)
    }

    fun formatDateToDisplay(date: Date): String {
        val isToday = isDateToday(date)
        val isYesterday = isDateYesterday(date)
        val dateText = displayFormat.format(date)

        return if (isToday) "Today" else if (isYesterday) "Yesterday" else dateText
    }

    fun formatTimeToDisplay(date: Date): String {
        return timeFormat.format(date)
    }

    fun parseDisplayDate(dateStr: String): Date? {
        return try {
            displayFormat.parse(dateStr)
        } catch (e: Exception) {
            null
        }
    }

    fun groupPerformanceMetricsByDate(performanceMetrics: List<DoWorkoutPerformanceMetricsDto>): Map<String, List<DoWorkoutPerformanceMetricsDto>> {
        return performanceMetrics.groupBy { formatDateToDisplay(it.date) }
    }

    fun groupPerformanceMetricsByTime(messages: List<DoWorkoutPerformanceMetricsDto>): Map<Date, List<DoWorkoutPerformanceMetricsDto>> {
        return messages.groupBy { truncateTime(it.date) }
    }

    private fun truncateTime(date: Date): Date {
        val calendar = Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.time
    }
}
