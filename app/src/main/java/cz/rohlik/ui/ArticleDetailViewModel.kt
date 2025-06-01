package cz.rohlik.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.rohlik.domain.Article
import cz.rohlik.domain.ArticleRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class ArticleDetailViewModel(
    private val articleRepository: ArticleRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private object Keys {
        const val ARTICLE_ID = "articleId"
    }

    sealed interface UiState {

        @Immutable
        data object Loading : UiState

        @Immutable
        data object Error : UiState

        @Immutable
        data class Loaded(val article: Article) : UiState
    }

    private val _state: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val loadingState: MutableSharedFlow<Unit> = MutableSharedFlow(
        extraBufferCapacity = 1,
    )

    init {
        viewModelScope.launch {
            loadingState.collectLatest {
                _state.value = UiState.Loading

                articleRepository.loadArticle(
                    articleId = savedStateHandle.get<Long>(Keys.ARTICLE_ID) ?: error("Missing ${Keys.ARTICLE_ID}"),
                )
                    .onSuccess { article ->
                        _state.value = UiState.Loaded(article)
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
}
