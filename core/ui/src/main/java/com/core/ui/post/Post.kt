package com.core.ui.post

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import com.core.designsystem.components.*
import com.core.designsystem.modifiers.noRippleClickable
import com.core.designsystem.theme.HarooTheme
import com.core.model.feature.ImageUiModel
import com.core.model.feature.PostUiModel
import com.core.ui.date.ColumnDayAndDate
import com.core.ui.image.AsyncImageList
import com.core.ui.image.HarooGridImages
import com.core.ui.tag.TagChip
import java.time.LocalDate

/**
 * 일별 기록의 최소 정보만 보여 주는 Component
 * @use HomeScreen
 */
@Composable
fun SimplePostItem(
    modifier: Modifier = Modifier,
    date: LocalDate, // 해당 일
    post: PostUiModel?, // 해당 일의 Post 정보
    onClickPost: (LocalDate) -> Unit, // 해당 일의 Post 화면 으로 이동
    onRemovePost: (PostUiModel) -> Unit // 해당 Post 제거
) {
    HarooSurface(
        modifier = modifier
    ) {
        if (post != null) {
            IconButton(
                modifier = Modifier
                    .padding(10.dp)
                    .size(20.dp)
                    .align(Alignment.TopEnd),
                onClick = { onRemovePost(post) }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "back"
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 30.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ColumnDayAndDate(
                date = date,
                dateTextColor = HarooTheme.colors.text.copy(alpha = 0.5f)
            )
            if (post != null) {
                AsyncImageList(
                    modifier = Modifier
                        .weight(1f)
                        .noRippleClickable { onClickPost(date) }
                        .padding(start = 20.dp, end = 8.dp),
                    images = post.images,
                    imageCount = 4,
                    space = 4.dp
                ) { image ->
                    HarooImage(imageType = image)
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
                HarooButton(
                    onClick = { onClickPost(date) }
                ) {
                    Text(text = "추가")
                }
            }
        }
    }
}

@Composable
fun BasePostItem(
    isShowContent: Boolean,
    post: PostUiModel?,
    date: LocalDate,
    contentColor: Color,
    imageContainer: @Composable (Modifier, List<ImageUiModel>) -> Unit,
    onRemovePost: (PostUiModel) -> Unit,
    onClickPost: (LocalDate) -> Unit
) {
    HarooVerticalDivider(modifier = Modifier.layoutId("Line"), dash = 0f)
    Canvas(modifier = Modifier.layoutId("Dot")) { drawCircle(contentColor) }
    ColumnDayAndDate(
        modifier = Modifier.layoutId("Day"),
        date = date,
        dayTextStyle = MaterialTheme.typography.subtitle1,
        dateTextStyle = MaterialTheme.typography.body1
    )
    if (post != null) {
        imageContainer(
            Modifier
                .layoutId("Images")
                .noRippleClickable { onClickPost(date) }, post.images
        )
        IconButton(modifier = Modifier
            .layoutId("DelBtn")
            .size(20.dp), onClick = { onRemovePost(post) }
        ) {
            Icon(imageVector = Icons.Outlined.Close, contentDescription = "back")
        }
        if (post.tags.isNotEmpty()) {
            post.tags.forEach { tag ->
                TagChip(
                    modifier = Modifier.layoutId("Tag"),
                    name = "#${tag.name}",
                    onClick = {}
                )
            }
            TagChip(
                modifier = Modifier.layoutId("Ellipse"),
                name = "...",
                onClick = {}
            )
        }
        if (isShowContent) {
            Text(
                modifier = Modifier.layoutId("Content"),
                text = post.content, style = MaterialTheme.typography.body1
            )
        }
    } else {
        HarooButton(
            modifier = Modifier.layoutId("AddBtn"),
            onClick = { onClickPost(date) }
        ) { Text(text = "추가") }
    }
}

@Composable
fun ImageContainerByType(
    modifier: Modifier = Modifier,
    postItemType: PostItemType,
    images: List<ImageUiModel>
) {
    when (postItemType) {
        PostItemType.LINEAR -> AsyncImageList(
            modifier = modifier,
            images = images,
            imageCount = 4,
            space = 8.dp,
            content = { HarooImage(imageType = it) })
        PostItemType.GRID -> HarooGridImages(
            modifier = Modifier.layoutId("Images"),
            images = images,
            content = {
                HarooImage(
                    shape = RectangleShape,
                    imageType = ImageType.AsyncImage(image = it)
                )
            }
        )
    }
}

@Composable
fun PostItemByType(
    date: LocalDate,
    modifier: Modifier = Modifier,
    isFirstItem: Boolean,
    isLastItem: Boolean,
    contentColor: Color = LocalContentColor.current,
    post: PostUiModel?, // 해당 일의 Post 정보
    postItemType: PostItemType,
    onRemovePost: (PostUiModel) -> Unit,
    onClickPost: (LocalDate) -> Unit
) {
    PostItem(
        modifier = modifier,
        postItem = PostItem.providePostItem(
            post != null, isFirstItem, isLastItem, postItemType
        ),
        content = {
            BasePostItem(
                isShowContent = postItemType.isShowContent,
                post = post,
                date = date,
                contentColor = contentColor,
                imageContainer = { modifier, images ->
                    ImageContainerByType(
                        modifier = modifier,
                        postItemType = postItemType,
                        images = images
                    )
                },
                onRemovePost = onRemovePost,
                onClickPost = onClickPost
            )
        }
    )
}

/**
 * 일별 기록 리스트 item - 일렬로 이미지 view
 * @use MonthlyScreen
 */
@Composable
fun PostItem(
    modifier: Modifier = Modifier,
    postItem: PostItem,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measureables, constraints ->
        postItem.measurePolicy(measureables, constraints, 26.dp.roundToPx())

        val lineHeight = when {
            postItem.isFirstItem -> postItem.postHeight - postItem.day.height / 2
            postItem.isLastItem -> postItem.day.height / 2
            else -> postItem.postHeight
        }
        val line = measureables.find { it.layoutId == "Line" }!!.measure(
            constraints.copy(minHeight = lineHeight, maxHeight = lineHeight)
        )
        layout(
            width = constraints.maxWidth,
            height = postItem.postHeight
        ) {
            line.placeRelative(x = 0, y = postItem.lineX)
            postItem.dot.placeRelative(x = postItem.dotX, y = postItem.dotY)
            postItem.day.placeRelative(x = postItem.dayX, y = 0)
            postItem.images?.placeRelative(x = postItem.imageX, y = postItem.imageY)
            postItem.addBtn?.placeRelative(x = postItem.addBtnX, y = postItem.addBtnY)
            postItem.delBtn?.placeRelative(x = postItem.delBtnX, y = postItem.delBtnY)

            var x = postItem.imageX
            var isOver = false
            postItem.tags.forEach {
                if (x + it.width >= postItem.tagsListEndX) {
                    isOver = true
                    return@forEach
                }
                it.placeRelative(x = x, y = postItem.tagsY)
                x += it.width + PostItem.paddingTags
            }
            if (isOver) postItem.ellipse?.placeRelative(
                x = x + PostItem.paddingTags, y = postItem.tagsY
            )
            postItem.placeExtraComponent(this)
        }
    }
}