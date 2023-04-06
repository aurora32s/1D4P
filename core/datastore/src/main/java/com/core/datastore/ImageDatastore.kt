package com.core.datastore

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.coroutineScope

class ImageDatastore(
    private val context: Context
) {
    suspend fun getImages(limit: Int, offset: Int): List<Uri> = coroutineScope {
        try {
            val uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.Images.ImageColumns._ID)
            val sortOrder =
                "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC LIMIT $limit OFFSET $offset"
            val query =
                context.contentResolver.query(uriExternal, projection, null, null, sortOrder)

            val galleryImage = mutableListOf<Uri>()
            query?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val contentUri = ContentUris.withAppendedId(uriExternal, id)
                    galleryImage.add(contentUri)
                }
            }
            galleryImage.toList()
        } catch (exception: Exception) {
            emptyList()
        }
    }
}