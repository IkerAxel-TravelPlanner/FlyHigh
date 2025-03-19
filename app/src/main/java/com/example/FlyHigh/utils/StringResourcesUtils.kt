package com.example.LowTravel.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

@Composable
@ReadOnlyComposable
fun stringResource(@StringRes id: Int): String {
    val context = LocalContext.current
    return context.getString(id)
}

@Composable
@ReadOnlyComposable
fun stringResource(@StringRes id: Int, vararg formatArgs: Any): String {
    val context = LocalContext.current
    return context.getString(id, *formatArgs)
}