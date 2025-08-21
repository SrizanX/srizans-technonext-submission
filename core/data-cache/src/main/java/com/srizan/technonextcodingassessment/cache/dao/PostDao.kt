package com.srizan.technonextcodingassessment.cache.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.srizan.technonextcodingassessment.cache.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM posts")
    fun getAll(): List<PostEntity>

    @Query("SELECT * FROM posts WHERE userId IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<PostEntity>

    @Query(
        "SELECT * FROM posts WHERE title LIKE :title AND " + "body LIKE :body LIMIT 1"
    )
    fun findByName(title: String, body: String): PostEntity

    @Insert
    suspend fun insertAll(vararg users: PostEntity): List<Long>

    @Delete
    fun delete(user: PostEntity)
}