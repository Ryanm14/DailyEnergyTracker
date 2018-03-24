package me.ryanmiles.dailyenergytracker.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

/*
 * Created by Ryan Miles on 3/20/2018.
 */
@RealmClass
open class HourlyEntry(

        @PrimaryKey var id: String = UUID.randomUUID().toString(),

        var time: String = "",

        var note: String = "",

        var energyNumber: Int = 0)

    : RealmObject(), Comparable<HourlyEntry> {

    override fun compareTo(other: HourlyEntry): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}