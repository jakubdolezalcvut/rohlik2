package cz.rohlik.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.rohlik.R
import cz.rohlik.domain.Article
import cz.rohlik.ui.ArticleListViewModel
import cz.rohlik.ui.theme.RohlikTheme
import cz.rohlik.ui.theme.RohlikTypography
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ArticleList(
    viewModel: ArticleListViewModel,
    appBarActionCallback: AppBarActionCallback,
    onArticleOpen: (Long) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.load()
    }
    when (val usedState = state) {
        is ArticleListViewModel.UiState.Loading -> {
            LoadingList()
        }
        is ArticleListViewModel.UiState.Error -> {
            ErrorScreen(
                title = stringResource(R.string.error_title),
                button = stringResource(R.string.error_button),
                onClick = viewModel::load,
            )
        }
        is ArticleListViewModel.UiState.Empty -> {
            ErrorScreen(
                title = stringResource(R.string.empty_title),
                button = stringResource(R.string.empty_button),
                onClick = viewModel::reset,
            )
        }
        is ArticleListViewModel.UiState.Loaded -> {
            ArticleList(
                articles = usedState.articles,
                search = usedState.search,
                appBarActionCallback = appBarActionCallback,
                onClick = onArticleOpen,
                onSearch = viewModel::search,
            )
        }
    }
}

@Composable
private fun LoadingList() {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(5) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )
        }
    }
}

@Composable
private fun ArticleList(
    articles: ImmutableList<Article>,
    search: String,
    appBarActionCallback: AppBarActionCallback,
    onClick: (Long) -> Unit,
    onSearch: (String) -> Unit,
) {
    Column {
        LazyColumn(
            contentPadding = PaddingValues(8.dp),
        ) {
            itemsIndexed(
                items = articles,
                key = { _, article -> article.id },
            ) { index, article ->
                if (index > 0) {
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                }
                ArticleCard(
                    article = article,
                    onClick = onClick,
                )
            }
        }
        SearchBottomSheet(
            text = search,
            appBarActionCallback = appBarActionCallback,
            onSearch = onSearch,
        )
    }
}

@Composable
private fun ArticleCard(
    article: Article,
    onClick: (Long) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(article.id) },
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = article.title,
                style = RohlikTypography.titleLarge,
            )
            Text(
                text = article.published.formatAsText(),
                style = RohlikTypography.labelSmall,
            )
            Text(
                text = article.summary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = RohlikTypography.bodyMedium,
            )
        }
    }
}

@Composable
@Preview
internal fun LoadingListPreview() {
    RohlikTheme {
        LoadingList()
    }
}

@Composable
@Preview
internal fun ArticlesPreview() {
    RohlikTheme {
        ArticleList(
            articles = articles,
            search = "",
            appBarActionCallback = AppBarActionCallback(),
            onClick = { },
            onSearch = { },
        )
    }
}
