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
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.core.designsystem.components.*
import com.core.designsystem.modifiers.noRippleClickable
import com.core.designsystem.theme.HarooTheme
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

/**
 * 일별 기록 리스트 item - 일렬로 이미지 view
 * @use MonthlyScreen
 */
@Composable
fun LinearPostItem(
    date: LocalDate,
    modifier: Modifier = Modifier,
    contentColor: Color = LocalContentColor.current,
    post: PostUiModel?, // 해당 일의 Post 정보
) {
    Layout(
        modifier = modifier,
        content = {
            HarooVerticalDivider(
                modifier = Modifier.layoutId("Line"),
                dash = 0f
            )
            Canvas(modifier = Modifier.layoutId("Dot")) {
                drawCircle(contentColor)
            }
            ColumnDayAndDate(
                modifier = Modifier.layoutId("Day"),
                date = date,
                dayTextStyle = MaterialTheme.typography.subtitle1,
                dateTextStyle = MaterialTheme.typography.body1
            )
            if (post != null) {
                AsyncImageList(
                    modifier = Modifier.layoutId("Images"),
                    images = post.images,
                    imageCount = 4,
                    space = 8.dp,
                    content = { HarooImage(imageType = it) }
                )
                IconButton(
                    modifier = Modifier
                        .layoutId("DelBtn")
                        .size(20.dp),
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "back"
                    )
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
            } else {
                HarooButton(
                    modifier = Modifier.layoutId("AddBtn"),
                    onClick = { }
                ) {
                    Text(text = "추가")
                }
            }
        },
    ) { measureables, constraints ->
        val dotSize = 15
        val paddingLineAndDay = 20
        val paddingDayAndImages = 23
        val paddingImagesAndDelBtn = 23
        val paddingImagesAndTags = 12
        val paddingTags = 4

        val day = measureables.find { it.layoutId == "Day" }!!.measure(constraints)

        val delBtn = measureables.find { it.layoutId == "DelBtn" }?.measure(constraints)
        val addBtn = measureables.find { it.layoutId == "AddBtn" }?.measure(constraints)

        val imageWidth = constraints.maxWidth - (paddingLineAndDay + day.width +
                paddingDayAndImages + paddingImagesAndDelBtn + (delBtn?.width ?: 0))
        val images = measureables.find { it.layoutId == "Images" }?.measure(
            constraints.copy(maxWidth = imageWidth)
        )
        val tags = measureables.filter { it.layoutId == "Tag" }.map {
            it.measure(constraints)
        }

        val tagHeight = tags.firstOrNull()?.height ?: 0
        val height = (
                if (post == null) day.height
                else images!!.height + tagHeight + paddingImagesAndTags) + 26.dp.roundToPx()
        val line = measureables.find { it.layoutId == "Line" }!!.measure(
            Constraints(minHeight = height, maxHeight = height)
        )
        val dot = measureables.find { it.layoutId == "Dot" }!!.measure(
            Constraints(
                minWidth = dotSize, maxWidth = dotSize,
                minHeight = dotSize, maxHeight = dotSize
            )
        )
        val ellipse = measureables.find { it.layoutId == "Ellipse" }?.measure(constraints)

        val dayX = paddingLineAndDay
        val imagesX = dayX + day.width + paddingDayAndImages
        val tagsListEndX = imagesX + (images?.width ?: 0)
        val tagsListY = (images?.height ?: 0) + paddingImagesAndTags
        layout(
            constraints.maxWidth,
            height
        ) {
            line.placeRelative(x = 0, y = 0)
            dot.placeRelative(
                x = -dot.width / 2,
                y = (day.height / 2) - dot.height / 2
            )
            day.placeRelative(x = dayX, y = 0)
            images?.placeRelative(x = imagesX, y = 0)
            delBtn?.placeRelative(
                x = constraints.maxWidth - delBtn.width,
                y = (images?.height ?: 0) / 2 - delBtn.height / 2
            )
            addBtn?.placeRelative(
                x = constraints.maxWidth - addBtn.width,
                y = (day.height / 2) - addBtn.height / 2
            )
            var x = imagesX
            var isOver = false
            tags.forEach {
                if (x + it.width >= tagsListEndX) {
                    isOver = true
                    return@forEach
                }
                it.placeRelative(x = x, y = tagsListY)
                x += it.width + paddingTags
            }
            if (isOver) ellipse?.placeRelative(x = x + paddingTags, y = tagsListY)
        }
    }
}

