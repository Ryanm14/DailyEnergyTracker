package me.ryanmiles.dailyenergytracker.data.interfaces

import io.realm.RealmList
import me.ryanmiles.dailyenergytracker.data.model.Entry
import me.ryanmiles.dailyenergytracker.data.model.HourlyEntry

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

    interface GetHourlyEntryCallback {

        fun onHourlyEntryLoaded(hourlyEntry: HourlyEntry)

        fun onDataNotAvailable()
    }

    fun getEntries(callback: LoadEntriesCallback)

    fun getEntry(entryId: String, callback: GetEntryCallback)

    fun saveEntry(entry: Entry): Entry

    fun saveHourlyEntry(hourlyEntries: RealmList<HourlyEntry>, hourlyEntry: HourlyEntry): HourlyEntry

    fun refreshEntries()

    fun deleteAllEntries()

    fun deleteEntry(entryId: String)

    fun deleteHourlyEntry(hourlyEntryId: String)

    fun getHourlyEntry(hourlyId: String, callback: GetHourlyEntryCallback)

    fun saveNewHourlyEntry(hourlyEntries: RealmList<HourlyEntry>, newHourlyEntry: HourlyEntry)

    fun getEntryWithDate(date: String): Entry?


}
