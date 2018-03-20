package me.ryanmiles.dailyenergytracker.data.model

import io.realm.RealmObject
import java.util.*

/**
 * Created by Ryan Miles on 3/20/2018.
 */
data class Entry(val id: UUID,

                 val time: Date,

                 val note: String,

                 val energyNumber: Int) : RealmObject(), Comparable<Entry> {

    override fun compareTo(other: Entry): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}