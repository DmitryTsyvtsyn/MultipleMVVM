package io.github.dmitrytsyvtsyn.multiple_mvvm.core.context

import android.app.Activity
import android.app.AlertDialog
import android.graphics.drawable.GradientDrawable
import android.view.View
import io.github.dmitrytsyvtsyn.multiple_mvvm.R
import io.github.dmitrytsyvtsyn.multiple_mvvm.core.theme.LightGray

fun Activity.showColorDialog(color: Int) {
    val dialogBuilder = AlertDialog.Builder(this)
    dialogBuilder.setTitle(R.string.selected_color)
    dialogBuilder.setNeutralButton(R.string.ok) { dialog, _ -> dialog.dismiss() }

    val colorView = View(this)
    colorView.background = GradientDrawable().apply {
        cornerRadius = dp(24f)
        setColor(color)
    }

    val dialog = dialogBuilder.create()
    dialog.setView(colorView, dp(16), dp(16), dp(16), dp(16))
    dialog.show()

    dialog.window?.setBackgroundDrawable(GradientDrawable().apply {
        setColor(LightGray)
        cornerRadius = dp(16f)
    })
    dialog.window?.setLayout(dp(300), dp(400))
}