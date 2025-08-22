package com.srizan.technonextcodingassessment.cache.dao

//import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.srizan.technonextcodingassessment.cache.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id ASC")
    fun getAll(): Flow<List<PostEntity>>


    @Query("SELECT * FROM posts WHERE isFavourite=1 ORDER BY id ASC")
    fun getAllFavourites(): Flow<List<PostEntity>>

//    @Query("SELECT * FROM posts ORDER BY id ASC")
//    fun pagingSource(): PagingSource<Int, PostEntity>
//
//    @Query("SELECT * FROM posts WHERE title LIKE '%' || :query || '%' OR body LIKE '%' || :query || '%' ORDER BY id ASC")
//    fun searchPosts(query: String): PagingSource<Int, PostEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(posts: List<PostEntity>)

    // mark post as favourite
    @Query("UPDATE posts SET isFavourite = 1 WHERE id = :postId")
    suspend fun markPostAsFavourite(postId: Int)

    // unmark post as favourite
    @Query("UPDATE posts SET isFavourite = 0 WHERE id = :postId")
    suspend fun unmarkPostAsFavourite(postId: Int)

    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun deletePost(postId: Int)

    @Query("DELETE FROM posts")
    suspend fun deleteAll()
}