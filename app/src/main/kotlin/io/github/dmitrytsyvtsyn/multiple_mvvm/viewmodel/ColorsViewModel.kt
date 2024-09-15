package io.github.dmitrytsyvtsyn.multiple_mvvm.viewmodel

import io.github.dmitrytsyvtsyn.multiple_mvvm.data.ColorsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ColorsViewModel(
    scope: CoroutineScope,
    private val repository: ColorsRepository
) : MainItemViewModel(scope) {

    private val _viewState = MutableStateFlow(
        State(
            color = 0,
            colors = emptyList()
        )
    )
    val viewState = _viewState.asStateFlow()

    private val _action = MutableSharedFlow<Action>()
    val action = _action.asSharedFlow()

    init {
        handleEvent(Event.Init)
    }

    fun handleEvent(event: Event) {
        when (event) {
            is Event.Init -> handleEvent(event)
            is Event.Select -> handleEvent(event)
            is Event.Apply -> handleEvent(event)
        }
    }

    private fun handleEvent(event: Event.Init) = uiScope.launch {
        _viewState.update { state -> state.copy(colors = repository.fetchAll()) }
    }

    private fun handleEvent(event: Event.Select) = uiScope.launch {
        _viewState.update { state -> state.copy(color = event.color) }
    }

    private fun handleEvent(event: Event.Apply) = uiScope.launch {
        _action.emit(Action.Apply(_viewState.value.color))
    }

    data class State(
        val color: Int,
        val colors: List<Int>
    )

    sealed interface Event {
        data object Init : Event
        class Select(val color: Int) : Event
        data object Apply : Event
    }

    sealed interface Action {
        class Apply(val color: Int) : Action
    }

}