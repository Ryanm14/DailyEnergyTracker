package me.ryanmiles.dailyenergytracker.singleentrygraph

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

class SingleEntryGraphPresenter(private val entryId: String?,
                                private val entryRepository: EntryDataSource,
                                val singleEntryGraphView: SingleEntryGraphContract.View,
                                override var isDataMissing: Boolean) : SingleEntryGraphContract.Presenter,
        EntryDataSource.GetEntryCallback {

    init {
        singleEntryGraphView.presenter = this
    }

    override fun start() {
        if (entryId != null && isDataMissing) {
            populateGraph()
        }
    }


    override fun populateGraph() {
        if (entryId == null) {
            throw RuntimeException("populateGraph() was called but the entry is new.")
        }
        entryRepository.getEntry(entryId, this)
    }

    override fun onEntryLoaded(entry: Entry) {
        // The view may not be able to handle UI updates anymore
        if (singleEntryGraphView.isActive) {
        }
    }

    override fun onDataNotAvailable() {
        // The view may not be able to handle UI updates anymore
        if (singleEntryGraphView.isActive) {
            singleEntryGraphView.showEmptyEntryError()
        }
    }
}