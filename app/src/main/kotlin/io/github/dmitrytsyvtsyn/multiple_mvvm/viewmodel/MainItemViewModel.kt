package io.github.dmitrytsyvtsyn.multiple_mvvm.viewmodel

import kotlinx.coroutines.CoroutineScope

sealed class MainItemViewModel(val uiScope: CoroutineScope)