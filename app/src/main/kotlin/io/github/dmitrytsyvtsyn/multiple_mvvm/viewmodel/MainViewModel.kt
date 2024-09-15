package io.github.dmitrytsyvtsyn.multiple_mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.dmitrytsyvtsyn.multiple_mvvm.data.ColorsRepository
import io.github.dmitrytsyvtsyn.multiple_mvvm.data.NumbersRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _viewState = MutableStateFlow(
        State(viewModels = emptyList())
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
            is Event.ShowColor -> handleEvent(event)
        }
    }

    private fun handleEvent(event: Event.Init) {
        _viewState.value = State(viewModels = listOf(
            NumbersViewModel(viewModelScope, NumbersRepository()),
            ColorsViewModel(viewModelScope, ColorsRepository()).apply { addActionHandler() },
            NumbersViewModel(viewModelScope, NumbersRepository()),
            ColorsViewModel(viewModelScope, ColorsRepository()).apply { addActionHandler() },
            NumbersViewModel(viewModelScope, NumbersRepository()),
            ColorsViewModel(viewModelScope, ColorsRepository()).apply { addActionHandler() }
        ))
    }

    private fun handleEvent(event: Event.ShowColor) = viewModelScope.launch {
        _action.emit(Action.ShowColor(event.color))
    }

    private fun ColorsViewModel.addActionHandler() = viewModelScope.launch {
        action.collect { action ->
            when (action) {
                is ColorsViewModel.Action.Apply -> {
                    handleEvent(Event.ShowColor(action.color))
                }
            }
        }
    }

    data class State(val viewModels: List<MainItemViewModel>)

    sealed interface Event {
        data object Init : Event
        class ShowColor(val color: Int) : Event
    }

    sealed interface Action {
        class ShowColor(val color: Int) : Action
    }

}