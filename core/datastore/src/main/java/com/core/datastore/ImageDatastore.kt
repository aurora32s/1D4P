package com.core.datastore

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.core.datastore.model.ImageData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class ImageDatastore @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    suspend fun getImages(limit: Int, offset: Int): List<ImageData> = coroutineScope {
        try {
            val uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.Images.ImageColumns._ID)
            val sortOrder =
                "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC LIMIT $limit OFFSET $offset"
            val query =
                context.contentResolver.query(uriExternal, projection, null, null, sortOrder)

            val galleryImage = mutableListOf<ImageData>()
            query?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val contentUri = ContentUris.withAppendedId(uriExternal, id)
                    galleryImage.add(ImageData(id, contentUri))
                }
            }
            galleryImage.toList()
        } catch (exception: Exception) {
            emptyList()
        }
    }
}