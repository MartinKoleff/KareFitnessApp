package com.koleff.kare_android.ui.state

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow

data class SearchState (
    var isSearching: Boolean = false,
    val searchText: String = ""
)