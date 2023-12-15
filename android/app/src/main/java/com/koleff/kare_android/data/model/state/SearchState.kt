package com.koleff.kare_android.data.model.state

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow

data class SearchState (
    var isSearching: Boolean = false,
    val searchText: String = ""
)