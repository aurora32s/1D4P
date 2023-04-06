package com.core.model.datastore

import android.net.Uri
import com.core.model.data.ImageSource

data class ImageData(
    val id: Long,
    val imageUrl: Uri
)

fun ImageData.toImageSource() = ImageSource(
    id = id,
    imageUrl = imageUrl.toString()
)