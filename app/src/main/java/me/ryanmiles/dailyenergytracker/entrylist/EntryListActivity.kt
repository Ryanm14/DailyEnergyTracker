package me.ryanmiles.dailyenergytracker.entrylist

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_entry_list.*
import me.ryanmiles.dailyenergytracker.R

/*
 * Created by Ryan Miles on 3/20/2018.
 */

/**
 * The starting activity for [EntryListFragment] and sets up overhead UI
 */
class EntryListActivity : AppCompatActivity() {

    private lateinit var entryListPresenter: EntryListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

}
