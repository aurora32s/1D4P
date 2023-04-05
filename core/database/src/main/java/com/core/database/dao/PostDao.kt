package com.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.core.model.database.ImageEntity
import com.core.model.database.PostEntity
import com.core.model.database.TagEntity

/**
 * 일별 기록과 관련된 Dao
 */
@Dao
interface PostDao {
    // post 추가
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity): Int

    // image 추가
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImageEntity>): Int

    // 특정 년도/월/일의 post 요청
    @Query("SELECT * FROM post WHERE year = :year AND month = :month AND day = :day")
    suspend fun selectPostByDate(year: Int, month: Int, day: Int): List<PostEntity>

    // 특정 년도/월의 post 요청
    @Query("SELECT * FROM post WHERE year = :year AND month = :month")
    suspend fun selectPostByMonth(year: Int, month: Int): List<PostEntity>

    // 특정 post 의 image 요청
    @Query("SELECT * FROM image WHERE post_id = :postId")
    suspend fun selectImagesByPost(postId: Int): List<ImageEntity>

    // 특정 post 의 tag 요청
    @Query("SELECT * FROM tag WHERE post_id = :postId")
    suspend fun selectTagsByPost(postId: Int): List<TagEntity>
}