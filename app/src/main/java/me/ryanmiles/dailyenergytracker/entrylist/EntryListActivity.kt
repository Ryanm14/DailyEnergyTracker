package me.ryanmiles.dailyenergytracker.entrylist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_entry_list.*
import me.ryanmiles.dailyenergytracker.R
import me.ryanmiles.dailyenergytracker.data.cache.EntryRepository
import me.ryanmiles.dailyenergytracker.data.source.RealmDataSource
import me.ryanmiles.dailyenergytracker.util.replaceFragmentInActivity

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

        val entryListFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
                as EntryListFragment? ?: EntryListFragment.newInstance().also {
            replaceFragmentInActivity(it, R.id.contentFrame)
        }

        // Create the presenter
        entryListPresenter = EntryListPresenter(EntryRepository.getInstance(
                RealmDataSource.getInstance()), entryListFragment)
    }


}
