package com.koleff.kare_android.authentication.data

import com.koleff.kare_android.data.room.dao.UserDao
import com.koleff.kare_android.data.room.entity.User
import com.koleff.kare_android.utils.FakeDao
import java.util.UUID

class UserDaoFake() : UserDao, FakeDao {
    private var usersDB = mutableListOf<User>()

    companion object {
        private const val TAG = "UserDaoFake"
    }

    override fun getUserById(userId: UUID): User =
        usersDB.first { it.userId == userId }


    override fun getUserByEmail(email: String): User =
        usersDB.first { it.email == email }


    override fun getUserByUsername(username: String): User =
        usersDB.first { it.username == username }


    override suspend fun saveUser(user: User) {
        usersDB.add(user)
    }

    override suspend fun updateUser(user: User) {

        //Find user entry
        val index = usersDB.indexOfFirst { it.userId == user.userId }

        //User found
        if (index != -1) {

            //Replace DB entry with new one
            usersDB[index] = user
        } else {

            //Delete invalid user?
        }
    }

    override suspend fun deleteUser(user: User) {
        usersDB.removeAll { it.userId == user.userId }
    }

    override fun clearDB() {
        usersDB.clear()
    }
}