package me.ryanmiles.dailyenergytracker.data.model

import io.realm.RealmList
import io.realm.RealmObject
import java.util.*

/*
 * Created by Ryan Miles on 3/20/2018.
 */
data class DailyEntry(

        val id: UUID,

        val date: Date,

        val note: String,

        val entries: RealmList<Entry>) : RealmObject(), Comparable<DailyEntry> {

    override fun compareTo(other: DailyEntry): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}