package me.ryanmiles.dailyenergytracker.addeditentry

import me.ryanmiles.dailyenergytracker.BasePresenter
import me.ryanmiles.dailyenergytracker.BaseView

/*
  * Created by Ryan Miles on 3/23/2018.
  */

/**
 * This specifies the contract between the view and the presenter.
 */
interface AddEditEntryContract {

    interface View : BaseView<Presenter> {
        var isActive: Boolean

        fun showEmptyEntryError()

        fun showEntriesList()

        fun setDate(date: String)

        fun setDateNote(note: String)

        fun setTime(time: String)

        fun setHourlyNote(hourlyNote: String)

        fun showEntryDeleted()

        fun setToCurrentDate()

        fun setToCurrentTime()

        fun setEnergyLevel(energyNumber: Int)

    }

    interface Presenter : BasePresenter {
        var isDataMissing: Boolean

        fun saveEntry(date: String, note: String, time: String, hourlyNote: String, energyNumber: Int)

        fun populateEntry()

        fun populateHourlyEntry()

        fun deleteEntry()

        fun deleteHourlyEntry()
    }
}