package com.koleff.kare_android.data.model.dto

import java.util.UUID

data class UserDto(
    val id: UUID = UUID.fromString(""),
    val username: String = "",
    val password: String = "",
    val email: String = "",
)
