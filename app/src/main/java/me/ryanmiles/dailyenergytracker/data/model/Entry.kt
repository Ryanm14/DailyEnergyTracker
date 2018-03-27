package me.ryanmiles.dailyenergytracker.data.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.text.SimpleDateFormat
import java.util.*

/*
 * Created by Ryan Miles on 3/20/2018.
 */
@RealmClass
open class Entry(

        var date: String = "",

        var note: String = "",

        var hourlyEntries: RealmList<HourlyEntry> = RealmList(),

        @PrimaryKey var id: String = UUID.randomUUID().toString())

    : RealmObject(), Comparable<Entry> {

    override fun compareTo(other: Entry): Int {
        val pattern = "MM/dd/yyyy"
        val dateFormat = SimpleDateFormat(pattern)
        val thisDate = dateFormat.parse(date)
        val otherDate = dateFormat.parse(other.date)
        return otherDate.compareTo(thisDate)
    }

    val isEmpty
        get() = date.isEmpty()

    fun addHourlyEntry(hourlyEntry: HourlyEntry) {
        hourlyEntries.add(hourlyEntry)
    }
}