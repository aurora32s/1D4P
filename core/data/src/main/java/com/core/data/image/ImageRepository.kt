package com.core.data.image

import androidx.paging.PagingData
import com.core.model.data.ImageSource
import kotlinx.coroutines.flow.Flow

/**
 * 이미지 리스트 요청 repository
 */
interface ImageRepository {
    // 이미지 paging 요청
    fun getImages(): Flow<PagingData<ImageSource>>
}