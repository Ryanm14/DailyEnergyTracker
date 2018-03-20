package me.ryanmiles.dailyenergytracker.entrylist

import me.ryanmiles.dailyenergytracker.BasePresenter
import me.ryanmiles.dailyenergytracker.BaseView
import me.ryanmiles.dailyenergytracker.data.model.DailyEntry

/*
 * Created by Ryan Miles on 3/20/2018.
 */

/**
 * This specifies the contract between the view and the presenter.
 */
interface EntryListContract {

    /** EntryList implementation of the [BaseView] **/
    interface View : BaseView<Presenter> {

        fun showEntries(entries: List<DailyEntry>)

        fun showAddEntry()

        fun showEntryDetailsUi(entryId: String)
    }

    /** EntryList implementation of the [BasePresenter] **/
    interface Presenter : BasePresenter


}