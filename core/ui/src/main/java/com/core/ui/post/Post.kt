package com.core.ui.post

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.core.designsystem.components.HarooButton
import com.core.designsystem.components.HarooImage
import com.core.designsystem.components.HarooSurface
import com.core.designsystem.theme.HarooTheme
import com.core.model.feature.PostUiModel
import com.core.ui.date.ColumnDayAndDate
import com.core.ui.image.AsyncImageList
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
                onClick = { }
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
fun LinearPostItem() {
}

/**
 * 일별 기록 리스트 item - grid 형태로 이미지 view
 * @use MonthlyScreen
 */
@Composable
fun GridPostItem() {
}