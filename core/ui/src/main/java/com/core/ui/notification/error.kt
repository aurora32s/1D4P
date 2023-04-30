package com.core.ui.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.core.common.ext.fullDisplayName
import com.core.designsystem.components.HarooButton
import com.core.designsystem.components.HarooDialog
import com.core.designsystem.util.getString
import com.core.ui.R
import kotlinx.coroutines.delay
import java.time.LocalDate

@Composable
fun PostRequestErrorNotification(
    modifier: Modifier = Modifier,
    date: LocalDate,
    onFinishCount: () -> Unit
) {
    ErrorNotification(
        modifier = modifier,
        title = date.fullDisplayName(),
        description = getString(id = R.string.error_post_load_desc),
        count = 5,
        onFinishCount = onFinishCount
    )
}

@Composable
fun ErrorNotification(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    count: Int,
    onFinishCount: () -> Unit
) {
    val extraCount = remember { mutableStateOf(count) }
    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(1000L)
            extraCount.value -= 1
            if (extraCount.value == 0) onFinishCount()
        }
    }

    HarooDialog(
        onDismissRequest = onFinishCount
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title)
            Text(text = description)
            Text(text = getString(id = R.string.error_retry_msg))
            Spacer(modifier = Modifier.height(32.dp))
            HarooButton(onClick = onFinishCount) {
                Text(text = "${extraCount.value}초")
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = getString(id = R.string.error_btn_desc, count))
        }
    }
}