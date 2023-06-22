package com.gerwalex.mymonma.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Shorthand for
 *
 * ```
 * remember {
 *     mutableStateOf(...)
 * }
 * ```
 */
@Composable
inline fun <T> rememberState(crossinline producer: @DisallowComposableCalls () -> T) =
    remember { mutableStateOf(producer()) }

/**
 * Shorthand for
 *
 * ```
 * remember(key) {
 *     mutableStateOf(...)
 * }
 * ```
 */
@Composable
inline fun <T> rememberState(key: Any?, crossinline producer: @DisallowComposableCalls () -> T) =
    remember(key) { mutableStateOf(producer()) }