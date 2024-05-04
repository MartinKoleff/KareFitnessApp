package com.koleff.kare_android.data.room

import androidx.room.TypeConverter
import com.koleff.kare_android.data.model.dto.ExerciseTime
import java.util.Date
import java.util.UUID

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? = uuid?.toString()

    @TypeConverter
    fun toUUID(uuid: String?): UUID? = uuid?.let { UUID.fromString(it) }
}

class ExerciseTimeConverters {
    @TypeConverter
    fun fromExerciseTime(exerciseTime: ExerciseTime?): String? {
        return exerciseTime?.toString()
    }

    @TypeConverter
    fun toExerciseTime(timeString: String?): ExerciseTime? {
        if (timeString == null) return null
        val parts = timeString.split(':')
        if (parts.size == 3) {
            val hours = parts[0].toInt()
            val minutes = parts[1].toInt()
            val seconds = parts[2].toInt()
            return ExerciseTime(hours, minutes, seconds)
        }
        return null
    }
}