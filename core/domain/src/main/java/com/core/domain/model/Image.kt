package com.core.domain.model

import android.net.Uri
import com.core.data.model.ImageSource

data class Image(
    val id: Long?,
    val imageUrl: Uri
)

internal fun ImageSource.toImage() = Image(
    id = id,
    imageUrl = imageUrl
)