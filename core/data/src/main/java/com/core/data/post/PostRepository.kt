package com.core.data.post

import androidx.paging.PagingData
import com.core.data.model.Post
import kotlinx.coroutines.flow.Flow

/**
 * 일별 기록과 관련된 Repository
 */
interface PostRepository {
    // post 추가
    suspend fun addPost(post: Post): Long

    // 특정 년도/월/일 post 요청
    suspend fun getPost(year: Int, month: Int, day: Int): Post?

    // 특정 년도/월 post 요창
    suspend fun getPosts(year: Int, month: Int): List<Post>

    // 특정 년도/월 post paging 요청
    fun getPostPaging(year: Int, month: Int): Flow<PagingData<Post>>
}