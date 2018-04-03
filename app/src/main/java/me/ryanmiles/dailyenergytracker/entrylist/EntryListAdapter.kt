package me.ryanmiles.dailyenergytracker.entrylist

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
        return EntryViewHolder(view)
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

    class EntryViewHolder(val view: View) : AbstractExpandableItemViewHolder(view) {

        fun bindEntry(entry: Entry) {
            itemView.title.text = "${entry.date} - ${entry.note}  Size: ${entry.hourlyEntries.size}"
        }
    }

    class HourlyEntryViewHolder(val view: View, private val itemListener: EntryItemListener) : AbstractExpandableItemViewHolder(view) {

        fun bindEntry(entry: Entry, hourlyEntry: HourlyEntry?) {
            if (hourlyEntry != null) {
                itemView.hourly_entry_item_time.text = "${hourlyEntry.time} : ${hourlyEntry.energyNumber}"
                itemView.setOnClickListener {
                    itemListener.onEntryClick(entry, hourlyEntry)
                }
            }
        }
    }

}