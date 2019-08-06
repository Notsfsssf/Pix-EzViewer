package com.perol.asdpl.pixivez.repository

import androidx.room.Update
import com.perol.asdpl.pixivez.networks.SharedPreferencesServices
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.sql.AppDatabase
import com.perol.asdpl.pixivez.sql.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

class AppDataRepository {
    companion object {
        val appDatabase = AppDatabase.getInstance(PxEZApp.instance)
        val pre = SharedPreferencesServices.getInstance()
        suspend fun getUser(): UserEntity {
            val result = withContext(Dispatchers.IO) {
                appDatabase.userDao().getUsers()
            }
            val num = pre.getInt("usernum")
            if (result.size > num)
                return result[num]
            else if (result.isNotEmpty()) {
                pre.setInt("usernum", 0)
                return result[0]
            } else {

                val refresh = pre.getString("Refresh_token")
                val device = pre.getString("Device_token")
                return UserEntity("olduser", 0, "", "", false, device, refresh, "")
            }
        }

        suspend fun getAllUser(): List<UserEntity> {
            val result = withContext(Dispatchers.IO) {
                appDatabase.userDao().getUsers()
            }
            return result
        }

        suspend fun deleteAllUser() {
            withContext(Dispatchers.IO) {
                appDatabase.userDao().deleteUsers()
            }

        }

        suspend fun UpdateUser(query: UserEntity) {
            return withContext(Dispatchers.IO) {
                appDatabase.userDao().updateUser(query)
            }
        }

        suspend fun InsertUser(query: UserEntity) {
            return withContext(Dispatchers.IO) {
                appDatabase.userDao().insert(query)
            }
        }

        suspend fun deleteUser(id: UserEntity) {
            return withContext(Dispatchers.IO) {
                appDatabase.userDao().deleteUser(id)
            }
        }

        suspend fun findUser(id: Long): List<UserEntity> {
            return withContext(Dispatchers.IO) {
                appDatabase.userDao().findUsers(id)
            }
        }
    }


}