package me.ryanmiles.dailyenergytracker.addeditentry

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.ryanmiles.dailyenergytracker.R
import me.ryanmiles.dailyenergytracker.data.cache.EntryRepository
import me.ryanmiles.dailyenergytracker.data.source.RealmDataSource
import me.ryanmiles.dailyenergytracker.util.replaceFragmentInActivity
import me.ryanmiles.dailyenergytracker.util.setupActionBar

class AddEditEntryActivity : AppCompatActivity() {

    private lateinit var addEditEntryPresenter: AddEditEntryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addentry_activity)

        val taskId = intent.getStringExtra(AddEditEntryFragment.ARGUMENT_EDIT_ENTRY_ID)
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setTitle(if (taskId == null) R.string.add_entry else R.string.edit_entry)
        }

        val addEditEntryFragment =
                supportFragmentManager.findFragmentById(R.id.contentFrame) as AddEditEntryFragment?
                        ?: AddEditEntryFragment.newInstance(taskId).also {
                            replaceFragmentInActivity(it, R.id.contentFrame)
                        }

        val shouldLoadDataFromRepo =
        // Prevent the presenter from loading data from the repository if this is a config change.
        // Data might not have loaded when the config change happen, so we saved the state.
                savedInstanceState?.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY) ?: true


        // Create the presenter
        addEditEntryPresenter = AddEditEntryPresenter(taskId,
                EntryRepository.getInstance(
                        RealmDataSource.getInstance()), addEditEntryFragment,
                shouldLoadDataFromRepo)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Save the state so that next time we know if we need to refresh data.
        super.onSaveInstanceState(outState.apply {
            putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, addEditEntryPresenter.isDataMissing)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY"
        const val REQUEST_ADD_ENTRY = 1
    }

}
