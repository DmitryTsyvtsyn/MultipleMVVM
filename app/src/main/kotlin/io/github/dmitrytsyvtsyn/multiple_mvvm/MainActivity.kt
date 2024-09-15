package io.github.dmitrytsyvtsyn.multiple_mvvm

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.dmitrytsyvtsyn.multiple_mvvm.adapter.MainAdapter
import io.github.dmitrytsyvtsyn.multiple_mvvm.core.context.dp
import io.github.dmitrytsyvtsyn.multiple_mvvm.core.context.showColorDialog
import io.github.dmitrytsyvtsyn.multiple_mvvm.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        val recyclerView = RecyclerView(this)
        recyclerView.itemAnimator = null
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        recyclerView.setPadding(dp(16))
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        setContentView(recyclerView)

        ViewCompat.setOnApplyWindowInsetsListener(recyclerView) { _, insets ->
            recyclerView.updatePadding(
                top = dp(16) + insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                bottom = dp(16) + insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            )

            ViewCompat.onApplyWindowInsets(recyclerView, insets)
        }

        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        lifecycleScope.launch {
            viewModel.viewState.collect { state ->
                val items = state.viewModels
                if (items.isNotEmpty()) {
                    recyclerView.adapter = MainAdapter(items)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.action.collect { action ->
                when (action) {
                    is MainViewModel.Action.ShowColor -> {
                        showColorDialog(action.color)
                    }
                }
            }
        }
    }

}
