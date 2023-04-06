package com.core.domain.post

import androidx.paging.PagingData
import androidx.paging.map
import com.core.data.image.ImageRepository
import com.core.domain.model.Image
import com.core.domain.model.toImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 이미지 리스트 를 받아 오는 use case
 */
class GetImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    operator fun invoke(): Flow<PagingData<Image>> {
        return imageRepository.getImages().map { images -> images.map { it.toImage() } }
    }
}