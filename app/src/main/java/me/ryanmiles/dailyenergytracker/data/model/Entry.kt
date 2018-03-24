package me.ryanmiles.dailyenergytracker.data.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

/*
 * Created by Ryan Miles on 3/20/2018.
 */
@RealmClass
open class Entry(

        @PrimaryKey var id: String = UUID.randomUUID().toString(),

        var date: String = "",

        var note: String = "",

        var hourlyEntries: RealmList<HourlyEntry> = RealmList())

    : RealmObject(), Comparable<Entry> {

    override fun compareTo(other: Entry): Int {
        return id.compareTo(other.id)
    }
}