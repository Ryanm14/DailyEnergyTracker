package me.ryanmiles.dailyenergytracker.entrylist

import me.ryanmiles.dailyenergytracker.BasePresenter
import me.ryanmiles.dailyenergytracker.BaseView

/**
 * Created by Ryan Miles on 3/20/2018.
 */
interface EntryListContract {
    interface View : BaseView<Presenter>

    interface Presenter : BasePresenter


}