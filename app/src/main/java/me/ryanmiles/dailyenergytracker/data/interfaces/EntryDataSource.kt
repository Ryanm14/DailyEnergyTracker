package me.ryanmiles.dailyenergytracker.data.interfaces

import me.ryanmiles.dailyenergytracker.data.model.DailyEntry

/*
 * Created by Ryan Miles on 3/20/2018.
 */
interface EntryDataSource {
    interface LoadEntriesCallback {
        fun onEntriesLoaded(entries: List<DailyEntry>)
        fun onDataNotAvailable()
    }
}