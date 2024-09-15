package io.github.dmitrytsyvtsyn.multiple_mvvm.core.theme

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.widget.Button
import io.github.dmitrytsyvtsyn.multiple_mvvm.core.context.dp
import io.github.dmitrytsyvtsyn.multiple_mvvm.core.view.fontSize

class CoreButton(context: Context) : Button(context) {

    init {
        background = RippleDrawable(
            ColorStateList.valueOf(Purple40),
            GradientDrawable().apply {
                cornerRadius = context.dp(16f)
                setColor(Purple80)
            },
            null
        )
        setTextColor(Black)
        fontSize(16f)
    }

}