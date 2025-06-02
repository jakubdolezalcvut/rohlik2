package cz.rohlik.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.rohlik.R
import cz.rohlik.ui.theme.RohlikTheme
import cz.rohlik.ui.theme.RohlikTypography
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun SearchBottomSheet(
    text: String,
    appBarActionCallback: AppBarActionCallback,
    onSearch: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    LaunchedEffect(Unit) {
        appBarActionCallback.onShowSearchClick = {
            showBottomSheet = true
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            appBarActionCallback.onShowSearchClick = { }
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                showBottomSheet = false
            },
        ) {
            SearchBottomSheet(
                text = text,
                onClick = { typedText ->
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                            onSearch(typedText)
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun SearchBottomSheet(
    text: String,
    onClick: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    var typedText by remember { mutableStateOf(text) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        Text(
            text = stringResource(R.string.search_title),
            style = RohlikTypography.titleLarge,
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = typedText,
            maxLines = 1,
            onValueChange = { text ->
                typedText = text
            },
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onClick(typedText)
            },
        ) {
            Text(
                text = stringResource(R.string.search_button),
                style = RohlikTypography.bodyMedium,
            )
        }
    }
}

@Composable
@Preview
internal fun SearchBottomSheetPreview() {
    RohlikTheme {
        SearchBottomSheet(
            text = "Space",
            onClick = { },
        )
    }
}
