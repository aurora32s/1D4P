package com.core.datastore

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import com.core.datastore.model.ImageData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ImageDatastore @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    suspend fun getImages(limit: Int, offset: Int): List<ImageData> = withContext(Dispatchers.IO) {
        try {
            val uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATE_TAKEN
            )
            val resolver = context.contentResolver
            val query = resolver.createCursor(uriExternal, projection, limit, offset)
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

    private fun ContentResolver.createCursor(
        uriExternal: Uri,
        projection: Array<String>,
        limit: Int,
        offset: Int
    ): Cursor? {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            val bundle = Bundle().apply {
                putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
                putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
                putString(
                    ContentResolver.QUERY_ARG_SORT_COLUMNS,
                    MediaStore.Images.ImageColumns.DATE_TAKEN
                )
                putInt(
                    ContentResolver.QUERY_ARG_SORT_DIRECTION,
                    ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                )
            }
            query(uriExternal, projection, bundle, null)
        } else {
            val sortOrder = "" +
                    "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC " +
                    "LIMIT $limit OFFSET $offset"
            query(uriExternal, projection, null, null, sortOrder)
        }
    }
}