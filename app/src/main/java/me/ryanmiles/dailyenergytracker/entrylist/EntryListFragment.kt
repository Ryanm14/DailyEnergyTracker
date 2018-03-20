package me.ryanmiles.dailyenergytracker.entrylist

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.ryanmiles.dailyenergytracker.R

/**
 * A placeholder fragment containing a simple view.
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
