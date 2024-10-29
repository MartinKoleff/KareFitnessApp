package com.koleff.kare_android.common

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.koleff.kare_android.data.model.dto.KareLanguage

object LanguageManager {

    fun getSupportedLanguages(): List<KareLanguage>{
        return emptyList()
    }

    fun changeLanguage(context: Context, selectedLanguage: KareLanguage){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //TODO: add custom prompt...

            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(selectedLanguage.countryCode)
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(selectedLanguage.countryCode))
        }
    }
}