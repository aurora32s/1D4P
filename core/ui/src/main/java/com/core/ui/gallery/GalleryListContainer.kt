package com.core.ui.gallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.core.designsystem.components.HarooButton
import com.core.designsystem.components.SelectableImage
import com.core.designsystem.theme.HarooTheme
import com.core.model.feature.ImageUiModel

@Composable
fun GalleryListContainer(
    modifier: Modifier = Modifier,
    images: LazyPagingItems<ImageUiModel>,
    selectedImages: List<ImageUiModel>,
    limit: Int = 0,
    onClickAddButton: () -> Unit = {},
    onImageSelect: (ImageUiModel) -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HarooButton(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f),
            onClick = onClickAddButton,
            shape = MaterialTheme.shapes.medium,
            backgroundColor = HarooTheme.colors.uiBackground,
            alpha = 0.1f,
            contentColor = HarooTheme.colors.text,
            content = {
                Icon(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = "Add Photo"
                )
            }
        )
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = images, key = { image -> image.id ?: image.hashCode() }) { image ->
                image?.let {
                    SelectableImage(
                        modifier = Modifier.aspectRatio(1f),
                        image = it,
                        selectedIndex = selectedImages.indexOf(it),
                        enableSelectFlag = selectedImages.size < limit,
                        onClick = onImageSelect
                    )
                }
            }
        }
    }
}