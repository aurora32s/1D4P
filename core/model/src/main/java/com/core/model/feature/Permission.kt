package com.core.model.feature

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 앱에 필요한 권한 정보
 */
data class Permission(
    val permission: String, // Permission Manifest
    @StringRes val title: Int, // String name resource id
    @StringRes val description: Int, // Permission description resource id
    val icon: ImageVector // permission icon
)
