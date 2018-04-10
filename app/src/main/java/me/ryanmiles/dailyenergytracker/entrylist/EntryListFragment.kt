package me.ryanmiles.dailyenergytracker.entrylist

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager
import kotlinx.android.synthetic.main.fragment_entry_list.*
import me.ryanmiles.dailyenergytracker.R
import me.ryanmiles.dailyenergytracker.addeditentry.AddEditEntryActivity
import me.ryanmiles.dailyenergytracker.addeditentry.AddEditEntryFragment
import me.ryanmiles.dailyenergytracker.data.model.Entry
import me.ryanmiles.dailyenergytracker.data.model.HourlyEntry
import me.ryanmiles.dailyenergytracker.util.showSnackBar
import java.util.*


/*
 * Created by Ryan Miles on 3/20/2018.
 */

/**
 * Display a list of [Entry]s. Users can view all, edit, and add new new [Entry]s
 */
class EntryListFragment : Fragment(), EntryListContract.View {

    override lateinit var presenter: EntryListContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    private lateinit var noEntriesView: View
    private lateinit var noEntryIcon: ImageView
    private lateinit var noEntryMainView: TextView
    private lateinit var noEntryAddView: TextView
    private lateinit var entriesView: LinearLayout
    private lateinit var entriesLabelView: TextView

    /**
     * Listener for clicks on tasks in the ListView.
     */
    private var itemListener: EntryItemListener = object : EntryItemListener {
        override fun onHourlyEntryClick(clickedEntry: Entry, clickedHourlyEntry: HourlyEntry) {
            presenter.openEditEntry(clickedEntry, clickedHourlyEntry)
        }

        override fun onHourlyEntryAddClick(clickedEntry: Entry) {
            presenter.openEditEntry(clickedEntry, null)
        }

        override fun collapseView(position: Int) {
            expMgr.collapseGroup(position)
        }
    }

    private val expMgr = RecyclerViewExpandableItemManager(null)
    private val recyclerViewAdapter = EntryListAdapter(ArrayList(0), itemListener)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_entry_list, container, false)

        with(root) {
            findViewById<RecyclerView>(R.id.entries_recycler_view).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = expMgr.createWrappedAdapter(recyclerViewAdapter)
                (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                itemAnimator = DefaultItemAnimator()
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                expMgr.attachRecyclerView(this)
            }

            entriesLabelView = findViewById(R.id.entriesLabel)
            entriesView = findViewById(R.id.entriesLinearLayout)

            // Set up  no tasks view
            noEntriesView = findViewById(R.id.noEntries)
            noEntryIcon = findViewById(R.id.noEntriesIcon)
            noEntryMainView = findViewById(R.id.noEntriesMain)
            noEntryAddView = (findViewById<TextView>(R.id.noEntriesAdd)).also {
                it.setOnClickListener {
                    showAddEntry()
                }
            }
        }

        // Set up floating action button
        activity!!.findViewById<FloatingActionButton>(R.id.fab_add_entry).apply {
            setImageResource(R.drawable.ic_add)
            setOnClickListener {
                presenter.addNewEntry()
            }
        }

        setHasOptionsMenu(true)
        return root
    }

    override fun showEntries(entries: List<Entry>) {
        recyclerViewAdapter.entries = entries
        entriesView.visibility = View.VISIBLE
        noEntriesView.visibility = View.GONE
    }

    override fun showNoEntries() {
        entriesView.visibility = View.GONE
        noEntriesView.visibility = View.VISIBLE

        noEntriesMain.text = resources.getString(R.string.no_entries_all)
        noEntriesIcon.setImageResource(R.drawable.ic_assignment_turned_in_24dp)
        noEntriesAdd.visibility = View.GONE
    }

    override fun showLoadingTasksError() {
        showMessage(getString(R.string.loading_entries_error))
    }

    private fun showMessage(message: String) {
        view?.showSnackBar(message, Snackbar.LENGTH_LONG)
    }

    override fun showAddEntry() {
        val intent = Intent(context, AddEditEntryActivity::class.java)
        startActivityForResult(intent, AddEditEntryActivity.REQUEST_ADD_ENTRY)
    }

    override fun showEditEntry(entryId: String, hourlyId: String?) {
        val intent = Intent(context, AddEditEntryActivity::class.java)
        intent.putExtra(AddEditEntryFragment.ARGUMENT_EDIT_ENTRY_ID, entryId)
        intent.putExtra(AddEditEntryFragment.ARGUMENT_EDIT_HOURLY_ENTRY_ID, hourlyId)
        startActivityForResult(intent, REQUEST_EDIT_ENTRY)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    interface EntryItemListener {

        fun onHourlyEntryClick(clickedEntry: Entry, clickedHourlyEntry: HourlyEntry)

        fun onHourlyEntryAddClick(clickedEntry: Entry)

        fun collapseView(position: Int)
    }

    companion object {

        private const val REQUEST_EDIT_ENTRY = 1

        fun newInstance() = EntryListFragment()
    }
}
