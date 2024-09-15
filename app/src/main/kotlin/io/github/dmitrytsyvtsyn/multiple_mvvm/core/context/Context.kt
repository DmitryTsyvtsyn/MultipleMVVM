package io.github.dmitrytsyvtsyn.multiple_mvvm.core.context

import android.content.Context
import kotlin.math.roundToInt

fun Context.dp(value: Int): Int {
    return (resources.displayMetrics.density * value).roundToInt()
}

fun Context.dp(value: Float): Float {
    return resources.displayMetrics.density * value
}