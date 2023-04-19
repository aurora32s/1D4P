package com.core.data.post

import androidx.paging.PagingData
import com.core.model.data.ImageSource
import com.core.model.data.PostSource
import com.core.model.data.PostSources
import com.core.model.data.TagSource
import kotlinx.coroutines.flow.Flow

/**
 * 일별 기록과 관련된 Repository
 */
interface PostRepository {
    // post 추가
    suspend fun addPost(
        post: PostSource,
        removeImages: List<ImageSource>,
        removeTags: List<TagSource>
    ): Long

    // 특정 년도/월/일 post 요청
    suspend fun getPost(year: Int, month: Int, day: Int): PostSource?

    // 특정 년도/월 post 요창
    suspend fun getPosts(year: Int, month: Int): List<PostSource>

    // 특정 년도/월 post paging 요청
    fun getPostPaging(): Flow<PagingData<PostSources>>

    // 특정 날짜의 게시글 제거
    suspend fun removePost(postId: Long)
}