package me.ryanmiles.dailyenergytracker.data.source

import android.support.annotation.VisibleForTesting
import io.realm.Realm
import me.ryanmiles.dailyenergytracker.data.interfaces.EntryDataSource
import me.ryanmiles.dailyenergytracker.data.model.DailyEntry

/*
 * Created by Ryan Miles on 3/20/2018.
 */

class RealmDataSource : EntryDataSource {

    val realm = Realm.getDefaultInstance()

    override fun getEntries(callback: EntryDataSource.LoadEntriesCallback) {
        val entries = realm.where(DailyEntry::class.java).findAll()
        if (entries.isEmpty()) {
            callback.onDataNotAvailable()
        } else {
            callback.onEntriesLoaded(entries)
        }
    }

    override fun getEntry(entryId: String, callback: EntryDataSource.getEntryCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private var INSTANCE: RealmDataSource? = null

        @JvmStatic
        fun getInstance(): RealmDataSource {
            if (INSTANCE == null) {
                synchronized(RealmDataSource::javaClass) {
                    INSTANCE = RealmDataSource()
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }
}
