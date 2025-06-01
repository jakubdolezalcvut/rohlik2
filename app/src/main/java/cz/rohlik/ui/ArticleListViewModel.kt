package cz.rohlik.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.rohlik.domain.ArticleRepository
import cz.rohlik.domain.Article
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class ArticleListViewModel(
    private val articleRepository: ArticleRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private object Keys {
        const val LAST_SEARCH = "lastSearch"
    }

    sealed interface UiState {

        @Immutable
        data object Loading : UiState

        @Immutable
        data object Error : UiState

        @Immutable
        data object Empty : UiState

        @Immutable
        data class Loaded(
            val articles: ImmutableList<Article>,
            val search: String,
        ) : UiState
    }

    private val _state: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val loadingState: MutableSharedFlow<Unit> = MutableSharedFlow(
        extraBufferCapacity = 1,
    )

    init {
        viewModelScope.launch {
            loadingState.collectLatest { request ->
                val search = savedStateHandle.get<String>(Keys.LAST_SEARCH) ?: ""
                _state.value = UiState.Loading

                articleRepository.loadArticles(
                    offset = 0,
                    limit = 10,
                    search = search,
                )
                    .onSuccess { articles ->
                        _state.value = when {
                            articles.isEmpty() -> UiState.Empty
                            else -> UiState.Loaded(articles, search)
                        }
                    }
                    .onFailure {
                        _state.value = UiState.Error
                    }

            }
        }
    }

    fun load() {
        loadingState.tryEmit(Unit)
    }

    fun search(text: String) {
        savedStateHandle[Keys.LAST_SEARCH] = text
        loadingState.tryEmit(Unit)
    }

    fun reset() {
        savedStateHandle.remove<String>(Keys.LAST_SEARCH)
        loadingState.tryEmit(Unit)
    }
}
