/*
 * MIT License
 *
 * Copyright (c) 2019 Perol_Notsfsssf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package com.perol.asdpl.pixivez.repository

import androidx.preference.PreferenceManager
import com.perol.asdpl.pixivez.services.PxEZApp
import com.perol.asdpl.pixivez.sql.AppDatabase
import com.perol.asdpl.pixivez.sql.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppDataRepository {
    companion object {
        private val appDatabase = AppDatabase.getInstance(PxEZApp.instance)
        val pre = PreferenceManager.getDefaultSharedPreferences(PxEZApp.instance)
        suspend fun getUser(): UserEntity {
            val result = withContext(Dispatchers.IO) {
                appDatabase.userDao().getUsers()
            }
            val num = pre.getInt("usernum", 0)
            return if (result.size <= num)
                result[0]
            else {
                result[num]
            }
        }

        suspend fun getAllUser(): List<UserEntity> {
            return withContext(Dispatchers.IO) {
                appDatabase.userDao().getUsers()
            }
        }

        suspend fun deleteAllUser() {
            withContext(Dispatchers.IO) {
                appDatabase.userDao().deleteUsers()
            }

        }

        suspend fun updateUser(query: UserEntity) {
            return withContext(Dispatchers.IO) {
                appDatabase.userDao().updateUser(query)
            }
        }

        suspend fun insertUser(query: UserEntity) {
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