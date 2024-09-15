package io.github.dmitrytsyvtsyn.multiple_mvvm.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import io.github.dmitrytsyvtsyn.multiple_mvvm.R
import io.github.dmitrytsyvtsyn.multiple_mvvm.core.context.dp
import io.github.dmitrytsyvtsyn.multiple_mvvm.core.coroutines.launchWith
import io.github.dmitrytsyvtsyn.multiple_mvvm.core.theme.CoreButton
import io.github.dmitrytsyvtsyn.multiple_mvvm.viewmodel.ColorsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class ColorsViewHolder(
    private val context: Context,
    view: LinearLayout = LinearLayout(context)
) : RecyclerView.ViewHolder(view) {

    private val colorsLinearLayout = LinearLayout(context)
    private val navigateButton = CoreButton(context)

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    init {
        view.setPadding(context.dp(16))
        view.orientation = LinearLayout.VERTICAL
        view.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )

        colorsLinearLayout.orientation = LinearLayout.VERTICAL
        colorsLinearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        view.addView(colorsLinearLayout)

        navigateButton.setText(R.string.navigate)
        view.addView(navigateButton)
    }

    fun bind(viewModel: ColorsViewModel) {
        coroutineScope.launchWith(itemView) {
            viewModel.viewState.collect { state ->
                val colors = state.colors
                colorsLinearLayout.removeAllViews()
                colors.forEach { color ->
                    val colorView = createColorView(color, state.color == color)
                    colorView.setOnClickListener {
                        viewModel.handleEvent(ColorsViewModel.Event.Select(color))
                    }
                    colorsLinearLayout.addView(colorView)
                    colorsLinearLayout.addView(Space(context).apply {
                        layoutParams = LinearLayout.LayoutParams(0, context.dp(12))
                    })
                }
            }
        }

        navigateButton.setOnClickListener {
            viewModel.handleEvent(ColorsViewModel.Event.Apply)
        }
    }

    private fun createColorView(color: Int, isChecked: Boolean): View {
        val colorView = FrameLayout(context)
        colorView.background = GradientDrawable().apply {
            setColor(color)
            cornerRadius = context.dp(12f)
        }
        colorView.layoutParams = LinearLayout.LayoutParams(
            context.dp(88),
            context.dp(88)
        )

        if (isChecked) {
            val checkImageView = ImageView(context)
            checkImageView.setColorFilter(Color.WHITE)
            checkImageView.setImageResource(R.drawable.ic_check)
            checkImageView.layoutParams = FrameLayout.LayoutParams(
                context.dp(24),
                context.dp(24)
            ).apply {
                gravity = Gravity.CENTER
            }
            colorView.addView(checkImageView)
        }

        return colorView
    }

}