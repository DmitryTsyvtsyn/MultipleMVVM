package io.github.dmitrytsyvtsyn.multiple_mvvm.adapter

import android.content.Context
import android.text.TextUtils
import android.widget.LinearLayout
import android.widget.Space
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import io.github.dmitrytsyvtsyn.multiple_mvvm.R
import io.github.dmitrytsyvtsyn.multiple_mvvm.core.context.dp
import io.github.dmitrytsyvtsyn.multiple_mvvm.core.coroutines.launchWith
import io.github.dmitrytsyvtsyn.multiple_mvvm.core.theme.CoreButton
import io.github.dmitrytsyvtsyn.multiple_mvvm.core.theme.CoreTextView
import io.github.dmitrytsyvtsyn.multiple_mvvm.core.view.fontSize
import io.github.dmitrytsyvtsyn.multiple_mvvm.viewmodel.NumbersViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class NumbersViewHolder(
    context: Context,
    view: LinearLayout = LinearLayout(context)
) : RecyclerView.ViewHolder(view) {

    private val numbersTextView = CoreTextView(context)
    private val addButton = CoreButton(context)
    private val removeButton = CoreButton(context)

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    init {
        view.orientation = LinearLayout.VERTICAL
        view.setPadding(context.dp(16))
        view.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            context.dp(200)
        )

        numbersTextView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            weight = 1f
        }
        numbersTextView.ellipsize = TextUtils.TruncateAt.END
        numbersTextView.fontSize(20f)
        view.addView(numbersTextView)
        view.addView(Space(context).apply {
            layoutParams = LinearLayout.LayoutParams(0, context.dp(16))
        })

        addButton.setText(R.string.add)
        addButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            weight = 1f
        }

        removeButton.setText(R.string.remove)
        removeButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            weight = 1f
        }

        val buttonsLinearLayout = LinearLayout(context)
        buttonsLinearLayout.orientation = LinearLayout.HORIZONTAL
        buttonsLinearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        buttonsLinearLayout.addView(addButton)
        buttonsLinearLayout.addView(Space(context).apply {
            layoutParams = LinearLayout.LayoutParams(context.dp(16), 0)
        })
        buttonsLinearLayout.addView(removeButton)
        view.addView(buttonsLinearLayout)
    }

    fun bind(viewModel: NumbersViewModel) {
        addButton.setOnClickListener {
            viewModel.handleEvent(NumbersViewModel.Event.Add)
        }

        removeButton.setOnClickListener {
            viewModel.handleEvent(NumbersViewModel.Event.Remove)
        }

        coroutineScope.launchWith(itemView) {
            viewModel.viewState.collect { state ->
                val numbersString = state.numbers.joinToString(", ")
                numbersTextView.text = numbersString
            }
        }
    }

}