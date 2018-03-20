package me.ryanmiles.dailyenergytracker.entrylist

import me.ryanmiles.dailyenergytracker.data.cache.EntryRepository

/*
 * Created by Ryan Miles on 3/20/2018.
 */

/**
 * Listens to user actions from the UI ([EntryListFragment]), retrieves the data and updates the
 * UI as required.
 */
class EntryListPresenter(val entryRepository: EntryRepository, val entryListView: EntryListContract.View) : EntryListContract.Presenter {

    init {
        entryListView.presenter = this
    }

    override fun start() {

    }

}