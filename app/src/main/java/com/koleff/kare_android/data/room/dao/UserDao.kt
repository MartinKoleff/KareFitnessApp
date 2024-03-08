package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.koleff.kare_android.data.room.entity.User
import java.util.UUID

@Dao
interface UserDao {

    @Query("SELECT * FROM users_table WHERE userId = :userId")
    fun getUserById(userId: UUID): User?

    @Query("SELECT * FROM users_table WHERE email = :email")
    fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users_table WHERE username = :username")
    fun getUserByUsername(username: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}