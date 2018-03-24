package me.ryanmiles.dailyenergytracker.data.interfaces

import me.ryanmiles.dailyenergytracker.data.model.Entry

/*
 * Created by Ryan Miles on 3/20/2018.
 */
interface EntryDataSource {

    interface LoadEntriesCallback {

        fun onEntriesLoaded(entries: List<Entry>)

        fun onDataNotAvailable()
    }

    interface GetEntryCallback {

        fun onEntryLoaded(entry: Entry)

        fun onDataNotAvailable()
    }

    fun getEntries(callback: LoadEntriesCallback)

    fun getEntry(entryId: String, callback: GetEntryCallback)

    fun saveEntry(entry: Entry): Entry

    fun refreshEntries()

    fun deleteAllEntries()

    fun deleteEntry(entryId: String)

}
