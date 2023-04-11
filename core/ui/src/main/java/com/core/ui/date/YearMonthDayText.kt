package com.core.ui.date

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.designsystem.theme.HarooTheme
import java.time.LocalDate

/**
 * 연도, 월, 일이 포함된 Component
 */
@Composable
fun YearMonthDayText(
    modifier: Modifier = Modifier,
    date: LocalDate,
    contentColor: Color = HarooTheme.colors.text
) {
    ConstraintLayout(modifier = modifier) {
        val (yearMonth, day, dayOfWeek) = createRefs()
        createVerticalChain(yearMonth, day, chainStyle = ChainStyle.Packed)

        CompositionLocalProvider(
            LocalContentColor provides contentColor
        ) {
            Text(
                text = "2023.01",
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .constrainAs(yearMonth) {
                        linkTo(
                            top = parent.top,
                            bottom = day.top
                        )
                    }
            )
            Text(
                text = "01",
                lineHeight = 0.sp,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .constrainAs(day) {
                        linkTo(
                            top = yearMonth.bottom,
                            bottom = parent.bottom
                        )
                    }
            )
            Text(
                text = "SUNDAY",
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .constrainAs(dayOfWeek) {
                        bottom.linkTo(day.bottom)
                        start.linkTo(day.end)
                    }
            )
        }
    }
}

@Composable
@Preview(name = "year, month, day preview")
fun YearMonthDayTextPreview() {
    AllForMemoryTheme {
        YearMonthDayText(
            modifier = Modifier.fillMaxWidth(),
            date = LocalDate.now()
        )
    }
}