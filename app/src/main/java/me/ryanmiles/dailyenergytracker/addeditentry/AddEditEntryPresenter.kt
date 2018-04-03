package me.ryanmiles.dailyenergytracker.addeditentry

import io.realm.RealmList
import me.ryanmiles.dailyenergytracker.data.interfaces.EntryDataSource
import me.ryanmiles.dailyenergytracker.data.model.Entry
import me.ryanmiles.dailyenergytracker.data.model.HourlyEntry

/*
  * Created by Ryan Miles on 3/23/2018.
  */

/**
 * Listens to user actions from the UI ([AddEditEntryFragment]), retrieves the data and updates
 * the UI as required.
 * @param entryId ID of the entry to edit or null for a new entry
 *
 * @param entryRepository a repository of data for entries
 *
 * @param addEntryView the add/edit view
 *
 * @param isDataMissing whether data needs to be loaded or not (for config changes)
 */

class AddEditEntryPresenter(private val entryId: String?,
                            private val hourlyId: String?,
                            private val entryRepository: EntryDataSource,
                            val addEntryView: AddEditEntryContact.View,
                            override var isDataMissing: Boolean) : AddEditEntryContact.Presenter,
        EntryDataSource.GetEntryCallback, EntryDataSource.GetHourlyEntryCallback {

    init {
        addEntryView.presenter = this
    }

    override fun start() {
        if (entryId != null && isDataMissing) {
            populateEntry()
        } else {
            addEntryView.setToCurrentDate()
        }

        if (hourlyId != null && isDataMissing) {
            populateHourlyEntry()
        } else {
            addEntryView.setToCurrentTime()
        }
    }

    override fun saveEntry(date: String, note: String, time: String, hourlyNote: String, energyNumber: Int) {
        if (entryId == null) {
            createEntry(date, note, time, hourlyNote, energyNumber)
        } else {
            entryRepository.getEntry(entryId, object : EntryDataSource.GetEntryCallback {
                override fun onEntryLoaded(entry: Entry) {
                    updateEntry(date, note, entry.hourlyEntries)
                    updateHourlyEntry(time, hourlyNote, energyNumber)
                }

                override fun onDataNotAvailable() {
                    addEntryView.showEmptyEntryError()
                }
            })
        }
    }

    override fun populateHourlyEntry() {
        if (hourlyId == null) {
            throw RuntimeException("populateHourlyEntry() was called but the hourly entry is new")
        }
        entryRepository.getHourlyEntry(hourlyId, this)
    }

    override fun populateEntry() {
        if (entryId == null) {
            throw RuntimeException("populateEntry() was called but the entry is new.")
        }
        entryRepository.getEntry(entryId, this)
    }

    override fun onEntryLoaded(entry: Entry) {
        // The view may not be able to handle UI updates anymore
        if (addEntryView.isActive) {
            addEntryView.setDate(entry.date)
            addEntryView.setDateNote(entry.note)
        }
    }

    override fun onHourlyEntryLoaded(hourlyEntry: HourlyEntry) {
        if (addEntryView.isActive) {
            addEntryView.setTime(hourlyEntry.time)
            addEntryView.setHourlyNote(hourlyEntry.note)
            addEntryView.setEnergyLevel(hourlyEntry.energyNumber)
        }
        isDataMissing = false
    }

    override fun onDataNotAvailable() {
        // The view may not be able to handle UI updates anymore
        if (addEntryView.isActive) {
            addEntryView.showEmptyEntryError()
        }
    }

    private fun createEntry(date: String, note: String, time: String, hourlyNote: String, energyNumber: Int) {
        val newEntry = Entry(date, note)
        val newHourlyEntry = HourlyEntry(time, hourlyNote, energyNumber)
        newEntry.addHourlyEntry(newHourlyEntry)
        if (newEntry.isEmpty || newHourlyEntry.isEmpty) {
            addEntryView.showEmptyEntryError()
        } else {
            entryRepository.saveEntry(newEntry)
            addEntryView.showEntriesList()

        }
    }

    private fun updateHourlyEntry(time: String, hourlyNote: String, energyNumber: Int) {
        if (hourlyId == null || entryId == null) {
            throw RuntimeException("updateHourlyEntry() was called but the hourlyEntry is new.")
        }
        entryRepository.saveHourlyEntry(HourlyEntry(time, hourlyNote, energyNumber, hourlyId))
        addEntryView.showEntriesList() // After an edit, go back to the list.
    }

    override fun deleteEntry() {
        if (entryId == null) {
            addEntryView.showEntryDeleted()
        } else {
            if (entryId.isEmpty()) {
                addEntryView.showEmptyEntryError()
                return
            }
            entryRepository.deleteEntry(entryId)
            addEntryView.showEntryDeleted()
        }
    }

    override fun deleteHourlyEntry() {
        if (hourlyId == null) {
            addEntryView.showEntryDeleted()
        } else {
            if (hourlyId.isEmpty()) {
                addEntryView.showEmptyEntryError()
                return
            }
            entryRepository.deleteHourlyEntry(hourlyId)
            addEntryView.showEntryDeleted()
        }
    }

    private fun updateEntry(date: String, note: String, hourlyEntries: RealmList<HourlyEntry>) {
        if (entryId == null) {
            throw RuntimeException("updateEntry() was called but the entry is new.")
        }
        entryRepository.saveEntry(Entry(date, note, hourlyEntries, entryId))
    }
}