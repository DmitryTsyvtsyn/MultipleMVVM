# MultipleMVVM

Задача: создать RecyclerView с элементами, которые являются ViewModel и сделать это более менее красиво в архитектурном плане

В качестве решения я выбрал простенькую реализацию MVI паттерна, основная идея: ViewModel принимает только Event, а на выходе обновляет ViewState или посылает Action, последний можно обрабатывать не только в UI, но и подписываться на него в других ViewModel'ях:

```kotlin
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

    // подписываемся на Action'ы из ColorsViewModel и обрабатываем их как Event в текущей
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
```
