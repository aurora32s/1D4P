package com.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.core.designsystem.R
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.designsystem.theme.HarooTheme

/**
 * 하루 이미지 4개를 가로 리스트 로 보여 주는 Component
 */
@Composable
fun HarooImageList() {

}

/**
 * 기본 Image Component
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HarooImage(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    shape: Shape = MaterialTheme.shapes.medium,
    elevation: Dp = 2.dp,
    imageType: Image
) {
    HarooSurface(
        modifier = modifier.clickable(
            role = Role.Button,
            onClick = onClick
        ),
        shape = shape,
        elevation = elevation
    ) {
        when (imageType) {
            is Image.BitmapImage -> Image(
                bitmap = imageType.bitmap,
                contentDescription = imageType.contentDescription,
                contentScale = ContentScale.Crop
            )
            is Image.ResourceImage -> Image(
                painter = painterResource(id = imageType.resource),
                contentDescription = imageType.contentDescription,
                contentScale = ContentScale.Crop
            )
        }
    }
}

sealed interface Image {
    data class BitmapImage(
        val bitmap: ImageBitmap,
        val contentDescription: String? = null
    ) : Image

    data class ResourceImage(
        @DrawableRes val resource: Int,
        val contentDescription: String? = null
    ) : Image
}

@Preview
@Composable
fun ImagePreview() {
    AllForMemoryTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(HarooTheme.colors.interactiveBackground)
                )
        ) {
            HarooImage(
                modifier = Modifier
                    .padding(12.dp)
                    .size(120.dp),
                imageType = Image.ResourceImage(R.drawable.test)
            )
        }
    }
}

