package me.ryanmiles.dailyenergytracker.entrylist

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_entry_list.*
import me.ryanmiles.dailyenergytracker.R
import me.ryanmiles.dailyenergytracker.data.model.DailyEntry
import me.ryanmiles.dailyenergytracker.util.showSnackBar
import java.util.*

/*
 * Created by Ryan Miles on 3/20/2018.
 */

/**
 * Display a list of [DailyEntry]s. Users can view all, edit, and add new new [DailyEntry]s
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
        override fun onEntryClick(clickedEntry: DailyEntry) {
            //presenter.openTaskDetails(clickedTask)
        }
    }


    private val recyclerViewAdapter = EntryListAdapter(ArrayList(0), itemListener)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_entry_list, container, false)

        with(root) {
            val recyclerView = findViewById<RecyclerView>(R.id.entries_recycler_view).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = recyclerViewAdapter
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
                //presenter.addNewTask()
            }
        }

        setHasOptionsMenu(true)
        return root
    }

    override fun showEntries(entries: List<DailyEntry>) {
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
        // val intent = Intent(context, AddEditTaskActivity::class.java)
        // startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK)
    }

    override fun showEntryDetailsUi(entryId: String) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        //  val intent = Intent(context, TaskDetailActivity::class.java).apply {
        //     putExtra(TaskDetailActivity.EXTRA_TASK_ID, taskId)
        //  }
        // startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    interface EntryItemListener {

        fun onEntryClick(clickedEntry: DailyEntry)
    }

    companion object {

        fun newInstance() = EntryListFragment()
    }
}