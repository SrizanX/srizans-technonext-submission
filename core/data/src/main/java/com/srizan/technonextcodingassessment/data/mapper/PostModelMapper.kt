package com.srizan.technonextcodingassessment.data.mapper

import com.srizan.technonextcodingassessment.cache.entity.PostEntity
import com.srizan.technonextcodingassessment.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Flow<List<PostEntity>>.toDomainModel() = this.map { it.toDomainModel() }

fun List<PostEntity>.toDomainModel() = this.map { it.toDomainModel() }

fun PostEntity.toDomainModel() = Post(
    id = this.id,
    userId = this.userId,
    title = this.title,
    body = this.body,
    isFavourite = this.isFavourite
)
