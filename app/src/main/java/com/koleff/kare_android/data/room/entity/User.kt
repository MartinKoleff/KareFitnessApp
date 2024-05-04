package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.koleff.kare_android.data.KareDto
import com.koleff.kare_android.data.model.dto.UserDto
import java.util.UUID

@Entity(
    tableName = "users_table"
)
data class User(
    @PrimaryKey(autoGenerate = false)
    val userId: UUID,
    val username: String,
    val password: String,
    val email: String,
): KareDto<UserDto> {
    override fun toDto(): UserDto {
        return UserDto(
            id = userId,
            username = username,
            password = password,
            email = email
        )
    }
}
