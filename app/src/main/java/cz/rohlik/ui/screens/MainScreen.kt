package cz.rohlik.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.rohlik.ui.ArticleDetailViewModel
import cz.rohlik.ui.ArticleListViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MainScreen() {
    val appBarActionCallback = remember { AppBarActionCallback() }
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val route = backStackEntry?.destination

    val screenType = remember(route) {
        when {
            route == null -> ScreenType.ArticleList
            route.hasRoute<Destination.ArticleList>() -> ScreenType.ArticleList
            route.hasRoute<Destination.ArticleDetail>() -> ScreenType.ArticleDetail
            else -> ScreenType.ArticleList
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                screenType = screenType,
                appBarActionCallback = appBarActionCallback,
                navigateUp = navController::popBackStack,
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.ArticleList,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<Destination.ArticleList> { navBackStackEntry ->
                val viewModel = koinViewModel<ArticleListViewModel>(viewModelStoreOwner = navBackStackEntry)

                ArticleList(
                    viewModel = viewModel,
                    appBarActionCallback = appBarActionCallback,
                ) { articleId ->
                    navController.navigate(
                        Destination.ArticleDetail(articleId)
                    )
                }
            }
            composable<Destination.ArticleDetail> { navBackStackEntry ->
                val viewModel = koinViewModel<ArticleDetailViewModel>(viewModelStoreOwner = navBackStackEntry)

                ArticleDetail(
                    viewModel = viewModel,
                    appBarActionCallback = appBarActionCallback,
                )
            }

        }
    }
}

internal sealed interface Destination {

    @Serializable
    object ArticleList : Destination

    @Serializable
    data class ArticleDetail(val articleId: Long) : Destination
}
