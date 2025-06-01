package cz.rohlik.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.rohlik.R
import cz.rohlik.ui.theme.RohlikTheme
import cz.rohlik.ui.theme.RohlikTypography

@Composable
internal fun ErrorScreen(
    title: String,
    button: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                style = RohlikTypography.titleLarge,
            )
            Button(
                onClick = onClick,
            ) {
                Text(
                    text = button,
                    style = RohlikTypography.bodyMedium,
                )
            }
        }
    }
}

@Composable
@Preview
internal fun ErrorScreenPreview() {
    RohlikTheme {
        ErrorScreen(
            title = stringResource(R.string.error_title),
            button = stringResource(R.string.error_button),
            onClick = { },
        )
    }
}
