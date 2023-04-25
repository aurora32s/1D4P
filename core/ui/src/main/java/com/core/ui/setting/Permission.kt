package com.core.ui.setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.core.designsystem.components.HarooButton
import com.core.designsystem.components.HarooDashLine
import com.core.designsystem.components.HarooSurface
import com.core.designsystem.theme.AllForMemoryTheme
import com.core.designsystem.theme.HarooTheme
import com.core.designsystem.util.getString
import com.core.ui.R
import com.core.ui.manager.Permission
import com.core.ui.manager.externalStoragePermission
import com.core.designsystem.R as dR

@Composable
fun PermissionRational(
    modifier: Modifier = Modifier,
    permissions: List<Permission>,
    onClickGrant: () -> Unit,
    onClickDeny: () -> Unit
) {
    Dialog(
        onDismissRequest = onClickDeny,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        (LocalView.current.parent as? DialogWindowProvider)?.window?.setDimAmount(0.7f)
        Column(
            modifier = modifier.padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            CompositionLocalProvider(
                LocalContentColor provides HarooTheme.colors.text
            ) {
                Text(
                    text = getString(id = dR.string.app_name),
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = getString(id = R.string.permission_desc),
                    style = MaterialTheme.typography.body1
                )
                HarooDashLine(modifier = Modifier.padding(vertical = 12.dp))
                permissions.forEach { PermissionItem(permission = it) }
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    HarooButton(onClick = onClickDeny) {
                        Text(
                            text = getString(id = R.string.btn_deny),
                            style = MaterialTheme.typography.h6
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    HarooButton(onClick = onClickGrant) {
                        Text(
                            text = getString(id = R.string.btn_grant),
                            style = MaterialTheme.typography.h6
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = getString(id = R.string.btn_grant_desc),
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun PermissionItem(
    modifier: Modifier = Modifier,
    permission: Permission
) {
    HarooSurface(
        modifier = modifier,
        color = HarooTheme.colors.dim,
        alpha = 1f,
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.linearGradient(HarooTheme.colors.interactiveBackground)
        ),
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = permission.icon,
                contentDescription = ""
            )
            Column {
                Text(
                    text = getString(id = permission.title),
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = getString(id = permission.description),
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
@Preview(name = "Permission Rational Item preview")
fun PermissionRationalItemPreview() {
    AllForMemoryTheme {
        PermissionItem(permission = previewPermissions.first())
    }
}

@Composable
@Preview(name = "Permission Rational preview")
fun PermissionRationalPreview() {
    AllForMemoryTheme {
        PermissionRational(
            permissions = previewPermissions,
            onClickGrant = {},
            onClickDeny = {}
        )
    }
}

private val previewPermissions = listOf(
    Permission(
        permission = externalStoragePermission,
        title = R.string.external_storage_title,
        description = R.string.external_storage_desc,
        icon = Icons.Default.Image
    )
)