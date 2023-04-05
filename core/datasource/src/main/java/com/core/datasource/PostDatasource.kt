package com.core.datasource

import androidx.paging.PagingData
import com.core.datasource.model.Post
import kotlinx.coroutines.flow.Flow

/**
 * 게시글(post) 정보를 받아 오는 datasource
 */
interface PostDatasource {
    // post 추가
    suspend fun addPost(post: Post): Int

    // 특정 년도/월/일 post 요청
    suspend fun getPost(year: Int, month: Int, day: Int): Post?

    // 특정 년도/월 post 요창
    suspend fun getPost(year: Int, month: Int): List<Post>

    // 특정 년도/월 post paging 요청
    fun getPostPaging(year: Int, month: Int): Flow<PagingData<Post>>
}