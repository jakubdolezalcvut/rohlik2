package cz.rohlik

import app.cash.paparazzi.DeviceConfig
import cz.rohlik.ui.screens.ArticleHumanPreview
import cz.rohlik.ui.screens.ArticleListPreview
import cz.rohlik.ui.screens.ErrorScreenPreview
import cz.rohlik.ui.screens.SearchBottomSheetPreview
import cz.rohlik.ui.screens.SwitchUiBottomSheetPreview
import io.kotest.core.spec.style.FunSpec
import app.cash.paparazzi.Paparazzi

internal class ArticleScreenshots : FunSpec({

    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
        theme = "android:Theme.Material.Light.NoActionBar",
    )
    extension(PaparazziExtension(paparazzi))

    test("ArticleListPreview") {
        paparazzi.snapshot {
            ArticleListPreview()
        }
    }

    test("ArticleHumanPreview") {
        paparazzi.snapshot {
            ArticleHumanPreview()
        }
    }

    test("SearchBottomSheetPreview") {
        paparazzi.snapshot {
            SearchBottomSheetPreview()
        }
    }

    test("SwitchUiBottomSheetPreview") {
        paparazzi.snapshot {
            SwitchUiBottomSheetPreview()
        }
    }

    test("ErrorScreenPreview") {
        paparazzi.snapshot {
            ErrorScreenPreview()
        }
    }
})
