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
open class DailyEntry(

        @PrimaryKey var id: String = UUID.randomUUID().toString(),

        var date: Date = Date(),

        var note: String = "",

        var entries: RealmList<Entry> = RealmList())

    : RealmObject(), Comparable<DailyEntry> {

    override fun compareTo(other: DailyEntry): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}