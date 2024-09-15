package io.github.dmitrytsyvtsyn.multiple_mvvm.core.coroutines

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CoroutineScope.launchWith(view: View, block: suspend CoroutineScope.() -> Unit) {
    var job = launch(block = block)

    view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {

        override fun onViewAttachedToWindow(v: View) {
            if (!job.isActive) {
                job = launch(block = block)
            }
        }

        override fun onViewDetachedFromWindow(v: View) {
            job.cancel()
        }

    })
}