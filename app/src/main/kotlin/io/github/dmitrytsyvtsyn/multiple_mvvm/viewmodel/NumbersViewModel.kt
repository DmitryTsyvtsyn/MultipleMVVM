package io.github.dmitrytsyvtsyn.multiple_mvvm.viewmodel

import io.github.dmitrytsyvtsyn.multiple_mvvm.data.NumbersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NumbersViewModel(
    scope: CoroutineScope,
    private val repository: NumbersRepository
) : MainItemViewModel(scope) {

    private val _viewState = MutableStateFlow(
        State(numbers = emptyList())
    )
    val viewState = _viewState.asStateFlow()

    init {
        handleEvent(Event.Init)
    }

    fun handleEvent(event: Event) {
        when (event) {
            is Event.Init -> handleEvent(event)
            is Event.Add -> handleEvent(event)
            is Event.Remove -> handleEvent(event)
        }
    }

    private fun handleEvent(event: Event.Init) = uiScope.launch {
        val numbers = repository.fetchAll()
        _viewState.update { state -> state.copy(numbers = numbers) }
    }

    private fun handleEvent(event: Event.Add) = uiScope.launch {
        repository.addRandomNumber()
        handleEvent(Event.Init)
    }

    private fun handleEvent(event: Event.Remove) = uiScope.launch {
        repository.removeLastNumber()
        handleEvent(Event.Init)
    }

    data class State(val numbers: List<Int>)

    sealed interface Event {
        data object Init : Event
        data object Add : Event
        data object Remove : Event
    }
}