package me.ryanmiles.dailyenergytracker.addeditentry

import me.ryanmiles.dailyenergytracker.BasePresenter
import me.ryanmiles.dailyenergytracker.BaseView

/*
  * Created by Ryan Miles on 3/23/2018.
  */

/**
 * This specifies the contract between the view and the presenter.
 */
interface AddEditEntryContact {

    interface View : BaseView<Presenter> {
        var isActive: Boolean

        fun showEmptyEntryError()

        fun showEntriesList()

        fun setDate(date: String)

        fun setNote(note: String)

        fun showEntryDeleted()

        fun setToCurrentDate()
    }

    interface Presenter : BasePresenter {
        var isDataMissing: Boolean

        fun saveEntry(date: String, note: String)

        fun populateEntry()

        fun deleteEntry()
    }
}