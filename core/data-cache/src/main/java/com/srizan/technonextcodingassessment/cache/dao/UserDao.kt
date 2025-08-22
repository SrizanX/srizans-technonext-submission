package com.srizan.technonextcodingassessment.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.srizan.technonextcodingassessment.cache.entity.UserEntity


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("DELETE FROM users WHERE email = :email")
    suspend fun deleteUserByEmail(email: String): Int

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun doesUserExist(email: String): Boolean
}
