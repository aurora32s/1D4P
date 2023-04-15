package com.core.designsystem.util

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun getString(@StringRes id: Int) = stringResource(id = id)

@Composable
fun getString(@StringRes id: Int, vararg values: Any) = stringResource(id = id, formatArgs = values)