package com.core.datasource

import androidx.paging.PagingData
import com.core.datasource.model.Image
import kotlinx.coroutines.flow.Flow

/**
 * 이미지 리스트 를 요청 하기 위한 data source
 */
interface ImageDatasource {
    // 이미지 paging 요청
    fun getImages(): Flow<PagingData<Image>>
}