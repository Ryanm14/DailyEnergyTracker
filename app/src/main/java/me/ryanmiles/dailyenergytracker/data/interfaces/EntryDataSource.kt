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

    interface getEntryCallback {

        fun onEntryLoaded(entry: DailyEntry)

        fun onDataNotAvailable()
    }

    fun getEntries(callback: LoadEntriesCallback)

    fun getEntry(entryId: String, callback: getEntryCallback)

}
