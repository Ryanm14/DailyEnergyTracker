package me.ryanmiles.dailyenergytracker.entrylist

import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder
import kotlinx.android.synthetic.main.daily_entry_item.view.*
import kotlinx.android.synthetic.main.hourly_entry_item.view.*
import me.ryanmiles.dailyenergytracker.R
import me.ryanmiles.dailyenergytracker.data.model.Entry
import me.ryanmiles.dailyenergytracker.data.model.HourlyEntry
import me.ryanmiles.dailyenergytracker.entrylist.EntryListFragment.EntryItemListener
import java.util.*

/*
  * Created by Ryan Miles on 3/20/2018.
  */
class EntryListAdapter(entries: List<Entry>, private val itemListener: EntryItemListener)
    : AbstractExpandableItemAdapter<EntryListAdapter.EntryViewHolder, EntryListAdapter.HourlyEntryViewHolder>() {

    init {
        setHasStableIds(true)
    }

    var entries: List<Entry> = entries
        set(entries) {
            field = entries
            notifyDataSetChanged()
        }

    override fun getGroupCount(): Int = entries.size

    override fun getChildCount(groupPosition: Int): Int = entries[groupPosition].hourlyEntries.size

    override fun getChildId(groupPosition: Int, childPosition: Int): Long =
            getId(entries[groupPosition].hourlyEntries[childPosition]!!.id)

    override fun getGroupId(groupPosition: Int): Long = getId(entries[groupPosition].id)

    private fun getId(id: String): Long {
        val stringId = UUID.fromString(id).leastSignificantBits.toString()
        return stringId.substring(0, 8).toLong()
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_entry_item, parent, false)
        return EntryViewHolder(entries, view, itemListener)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): HourlyEntryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hourly_entry_item, parent, false)
        return HourlyEntryViewHolder(view, itemListener)
    }

    override fun onBindGroupViewHolder(holder: EntryViewHolder, groupPosition: Int, viewType: Int) {
        holder.bindEntry(entries[groupPosition])
    }

    override fun onBindChildViewHolder(holder: HourlyEntryViewHolder, groupPosition: Int, childPosition: Int, viewType: Int) {
        holder.bindEntry(entries[groupPosition], entries[groupPosition].hourlyEntries[childPosition])
    }

    override fun onCheckCanExpandOrCollapseGroup(holder: EntryViewHolder?, groupPosition: Int, x: Int, y: Int, expand: Boolean): Boolean {
        return true
    }

    class EntryViewHolder(val entries: List<Entry>, val view: View, private val itemListener: EntryItemListener) : AbstractExpandableItemViewHolder(view) {

        fun bindEntry(entry: Entry) {
            itemView.title.text = "${entry.date}"
            itemView.entry_item_count.text = "${entry.hourlyEntries.size} Entries"
            itemView.entry_item_avg_energy.text = "${entry.getAvgEnergy()} Avg"
            itemView.add_hourly_entry_item_button.setOnClickListener {
                itemListener.onHourlyEntryAddClick(entry)
                itemListener.collapseView(entries.indexOf(entry))
            }
            itemView.graph_info_button.setOnClickListener {
                itemListener.onEntryGraphClick(entry)
                itemListener.collapseView(entries.indexOf(entry))
            }
        }
    }

    class HourlyEntryViewHolder(val view: View, private val itemListener: EntryItemListener) : AbstractExpandableItemViewHolder(view) {

        fun bindEntry(entry: Entry, hourlyEntry: HourlyEntry?) {
            if (hourlyEntry != null) {
                itemView.hourly_entry_item_time.text = "${hourlyEntry.time}"
                itemView.hourly_entry_item_energy.text = "${hourlyEntry.energyNumber}"
                when (hourlyEntry.energyNumber) {
                    10 -> itemView.hourly_entry_item_energy.setTextColor(ContextCompat.getColor(view.context, R.color.peak_energy))
                    7, 8, 9 -> itemView.hourly_entry_item_energy.setTextColor(ContextCompat.getColor(view.context, R.color.high_energy))
                    4, 5, 6 -> itemView.hourly_entry_item_energy.setTextColor(ContextCompat.getColor(view.context, R.color.medium_energy))
                    0, 1, 2, 3 -> itemView.hourly_entry_item_energy.setTextColor(ContextCompat.getColor(view.context, R.color.low_energy))
                }
                itemView.setOnClickListener {
                    itemListener.onHourlyEntryClick(entry, hourlyEntry)
                }
            }
        }
    }

}