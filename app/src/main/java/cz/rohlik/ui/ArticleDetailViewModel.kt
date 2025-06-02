package cz.rohlik.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.rohlik.domain.Article
import cz.rohlik.domain.ArticleRepository
import cz.rohlik.domain.UiChoice
import cz.rohlik.preferences.UiChoiceDataSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

internal class ArticleDetailViewModel(
    private val articleRepository: ArticleRepository,
    private val savedStateHandle: SavedStateHandle,
    private val uiChoiceDataSource: UiChoiceDataSource,
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
        data class Loaded(
            val article: Article,
            val uiChoice: UiChoice,
        ) : UiState
    }

    private val _state: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val loadingState: MutableSharedFlow<Unit> = MutableSharedFlow(
        extraBufferCapacity = 1,
    )

    private val articleId: Long
        get() = savedStateHandle.get<Long>(Keys.ARTICLE_ID) ?: error("Missing ${Keys.ARTICLE_ID}")

    init {
        viewModelScope.launch {
            loadingState.collectLatest {
                _state.value = UiState.Loading

                articleRepository.loadArticle(
                    articleId = articleId,
                )
                    .onSuccess { article ->
                        _state.value = UiState.Loaded(
                            article = article,
                            uiChoice = uiChoiceDataSource.uiChoiceFlow.first(),
                        )
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

    fun setUiChoice(uiChoice: UiChoice) {
        viewModelScope.launch {
            uiChoiceDataSource.update(uiChoice)
            loadingState.tryEmit(Unit)
        }
    }
}
