package io.github.dmitrytsyvtsyn.multiple_mvvm.core.view

import android.util.TypedValue
import android.widget.TextView

fun TextView.fontSize(size: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
}