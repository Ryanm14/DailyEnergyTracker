package me.ryanmiles.dailyenergytracker.entrylist

import me.ryanmiles.dailyenergytracker.BasePresenter
import me.ryanmiles.dailyenergytracker.BaseView
import me.ryanmiles.dailyenergytracker.data.model.Entry

/*
 * Created by Ryan Miles on 3/20/2018.
 */

/**
 * This specifies the contract between the view and the presenter.
 */
interface EntryListContract {

    /** EntryList implementation of the [BaseView] **/
    interface View : BaseView<Presenter> {

        var isActive: Boolean

        fun showEntries(entries: List<Entry>)

        fun showAddEntry()

        fun showEntryDetailsUi(entryId: String)

        fun showNoEntries()

        fun showLoadingTasksError()
    }

    /** EntryList implementation of the [BasePresenter] **/
    interface Presenter : BasePresenter {
        fun loadEntriesFromStart(forceUpdate: Boolean)
        fun openEntryDetails(requestEntry: Entry)
        fun addNewEntry()
    }


}