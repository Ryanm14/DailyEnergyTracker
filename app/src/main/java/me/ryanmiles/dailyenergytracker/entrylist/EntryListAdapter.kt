package me.ryanmiles.dailyenergytracker.entrylist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.daily_entry_item.view.*
import me.ryanmiles.dailyenergytracker.R
import me.ryanmiles.dailyenergytracker.data.model.DailyEntry
import me.ryanmiles.dailyenergytracker.entrylist.EntryListFragment.EntryItemListener

/*
  * Created by Ryan Miles on 3/20/2018.
  */
class EntryListAdapter(entries: List<DailyEntry>, private val itemListener: EntryItemListener)
    : RecyclerView.Adapter<EntryListAdapter.ViewHolder>() {

    var entries: List<DailyEntry> = entries
        set(tasks) {
            field = tasks
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.daily_entry_item, parent, false)
        return ViewHolder(view, itemListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindImage(entries[position])
    }

    override fun getItemCount(): Int = entries.size

    class ViewHolder(val view: View, private val itemListener: EntryItemListener) : RecyclerView.ViewHolder(view) {

        fun bindImage(entry: DailyEntry) {
            itemView.title.text = entry.toString()
            itemView.setOnClickListener { itemListener.onEntryClick(entry) }
        }
    }

}