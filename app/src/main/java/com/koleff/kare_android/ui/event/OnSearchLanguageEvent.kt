package com.koleff.kare_android.ui.event

import com.koleff.kare_android.data.model.dto.KareLanguage
import com.koleff.kare_android.data.model.dto.WorkoutDto

sealed class OnSearchLanguageEvent(){
    class OnToggleSearch(val isSearching: Boolean = false, val languages: List<KareLanguage>) : OnSearchLanguageEvent()
    class OnSearchTextChange(val searchText: String, val languages: List<KareLanguage>) : OnSearchLanguageEvent()
}
