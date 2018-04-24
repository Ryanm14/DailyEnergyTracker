package me.ryanmiles.dailyenergytracker.addeditentry


import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.*
import android.widget.TextView
import me.ryanmiles.dailyenergytracker.R
import me.ryanmiles.dailyenergytracker.util.showSnackBar
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import org.jetbrains.anko.find
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
    private lateinit var time: TextView
    private lateinit var hourlyNote: TextView
    private lateinit var energyBar: DiscreteSeekBar

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_add_edit_entry, container, false)
        with(root) {
            date = find(R.id.add_entry_date)
            note = find(R.id.add_entry_note)
            time = find(R.id.add_entry_hour_time)
            hourlyNote = find(R.id.add_entry_hour_note)
            energyBar = find(R.id.add_entry_energy_bar)

            date.setOnClickListener {
                val myFormat = "MM/dd/yy"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val myCalendar = Calendar.getInstance()
                if (date.text.isNotEmpty()) {
                    myCalendar.time = sdf.parse(date.text.toString())
                }
                val dateCall = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    myCalendar.set(year, monthOfYear, dayOfMonth)
                    setDate(sdf.format(myCalendar.time))
                }

                DatePickerDialog(context, dateCall, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }

            time.setOnClickListener {
                val mCurrentTime = Calendar.getInstance()
                val pattern = "h:mm a"
                val timeFormat = SimpleDateFormat(pattern, Locale.US)
                if (time.text.isNotEmpty()) {
                    mCurrentTime.time = timeFormat.parse(time.text.toString())
                }

                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                    val isPM = selectedHour >= 12
                    setTime(String.format("%02d:%02d %s", if (selectedHour == 12 || selectedHour == 0) 12 else selectedHour % 12, selectedMinute, if (isPM) "PM" else "AM"))
                }, mCurrentTime.get(Calendar.HOUR_OF_DAY), mCurrentTime.get(Calendar.MINUTE), false)

                mTimePicker.setTitle("Select Time")
                mTimePicker.show()
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
                presenter.saveEntry(date.text.toString(), note.text.toString(), time.text.toString(),
                        hourlyNote.text.toString(), energyBar.progress)
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

    override fun setToCurrentDate() {
        val myFormat = "MM/dd/yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        setDate(sdf.format(Calendar.getInstance().time))
    }

    override fun setToCurrentTime() {
        val df = SimpleDateFormat("h:mm a", Locale.US)
        val time = df.format(Calendar.getInstance().time)
        setTime(time)
    }

    override fun setDateNote(note: String) {
        this.note.text = note
    }

    override fun setHourlyNote(hourlyNote: String) {
        this.hourlyNote.text = hourlyNote
    }

    override fun setTime(time: String) {
        this.time.text = time
    }

    override fun setEnergyLevel(energyNumber: Int) {
        this.energyBar.progress = energyNumber
    }

    override fun showEntryDeleted() {
        activity!!.finish()
    }

    companion object {
        const val ARGUMENT_EDIT_ENTRY_ID = "EDIT_ENTRY_ID"
        const val ARGUMENT_EDIT_HOURLY_ENTRY_ID = "EDIT_HOURLY_ENTRY_ID"

        fun newInstance(taskId: String?) =
                AddEditEntryFragment().apply {
                    arguments = Bundle().apply {
                        putString(AddEditEntryFragment.ARGUMENT_EDIT_ENTRY_ID, taskId)
                    }
                }
    }
}