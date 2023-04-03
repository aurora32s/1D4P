package com.core.data.post

/**
 * 일별 기록과 관련된 Repository
 */
interface PostRepository {
    // 일별 기록 요청
    suspend fun getPostByDate(year: Int, month: Int, day: Int)

    // 월별 기록 요청
    suspend fun getPostByMonth(year: Int, month: Int)

    // 월별 기록 paging 요청
    fun getPostPageByMonth(year: Int, month: Int)
}