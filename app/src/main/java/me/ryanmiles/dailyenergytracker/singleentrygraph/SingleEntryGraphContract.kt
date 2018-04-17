package me.ryanmiles.dailyenergytracker.singleentrygraph

import me.ryanmiles.dailyenergytracker.BasePresenter
import me.ryanmiles.dailyenergytracker.BaseView

interface SingleEntryGraphContract {

    interface View : BaseView<Presenter> {
        var isActive: Boolean

        fun setGraph()

        fun showEmptyEntryError()
    }

    interface Presenter : BasePresenter {
        var isDataMissing: Boolean

        fun populateGraph()
    }
}