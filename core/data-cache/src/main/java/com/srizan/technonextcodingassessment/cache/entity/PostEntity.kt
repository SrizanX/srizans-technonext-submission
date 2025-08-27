package com.srizan.technonextcodingassessment.cache.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "posts",
    indices = [
        Index(value = ["userId"]),
        Index(value = ["isFavourite"]),
        Index(value = ["title"]) // For search functionality
    ]
)
data class PostEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val isFavourite: Boolean = false
)