/**
 * 일별 기록 리스트 item - grid 형태로 이미지 view
 * @use MonthlyScreen
 */
@Composable
fun GridPostItem(
    date: LocalDate,
    modifier: Modifier = Modifier,
    contentColor: Color = LocalContentColor.current,
    post: PostUiModel?, // 해당 일의 Post 정보
) {
    Layout(
        modifier = modifier,
        content = {
            HarooVerticalDivider(
                modifier = Modifier.layoutId("Line"),
                dash = 0f
            )
            Canvas(modifier = Modifier.layoutId("Dot")) {
                drawCircle(contentColor)
            }
            ColumnDayAndDate(
                modifier = Modifier.layoutId("Day"),
                date = date,
                dayTextStyle = MaterialTheme.typography.subtitle1,
                dateTextStyle = MaterialTheme.typography.body1
            )
            if (post != null) {
                HarooGridImages(
                    modifier = Modifier.layoutId("Images"),
                    images = post.images,
                    content = {
                        HarooImage(
                            shape = RectangleShape,
                            imageType = ImageType.AsyncImage(image = it)
                        )
                    }
                )
                IconButton(
                    modifier = Modifier
                        .layoutId("DelBtn")
                        .size(20.dp),
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "back"
                    )
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
            } else {
                HarooButton(
                    modifier = Modifier.layoutId("AddBtn"),
                    onClick = { }
                ) {
                    Text(text = "추가")
                }
            }
        },
    ) { measureables, constraints ->
        val dotSize = 15
        val paddingLineAndDay = 20
        val paddingDayAndImages = 23
        val paddingImagesAndTags = 12
        val paddingTags = 4

        val day = measureables.find { it.layoutId == "Day" }!!.measure(constraints)
        val delBtn = measureables.find { it.layoutId == "DelBtn" }?.measure(constraints)
        val addBtn = measureables.find { it.layoutId == "AddBtn" }?.measure(constraints)
        val tags = measureables.filter { it.layoutId == "Tag" }.map { it.measure(constraints) }

        val imageWidth = constraints.maxWidth - (paddingLineAndDay + day.width + paddingDayAndImages)
        val images = measureables.find { it.layoutId == "Images" }?.measure(
            constraints.copy(maxWidth = imageWidth)
        )

        val tagHeight = tags.firstOrNull()?.height ?: 0
        var height = day.height + 26.dp.roundToPx()
        if (post != null) height += (images?.height ?: 0) + tagHeight + paddingImagesAndTags
        val line = measureables.find { it.layoutId == "Line" }!!.measure(
            Constraints(minHeight = height, maxHeight = height)
        )
        val dot = measureables.find { it.layoutId == "Dot" }!!.measure(
            Constraints(
                minWidth = dotSize, maxWidth = dotSize,
                minHeight = dotSize, maxHeight = dotSize
            )
        )
        val ellipse = measureables.find { it.layoutId == "Ellipse" }?.measure(constraints)

        val dayX = paddingLineAndDay
        val imagesX = dayX + day.width + paddingDayAndImages
        val imagesY = day.height
        val tagsListEndX = imagesX + (images?.width ?: 0)
        val tagsListY = imagesY + (images?.height ?: 0) + paddingImagesAndTags
        layout(
            constraints.maxWidth,
            height
        ) {
            line.placeRelative(x = 0, y = 0)
            dot.placeRelative(
                x = -dot.width / 2,
                y = (day.height / 2) - dot.height / 2
            )
            day.placeRelative(x = dayX, y = 0)
            images?.placeRelative(
                x = imagesX, y = imagesY
            )
            delBtn?.placeRelative(
                x = constraints.maxWidth - delBtn.width,
                y = day.height / 2 - delBtn.height / 2
            )
            addBtn?.placeRelative(
                x = constraints.maxWidth - addBtn.width,
                y = (day.height / 2) - addBtn.height / 2
            )
            var x = imagesX
            var isOver = false
            tags.forEach {
                if (x + it.width >= tagsListEndX) {
                    isOver = true
                    return@forEach
                }
                it.placeRelative(x = x, y = tagsListY)
                x += it.width + paddingTags
            }
            if (isOver) ellipse?.placeRelative(x = x + paddingTags, y = tagsListY)
        }
    }
}