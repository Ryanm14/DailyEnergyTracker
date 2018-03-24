package me.ryanmiles.dailyenergytracker.addeditentry


import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.*
import android.widget.TextView
import me.ryanmiles.dailyenergytracker.R
import me.ryanmiles.dailyenergytracker.util.showSnackBar
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class AddEditEntryFragment : Fragment(), AddEditEntryContact.View {

    override lateinit var presenter: AddEditEntryContact.Presenter
    override var isActive = false
        get() = isAdded

    private lateinit var date: TextView
    private lateinit var note: TextView

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_add_edit_entry, container, false)
        with(root) {
            date = findViewById(R.id.add_entry_date)
            note = findViewById(R.id.add_entry_note)

            date.setOnClickListener {
                val myCalendar = Calendar.getInstance()
                val dateCall = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    myCalendar.set(Calendar.YEAR, year)
                    myCalendar.set(Calendar.MONTH, monthOfYear)
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)


                    val myFormat = "MM/dd/yy" //In which you need put here
                    val sdf = SimpleDateFormat(myFormat, Locale.US)

                    setDate(sdf.format(myCalendar.time))
                }

                DatePickerDialog(context, dateCall, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
        setHasOptionsMenu(true)
        return root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val deletePressed = item.itemId == R.id.menu_delete_entry
        if (deletePressed) presenter.deleteEntry()
        return deletePressed
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_addeditfragment, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(activity!!.findViewById<FloatingActionButton>(R.id.fab_edit_entry_done)) {
            setImageResource(R.drawable.ic_done)
            setOnClickListener {
                presenter.saveEntry(date.text.toString(), note.text.toString())
            }
        }
    }

    override fun showEmptyEntryError() {
        date.showSnackBar(getString(R.string.empty_task_message), Snackbar.LENGTH_LONG)
    }

    override fun showEntriesList() {
        with(activity) {
            this!!.setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun setDate(date: String) {
        this.date.text = date
    }

    override fun setNote(note: String) {
        this.note.text = note
    }

    override fun showEntryDeleted() {
        activity!!.finish()
    }

    companion object {
        const val ARGUMENT_EDIT_ENTRY_ID = "EDIT_ENTRY_ID"

        fun newInstance(taskId: String?) =
                AddEditEntryFragment().apply {
                    arguments = Bundle().apply {
                        putString(AddEditEntryFragment.ARGUMENT_EDIT_ENTRY_ID, taskId)
                    }
                }
    }
}