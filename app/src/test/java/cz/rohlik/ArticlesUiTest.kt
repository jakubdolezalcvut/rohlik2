package cz.rohlik

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import cz.rohlik.ui.screens.ArticlesPreview
import cz.rohlik.ui.screens.ErrorPreview
import cz.rohlik.ui.screens.LoadingPreview
import org.junit.Rule
import org.junit.Test

internal class ArticlesUiTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        theme = "android:Theme.Material.Light.NoActionBar",
    )

    @Test
    fun loadingPreview() {
        paparazzi.snapshot {
            LoadingPreview()
        }
    }

    @Test
    fun errorPreview() {
        paparazzi.snapshot {
            ErrorPreview()
        }
    }

    @Test
    fun articlesPreview() {
        paparazzi.snapshot {
            ArticlesPreview()
        }
    }
}
