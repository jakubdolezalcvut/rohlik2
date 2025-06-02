package cz.rohlik.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import cz.rohlik.R
import cz.rohlik.domain.Article
import cz.rohlik.domain.UiChoice
import cz.rohlik.ui.ArticleDetailViewModel
import cz.rohlik.ui.theme.RohlikTheme
import cz.rohlik.ui.theme.RohlikTypography
import timber.log.Timber

@Composable
internal fun ArticleDetail(
    viewModel: ArticleDetailViewModel,
    appBarActionCallback: AppBarActionCallback,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.load()
    }
    when (val usedState = state) {
        is ArticleDetailViewModel.UiState.Loading -> {
            LoadingDetail()
        }
        is ArticleDetailViewModel.UiState.Error -> {
            ErrorScreen(
                title = stringResource(R.string.error_title),
                button = stringResource(R.string.error_button),
                onClick = viewModel::load,
            )
        }
        is ArticleDetailViewModel.UiState.Loaded -> {
            ArticleSwitch(
                article = usedState.article,
                uiChoice = usedState.uiChoice,
                appBarActionCallback = appBarActionCallback,
                onSwitch = viewModel::setUiChoice,
            )
        }
    }
}

@Composable
private fun LoadingDetail() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val isSith = isSystemInDarkTheme()
        val saberColor = if (isSith) Color(0xFFFF1C1C) else Color(0xFF3D8EFF)
        LightsaberSpinner(color = saberColor)
    }
}

@Composable
private fun ArticleSwitch(
    article: Article,
    uiChoice: UiChoice,
    appBarActionCallback: AppBarActionCallback,
    onSwitch: (UiChoice) -> Unit,
) {
    Column {
        when (uiChoice) {
            UiChoice.HUMAN -> ArticleHuman(article)
            UiChoice.AI -> ArticleAi(article)
        }
        SwitchUiBottomSheet(
            uiChoice = uiChoice,
            appBarActionCallback = appBarActionCallback,
            onSwitch = onSwitch,
        )
    }
}

@Composable
private fun ArticleHuman(
    article: Article,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
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
            text = article.authors.joinToString(),
            style = RohlikTypography.labelSmall,
        )
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(article.imageUrl.toString())
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_launcher_background),
            error = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null,
            contentScale = ContentScale.None,
            onError = { error -> Timber.w("$error") },
        )
        Text(
            text = article.summary,
            style = RohlikTypography.bodyMedium,
        )
    }
}

@Composable
private fun ArticleAi(
    article: Article,
) {
    val scrollState = rememberScrollState()
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .matchParentSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(article.imageUrl.toString())
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_launcher_background),
            error = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            onError = { error -> Timber.w("$error") },
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
            )
            Text(
                text = article.published.formatAsText(),
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray,
            )
            Text(
                text = article.authors.joinToString(),
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray,
            )
            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
            )
        }
    }
}

@Composable
@Preview
internal fun LoadingDetailPreview() {
    RohlikTheme {
        LoadingDetail()
    }
}


@Composable
@Preview
internal fun ArticleHumanPreview() {
    RohlikTheme {
        ArticleHuman(
            article = article,
        )
    }
}

@Composable
@Preview
internal fun ArticleAiPreview() {
    RohlikTheme {
        ArticleAi(
            article = article,
        )
    }
}
