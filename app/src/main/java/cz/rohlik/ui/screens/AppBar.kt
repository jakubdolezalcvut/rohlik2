package cz.rohlik.ui.screens

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
    appBarActionCallback: AppBarActionCallback,
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
                        appBarActionCallback.onShowSearchClick()
                    },
                ) {
                    Icon(Icons.Filled.Search, null)
                }
            }
            if (screenType.showUiChoice) {
                IconButton(
                    onClick = {
                        appBarActionCallback.onShowUiChoice()
                    },
                ) {
                    Icon(Icons.Filled.Edit, null)
                }
            }
        },
    )
}

internal enum class ScreenType(
    @StringRes val title: Int,
    val showGoBackIcon: Boolean,
    val showSearch: Boolean,
    val showUiChoice: Boolean,
) {
    ArticleList(
        title = R.string.title_article_list,
        showGoBackIcon = false,
        showSearch = true,
        showUiChoice = false,
    ),
    ArticleDetail(
        title = R.string.title_article_detail,
        showGoBackIcon = true,
        showSearch = false,
        showUiChoice = true,
    ),
}

internal class AppBarActionCallback() {
    var onShowSearchClick: () -> Unit = { }
    var onShowUiChoice: () -> Unit = { }
}
