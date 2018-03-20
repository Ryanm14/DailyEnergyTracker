package me.ryanmiles.dailyenergytracker.entrylist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.ryanmiles.dailyenergytracker.R

/*
 * Created by Ryan Miles on 3/20/2018.
 */

/**
 * Display a list of [DailyEntry]s. Users can view all, edit, and add new new [DailyEntry]s
 */
class EntryListFragment : Fragment(), EntryListContract.View {

    override lateinit var presenter: EntryListContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_entry_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }
}
