package com.koleff.kare_android.data.model.event

sealed class OnSearchEvent(val searchText: String = ""){
    class OnToggleSearch : OnSearchEvent()
    class OnSearchTextChange(searchText: String) : OnSearchEvent(searchText)
}
