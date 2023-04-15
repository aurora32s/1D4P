package com.feature.monthly

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.core.designsystem.components.HarooVerticalDivider
import com.core.designsystem.theme.HarooTheme
import com.core.model.feature.PostUiModel
import com.core.ui.date.RowMonthAndName
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MonthlyScreen() {
    val today = YearMonth.now()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .background(Brush.linearGradient(HarooTheme.colors.interactiveBackground)),
    ) {
        MonthlyHeader(date = today, posts = emptyMap())
    }
}

@Composable
fun MonthlyHeader(
    date: YearMonth,
    modifier: Modifier = Modifier,
    verticalSpace: Dp = 4.dp,
    horizontalSpace: Dp = 4.dp,
    posts: Map<LocalDate, PostUiModel>
) {
    Column(
        modifier = modifier
    ) {
        CompositionLocalProvider(
            LocalContentColor provides HarooTheme.colors.text
        ) {
            RowMonthAndName(
                modifier = Modifier.padding(16.dp),
                date = date
            )
//        Calendar(
//            space = verticalSpace,
//            currentMonth = date,
//            dayContent = {
//                DateWithImage(
//                    modifier = Modifier.padding(horizontal = horizontalSpace),
//                    state = it,
//                    image = posts[it.date]?.images?.firstOrNull()
//                )
//            }
//        )
            Row(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                MonthlyCountContainer(name = "전체일수", count = date.lengthOfMonth())
                HarooVerticalDivider(modifier = Modifier.padding(horizontal = 30.dp))
                MonthlyCountContainer(name = "기록일수", count = posts.size)
                HarooVerticalDivider(modifier = Modifier.padding(horizontal = 30.dp))
                MonthlyCountContainer(name = "이미지", count = posts.values.sumOf { it.images.size })
            }
        }
    }
}

@Composable
fun MonthlyCountContainer(
    name: String, // info name
    count: Int // info 개수
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = name, style = MaterialTheme.typography.body1)
        Text(text = count.toString(), style = MaterialTheme.typography.body1)
    }
}