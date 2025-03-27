package com.koleff.kare_android.common

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.koleff.kare_android.R
import com.koleff.kare_android.data.model.dto.KareLanguage

object LanguageManager {

    fun getSupportedLanguages(): List<KareLanguage> {
        return listOf(
            KareLanguage(
                name = "Bulgarian",
                countryCode = "",
                countryImage = R.drawable.bulgaria_flag
            ),
            KareLanguage(
                name = "Chinese",
                countryCode = "",
                countryImage = R.drawable.china_flag
            ),
            KareLanguage(
                name = "English",
                countryCode = "",
                countryImage = R.drawable.united_kingdom_flag
            ),
            KareLanguage(
                name = "Finnish",
                countryCode = "",
                countryImage = R.drawable.finland_flag
            ),
            KareLanguage(
                name = "French",
                countryCode = "",
                countryImage = R.drawable.france_flag
            ),
            KareLanguage(
                name = "German",
                countryCode = "",
                countryImage = R.drawable.germany_flag
            ),
            KareLanguage(
                name = "Hindi",
                countryCode = "",
                countryImage = R.drawable.india_flag
            ),
            KareLanguage(
                name = "Japanese",
                countryCode = "",
                countryImage = R.drawable.japan_flag
            ),
            KareLanguage(
                name = "Dutch",
                countryCode = "",
                countryImage = R.drawable.netherlands_flag
            ),
            KareLanguage(
                name = "Norse",
                countryCode = "",
                countryImage = R.drawable.norway_flag
            ),
            KareLanguage(
                name = "Romanian",
                countryCode = "",
                countryImage = R.drawable.romania_flag
            ),
            KareLanguage(
                name = "Russian",
                countryCode = "",
                countryImage = R.drawable.russia_flag
            ),
            KareLanguage(
                name = "Serbian",
                countryCode = "",
                countryImage = R.drawable.serbia_flag
            ),
            KareLanguage(
                name = "Korean",
                countryCode = "",
                countryImage = R.drawable.south_korea_flag
            ),
            KareLanguage(
                name = "Spanish",
                countryCode = "",
                countryImage = R.drawable.spain_flag
            ),
            KareLanguage(
                name = "Swedish",
                countryCode = "",
                countryImage = R.drawable.sweden_flag
            )
        )
    }

    fun changeLanguage(context: Context, selectedLanguage: KareLanguage) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //TODO: add custom prompt...

            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(selectedLanguage.countryCode)
        } else {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(
                    selectedLanguage.countryCode
                )
            )
        }
    }
}