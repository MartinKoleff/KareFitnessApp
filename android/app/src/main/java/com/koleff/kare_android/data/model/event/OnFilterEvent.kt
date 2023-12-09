package com.koleff.kare_android.data.model.event

sealed class OnFilterEvent{
    object DumbbellFilter : OnFilterEvent()
    object BarbellFilter : OnFilterEvent()
    object MachineFilter : OnFilterEvent()
    object NoFilter : OnFilterEvent()
}
