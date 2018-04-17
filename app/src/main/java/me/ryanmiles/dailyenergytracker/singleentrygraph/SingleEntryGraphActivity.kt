package me.ryanmiles.dailyenergytracker.singleentrygraph

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.ryanmiles.dailyenergytracker.R
import me.ryanmiles.dailyenergytracker.addeditentry.AddEditEntryFragment
import me.ryanmiles.dailyenergytracker.data.cache.EntryRepository
import me.ryanmiles.dailyenergytracker.data.source.RealmDataSource
import me.ryanmiles.dailyenergytracker.util.replaceFragmentInActivity
import me.ryanmiles.dailyenergytracker.util.setupActionBar

class SingleEntryGraphActivity : AppCompatActivity() {

    private lateinit var singleEntryGraphPresenter: SingleEntryGraphPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.singleentrygraph_activity)

        val entryId = intent.getStringExtra(AddEditEntryFragment.ARGUMENT_EDIT_ENTRY_ID)

        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setTitle(if (entryId == null) R.string.add_entry else R.string.edit_entry)
        }

        val singleEntryFragment =
                supportFragmentManager.findFragmentById(R.id.contentFrame) as SingleEntryGraphFragment?
                        ?: SingleEntryGraphFragment.newInstance(entryId).also {
                            replaceFragmentInActivity(it, R.id.contentFrame)
                        }

        val shouldLoadDataFromRepo =
        // Prevent the presenter from loading data from the repository if this is a config change.
        // Data might not have loaded when the config change happen, so we saved the state.
                savedInstanceState?.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY) ?: true


        // Create the presenter
        singleEntryGraphPresenter = SingleEntryGraphPresenter(entryId,
                EntryRepository.getInstance(
                        RealmDataSource.getInstance()), singleEntryFragment,
                shouldLoadDataFromRepo)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Save the state so that next time we know if we need to refresh data.
        super.onSaveInstanceState(outState.apply {
            putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, singleEntryGraphPresenter.isDataMissing)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY"
    }

}
