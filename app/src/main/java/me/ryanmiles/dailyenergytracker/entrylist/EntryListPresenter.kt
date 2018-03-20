package me.ryanmiles.dailyenergytracker.entrylist

import me.ryanmiles.dailyenergytracker.data.cache.EntryRepository
import me.ryanmiles.dailyenergytracker.data.interfaces.EntryDataSource
import me.ryanmiles.dailyenergytracker.data.model.DailyEntry
import java.util.*

/*
 * Created by Ryan Miles on 3/20/2018.
 */

/**
 * Listens to user actions from the UI ([EntryListFragment]), retrieves the data and updates the
 * UI as required.
 */
class EntryListPresenter(val entryRepository: EntryRepository, val entryListView: EntryListContract.View) : EntryListContract.Presenter {

    private var firstLoad = true

    init {
        entryListView.presenter = this
    }

    override fun start() {
        loadEntriesFromStart(false)
    }

    override fun loadEntriesFromStart(forceUpdate: Boolean) {
        // Simplification for sample: a network reload will be forced on first load.
        loadEntries(forceUpdate || firstLoad)
        firstLoad = false
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the [TasksDataSource]
     * *
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private fun loadEntries(forceUpdate: Boolean) {
        if (forceUpdate) {
            entryRepository.refreshTasks()
        }

        // The network request might be handled in a different thread so make sure Espresso knows
        // that the app is busy until the response is handled.
        //EspressoIdlingResource.increment() // App is busy until further notice

        entryRepository.getEntries(object : EntryDataSource.LoadEntriesCallback {
            override fun onEntriesLoaded(entries: List<DailyEntry>) {
                //Sort to right date
                Collections.sort(entries)

                // This callback may be called twice, once for the cache and once for loading
                // the data from the server API, so we check before decrementing, otherwise
                // it throws "Counter has been corrupted!" exception.
                //if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                //    EspressoIdlingResource.decrement() // Set app as idle.
                // }
                processTasks(entries)
            }

            override fun onDataNotAvailable() {
                // The view may not be able to handle UI updates anymore
                if (!entryListView.isActive) {
                    return
                }
                entryListView.showLoadingTasksError()
            }
        })
    }

    private fun processTasks(entries: List<DailyEntry>) {
        if (entries.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type.
            entryListView.showNoEntries()
        } else {
            // Show the list of tasks
            entryListView.showEntries(entries)
        }
    }

    override fun addNewEntry() {
        entryListView.showAddEntry()
    }

    override fun openEntryDetails(requestEntry: DailyEntry) {
        entryListView.showEntryDetailsUi(requestEntry.id)
    }

}