package com.koleff.kare_android.data.model.dto

import com.koleff.kare_android.data.room.entity.User
import java.util.UUID

data class UserDto(
    val id: UUID? = null,
    val username: String = "",
    val password: String = "",
    val email: String = "",
) {
    fun toEntity(): User {
        return User(
            userId = id ?: UUID.randomUUID(), // Generate new UUID if null
            username = username,
            password = password,
            email = email
        )
    }
}
