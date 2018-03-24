package me.ryanmiles.dailyenergytracker.addeditentry

import io.realm.RealmList
import me.ryanmiles.dailyenergytracker.data.interfaces.EntryDataSource
import me.ryanmiles.dailyenergytracker.data.model.Entry

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
                            val entryRepository: EntryDataSource,
                            val addEntryView: AddEditEntryContact.View,
                            override var isDataMissing: Boolean) : AddEditEntryContact.Presenter,
        EntryDataSource.GetEntryCallback {

    init {
        addEntryView.presenter = this
    }

    override fun start() {
        if (entryId != null && isDataMissing) {
            populateEntry()
        }
    }

    override fun saveEntry(date: String, note: String) {
        if (entryId == null) {
            createEntry(date, note)
        } else {
            updateEntry(date, note)
        }
    }

    override fun populateEntry() {
        if (entryId == null) {
            throw RuntimeException("populateTask() was called but task is new.")
        }
        entryRepository.getEntry(entryId, this)
    }

    override fun onEntryLoaded(entry: Entry) {
        // The view may not be able to handle UI updates anymore
        if (addEntryView.isActive) {
            addEntryView.setDate(entry.date)
            addEntryView.setNote(entry.note)
        }
        isDataMissing = false
    }

    override fun onDataNotAvailable() {
        // The view may not be able to handle UI updates anymore
        if (addEntryView.isActive) {
            addEntryView.showEmptyEntryError()
        }
    }

    private fun createEntry(date: String, note: String) {
        val newEntry = Entry(date, note)
        if (newEntry.isEmpty) {
            addEntryView.showEmptyEntryError()
        } else {
            entryRepository.saveEntry(newEntry)
            addEntryView.showEntriesList()
        }
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

    private fun updateEntry(date: String, note: String) {
        if (entryId == null) {
            throw RuntimeException("updateTask() was called but task is new.")
        }
        entryRepository.saveEntry(Entry(date, note, RealmList(), entryId))
        addEntryView.showEntriesList() // After an edit, go back to the list.
    }
}