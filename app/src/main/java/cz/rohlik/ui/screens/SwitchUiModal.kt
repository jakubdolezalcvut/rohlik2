package cz.rohlik.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.rohlik.R
import cz.rohlik.domain.UiChoice
import cz.rohlik.ui.theme.RohlikTheme
import cz.rohlik.ui.theme.RohlikTypography

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun SwitchUiBottomSheet(
    uiChoice: UiChoice,
    appBarActionCallback: AppBarActionCallback,
    onSwitch: (UiChoice) -> Unit,
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    LaunchedEffect(Unit) {
        appBarActionCallback.onShowUiChoice = {
            showBottomSheet = true
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            appBarActionCallback.onShowUiChoice = { }
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                showBottomSheet = false
            },
        ) {
            SwitchUiScreen(
                choice = uiChoice,
                onSwitch = onSwitch,
            )
        }
    }
}

@Composable
private fun SwitchUiScreen(
    choice: UiChoice,
    onSwitch: (UiChoice) -> Unit,
) {
    var newChoice by remember { mutableStateOf(choice) }
    var message = remember(newChoice) {
        derivedStateOf {
            when (newChoice) {
                UiChoice.HUMAN -> R.string.switch_message_human
                UiChoice.AI -> R.string.switch_message_ai
            }
        }
    }
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        Text(
            text = stringResource(R.string.switch_title),
            style = RohlikTypography.titleLarge,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Switch(
                checked = newChoice == UiChoice.AI,
                onCheckedChange = { checked ->
                    newChoice = if (checked) UiChoice.AI else UiChoice.HUMAN
                    onSwitch(newChoice)
                }
            )
            Text(
                text = stringResource(message.value),
                style = RohlikTypography.bodyMedium,
            )
        }
    }
}

@Composable
@Preview
internal fun SwitchUiScreenBottomSheetPreview() {
    RohlikTheme {
        SwitchUiScreen(
            choice = UiChoice.HUMAN,
            onSwitch = { },
        )
    }
}
