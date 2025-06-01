package cz.rohlik.ui.screens

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cz.rohlik.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun AppBar(
    screenType: ScreenType,
    searchNavigationCallback: SearchNavigationCallback,
    navigateUp: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(screenType.title),
            )
        },
        navigationIcon = {
            if (screenType.showGoBackIcon) {
                IconButton(
                    onClick = navigateUp,
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            }
        },
        actions = {
            if (screenType.showSearch) {
                IconButton(
                    onClick = {
                        searchNavigationCallback.onAppBarAction()
                    },
                ) {
                    Icon(Icons.Filled.Search, null)
                }
            }
        },
    )
}

internal enum class ScreenType(
    @StringRes val title: Int,
    val showGoBackIcon: Boolean,
    val showSearch: Boolean,
) {
    ArticleList(
        title = R.string.title_article_list,
        showGoBackIcon = false,
        showSearch = true,
    ),
    ArticleDetail(
        title = R.string.title_article_detail,
        showGoBackIcon = true,
        showSearch = false,
    ),
}

internal class SearchNavigationCallback() {
    var onAppBarAction: () -> Unit = { }
}
