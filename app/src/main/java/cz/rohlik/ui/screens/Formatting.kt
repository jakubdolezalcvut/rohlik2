package cz.rohlik.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import cz.rohlik.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

@Composable
internal fun LocalDateTime.formatAsText(): String {
    val context = LocalContext.current

    val customFormat = remember(context) {
        LocalDateTime.Format {
            dayOfMonth(Padding.NONE)
            char(' ')
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            hour();
            char(':');
            minute()
        }
    }
    return stringResource(R.string.published, format(customFormat))
}
